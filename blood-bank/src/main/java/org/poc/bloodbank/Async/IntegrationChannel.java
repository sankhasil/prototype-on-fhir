package org.poc.bloodbank.Async;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.hl7.fhir.r4.model.BaseResource;
import org.poc.bloodbank.config.AuditBloodBankConfig;
import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.entity.model.ExtendedAppointment;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;
import org.poc.bloodbank.enums.AuditTrailEnum;
import org.poc.bloodbank.property.IntegrationConfigProperties;
import org.poc.core.base.util.GeneralFhirUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;


@EnableAsync
@Component
public class IntegrationChannel {

    private final static String URI_PROTOCOL = "http://";
    private final static String URI_DELIMITER = ":";

    Logger integrationChannelLogger = LoggerFactory.getLogger(IntegrationChannel.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    IntegrationConfigProperties integrationConfigProperties;

    @Autowired
    AuditBloodBankConfig auditOpdConfig;

    public void setIntegrationConfigProperties(IntegrationConfigProperties integrationConfigProperties) {
        this.integrationConfigProperties = integrationConfigProperties;
    }

    @Async
    public void sendSlotDataToScheduler(String id, JsonObject slotDataRequestBody) throws JsonProcessingException {
        integrationChannelLogger.info("The Service ID : " + integrationConfigProperties.getSchedulerServiceID());
        Application application = eurekaClient
                .getApplication(integrationConfigProperties.getSchedulerServiceID());
        integrationChannelLogger.info("eureka client " + eurekaClient + " property "
                + integrationConfigProperties.getSchedulerServiceID());
        integrationChannelLogger.info("Application client " + application.getName() + " application Instance size "
                + application.getInstances().size());

        final InstanceInfo instanceInfo = application.getInstances().get(0);
        if (!slotDataRequestBody.isJsonNull() && slotDataRequestBody.get("status") != null) {
            final String apiOpearation = "/slot/update/" + id;
            final HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            final HttpEntity<String> slotDataRequestBodyEntity = new HttpEntity<String>(slotDataRequestBody.toString(), headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            String response = restTemplate.exchange(prepareURIForRest(instanceInfo, apiOpearation), HttpMethod.PATCH, slotDataRequestBodyEntity, String.class).getBody();

            integrationChannelLogger.info("Integration Channel return :" + response);
        }
    }

    /**
     * Prepare the Order transaction URI.
     *
     * @param instanceInfo an object instance of type {@link InstanceInfo}.
     * @param apiOperation API Operation service path.
     * @return a {@link String} value.
     */
    private String prepareURIForRest(final InstanceInfo instanceInfo, final String apiOperation) {
        return (URI_PROTOCOL + instanceInfo.getIPAddr() + URI_DELIMITER + instanceInfo.getPort() + apiOperation);
    }

    @Async
    public String sendResourceToElastic(BaseResource resourceObject, boolean update) throws JsonProcessingException {
        integrationChannelLogger.info("The Service ID : " + integrationConfigProperties.getAuditTrailServiceID());
        Application application = eurekaClient
                .getApplication(integrationConfigProperties.getAuditTrailServiceID());
        integrationChannelLogger.info("eureka client " + eurekaClient + " property "
                + integrationConfigProperties.getAuditTrailServiceID());
        integrationChannelLogger.info("Application client " + application.getName() + " application Instance size "
                + application.getInstances().size());

        final InstanceInfo instanceInfo = application.getInstances().get(0);
        RestTemplate restTemplateForElastic = new RestTemplate();
        String auditTrailDataObject = prepareAuditTrailDataObject(resourceObject, update);
        integrationChannelLogger.info("Request Object for Audit trail: " + auditTrailDataObject);
        String responseFromElastic = "";
        if (update) {
            final String apiOpearation = "/auditTrail/" + GeneralFhirUtility.trimResourceId(resourceObject.getId());
            restTemplateForElastic.put(prepareURIForRest(instanceInfo, apiOpearation), auditTrailDataObject);
            responseFromElastic = AuditTrailEnum.UPDATED_RESPONSE.getValue();
        } else {
            responseFromElastic = restTemplateForElastic.postForObject(prepareURIForRest(instanceInfo, "/auditTrail/"),
                    auditTrailDataObject, String.class);
            integrationChannelLogger.info("Response from Elastic: " + responseFromElastic);
        }

        return responseFromElastic;
    }

    /**
     * @param baseResource
     * @param update
     * @return auditTrailObject
     */
    private String prepareAuditTrailDataObject(BaseResource baseResource, boolean update) throws JsonProcessingException {
        JsonObject auditTrailObject = new JsonObject();
        if (update) {
            auditTrailObject.addProperty("id", GeneralFhirUtility.trimResourceId(baseResource.getId()));
            auditTrailObject.addProperty("requestType", String.valueOf(HttpMethod.PUT));
            auditTrailObject.addProperty("action", AuditTrailEnum.UPDATED_ACTION.getValue());
            auditTrailObject.addProperty("response", AuditTrailEnum.UPDATED_RESPONSE.getValue());

//			auditTrailObject.addProperty("createdBy", String.valueOf(baseResource.getCreatedBy()));
//			auditTrailObject.addProperty("updatedBy", String.valueOf(baseResource.getUpdatedBy()));
            auditTrailObject.addProperty("responseCode", String.valueOf(HttpStatus.OK.value()));
        } else {
            auditTrailObject.addProperty("id", baseResource.getId());
            auditTrailObject.addProperty("requestType", String.valueOf(HttpMethod.POST));
            auditTrailObject.addProperty("action", AuditTrailEnum.CREATED_ACTION.getValue());
            auditTrailObject.addProperty("response", AuditTrailEnum.CREATED_RESPONSE.getValue());
//			auditTrailObject.addProperty("createdBy", String.valueOf(baseResource.getCreatedBy()));
            auditTrailObject.addProperty("responseCode", String.valueOf(HttpStatus.CREATED.value()));
        }

        auditTrailObject.addProperty("serviceName", auditOpdConfig.getApplicationName());
        auditTrailObject.addProperty("objectName", baseResource.getClass().getSimpleName());
        auditTrailObject.addProperty("envName", auditOpdConfig.getActiveProfile());
//		auditTrailObject.addProperty("unitCode", String.valueOf(baseResource.getUnitCode()));
//		auditTrailObject.addProperty("orgCode", String.valueOf(baseResource.getOrgCode()));
        String extendedPatientResponseJson = objectMapper.writeValueAsString(baseResource);
        auditTrailObject.addProperty("raw_text", extendedPatientResponseJson);

        return auditTrailObject.toString();
    }


    @Async
    public void sendExtendedServiceRequest(ExtendedServiceRequest serviceRequest) {
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = integrationConfigProperties.getMirthBaseUrlServiceRequest() + "/tempProcedure/";
        String serviceRequestJson = null;
        try {
            serviceRequestJson = objectMapper.writeValueAsString(serviceRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> response = restTemplate.postForEntity(url, serviceRequestJson, String.class);
        if (response != null) {
            integrationChannelLogger.info("Response from clinic care:" + response);
        }
    }

    @Async
    public void sendEmailNotificationToRegPatient(ExtendedServiceRequest extendedServiceRequest, Donor extendedPatient, ExtendedAppointment extendedAppointment) {
        integrationChannelLogger.info(" Email Notification :");
        try {
            HashMap message = new HashMap<>();
            HashMap messageContent = new HashMap<>();
            String patientName = extendedPatient.getName().size() > 0 ? extendedPatient.getName().get(0).getText()
                    : extendedPatient.getFullName().toString();
            if (!extendedPatient.getName().get(1).getText().isEmpty() && extendedPatient.getName().get(1).getText() != null) {
                patientName = patientName + extendedPatient.getName().get(1).getText();
            }
			/*if(extendedPatient.getName().size() > 0){
				if( "female".equalsIgnoreCase(extendedPatient.getGender().toString())) {
					messageContent.put("Patient_Prefix", "Madam");
				}else if("male".equalsIgnoreCase(extendedPatient.getGender().toString())) {
					messageContent.put("Patient_Prefix", "Sir");
				}else{
					messageContent.put("Patient_Prefix", patientName );
				}

			}*/

            messageContent.put("Patient_Name", patientName);
            messageContent.put("IDNo", extendedPatient.getIdentifier().size() > 1 ? extendedPatient.getIdentifier().get(1).getValue() : "");
            messageContent.put("Doctor_Name", extendedServiceRequest.getPerformer().get(0).getDisplay().toString());
            String appointmentdatetime = extendedAppointment.getStart().toString();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z uuuu").withLocale(Locale.US);
            ZonedDateTime zdt = ZonedDateTime.parse(appointmentdatetime, f);
            LocalDateTime ld = zdt.toLocalDateTime();
            DateTimeFormatter fLocalDate = DateTimeFormatter.ofPattern("dd/MM/uuuu,hh:mm a");
            String output = ld.format(fLocalDate);
            messageContent.put("Date_time", appointmentdatetime);
            messageContent.put("Procedure_Name", extendedServiceRequest.getPlannedProcedure().getDisplay().toString());
            message.put("messageContent", messageContent);
            String email = extendedPatient.getTelecom().size() > 1 ? extendedPatient.getTelecom().get(1).getValue() : "";

            HashMap messageInfo = new HashMap<>();
            String doctorEmail = extendedServiceRequest.getPerformer().get(0).getIdentifier().getType().getText();
            messageInfo.put("to", email);
            messageInfo.put("subject", "Confirmation of Procedure Schedule at TCS");
            messageInfo.put("templateName", "ProcedureRequest");
            messageInfo.put("cc", doctorEmail);

            message.put("message", messageInfo);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<HashMap> htp = new HttpEntity<HashMap>(message);
            String userMailResponse = objectMapper.writeValueAsString(message);
            Application application = eurekaClient.getApplication("notification-service");
            InstanceInfo instanceInfo = application.getInstances().get(0);
            String resourceURL = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/notification/send?type=email";
            restTemplate.postForObject(resourceURL, htp, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

