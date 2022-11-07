/**
 *
 */
package org.poc.bloodbank.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import com.netflix.discovery.shared.Application;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestStatus;
import org.poc.bloodbank.Async.IntegrationChannel;
import org.poc.bloodbank.Async.ProcedureResourceOperations;
import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.entity.model.ExtendedAppointment;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;
import org.poc.bloodbank.enums.ServiceRequestSearchEnum;
import org.poc.bloodbank.exception.AppointmentCreationException;
import org.poc.bloodbank.exception.ServiceRequestCreationException;
import org.poc.bloodbank.property.IntegrationConfigProperties;
import org.poc.bloodbank.property.ServicecConfigProperties;
import org.poc.bloodbank.repository.ExtendedServiceRequestRepository;
import org.poc.bloodbank.rest.model.BaseItem;
import org.poc.bloodbank.rest.model.Instrument;
import org.poc.bloodbank.rest.model.PreferenceKartRequestBody;
import org.poc.bloodbank.rest.model.ServiceRequestFormBody;
import org.poc.bloodbank.util.BloodBankUtils;
import org.poc.core.base.rest.util.HttpUtils;
import org.poc.core.base.util.GeneralFhirUtility;
import org.poc.core.constants.HttpHeaders;
import org.poc.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sankha
 *
 */

@Service
public class ExtendedServiceRequestService extends BaseService<ExtendedServiceRequest> {
    Logger logger = LoggerFactory.getLogger(ExtendedServiceRequestService.class);
    String serviceCode;
    Long startNo;
    ServicecConfigProperties servicecConfigProperties;
    @Autowired
    private EurekaClient eurekaClient;
    @Autowired
    IntegrationConfigProperties integrationConfigProperties;
    @Autowired
    ProcedureResourceOperations procedureResourceOperations;
    @Autowired
    IntegrationChannel integrationChannel;
    @Autowired
    ExtendedAppointmentService extendedAppointmentService;
    @Autowired
    ObjectMapper objectMapper;
    CodeableConcept codeableConcept;

    ExtendedServiceRequestRepository extendedServiceRequestRepository;
    public static final String BUSINESS_STATUS_REQUESTED = "requested";
    public static final String BUSINESS_STATUS_SCHEDULED = "scheduled";
    public boolean isPatientReg;


    @Autowired
    private ExtendedAppointmentService appointmentService;

    @Autowired
    public ExtendedServiceRequestService(ExtendedServiceRequestRepository extendedServiceRequestRepository,
                                         MongoTemplate mongoTemplate) {
        super(extendedServiceRequestRepository, mongoTemplate);
        this.extendedServiceRequestRepository = extendedServiceRequestRepository;
    }

    public ExtendedServiceRequest create(ServiceRequestFormBody serviceRequestFormBody) {

        ExtendedServiceRequest serviceRequest = null;
        if (serviceRequestFormBody.getExtendedServiceRequest() != null
                && !serviceRequestFormBody.getExtendedServiceRequest().isEmpty()) {
            serviceRequest = serviceRequestFormBody.getExtendedServiceRequest();
            serviceRequest.setBusinessStatus(new StringType(BUSINESS_STATUS_REQUESTED));
            serviceRequest.setPriority(ServiceRequest.ServiceRequestPriority.ROUTINE);

        }
        serviceRequest = generateSequenceNumber(serviceRequest);
        // Create location information object and persist if required.
        try {
            if (serviceRequestFormBody.getExtendedPatient() != null
                    && !serviceRequestFormBody.getExtendedPatient().isEmpty()) {
                addDonorDetails(serviceRequest, serviceRequestFormBody.getExtendedPatient());
            }

            if (serviceRequestFormBody.getExtendedAppointment() != null
                    && !serviceRequestFormBody.getExtendedAppointment().isEmpty()) {
                // FIXME: before adding the appointment to service request we need to create the
                // appointment using appointment service.
                try {
                    ExtendedAppointment otAppointment = extendedAppointmentService
                            .create(serviceRequestFormBody.getExtendedAppointment());
                    addAppointmentDetails(serviceRequest, otAppointment);
                } catch (Exception e) {
                    throw new AppointmentCreationException(e.getMessage(), e);
                }
                serviceRequest.setBusinessStatus(new StringType(BUSINESS_STATUS_SCHEDULED));
                // FIXME : Add validation for plannedProcedure code and desc.
                if (serviceRequest.getPlannedProcedure().getCode() != null) {
                    logger.info("Planned Procedure" + serviceRequest.getPlannedProcedure().getCode());
//				String preferenceCardDetails = fetchPreferenceKartDetails(
//						serviceRequest.getPlannedProcedure().getCoding().get(0).getCode());

                    /*
                     * addPreferenceKartDetails(serviceRequest,
                     * fetchPreferenceKartDetails(serviceRequest.getPlannedProcedure().getCode()));
                     */
                    // TODO : construct the rest model of Preference Kart from master fetch
                    // preference Details.
                    // serviceRequest = appendPreferenceKartDetailsToRequest(serviceRequest,
                    // preferenceCardDetails);
                } else {
                    throw new ServiceRequestCreationException("Planned Procedure was null");
                }

                // Multiple Procedures set Primary
                if (serviceRequest.getOtInformationList() != null) {
                    if (serviceRequest.getOtInformationList().size() > 0) {
                        ExtendedServiceRequest.Information primaryInfo = serviceRequest.getOtInformationList().stream()
                                .filter(info -> info.getIsPrimary().booleanValue()).findFirst().orElse(null);
                        if (primaryInfo != null) {
                            if (primaryInfo.hasPlannedProcedure())
                                serviceRequest.setPlannedProcedure(primaryInfo.getPlannedProcedure());
                            if (primaryInfo.getPerformer() != null)
                                serviceRequest.setPerformer(primaryInfo.getPerformer());
                            if (primaryInfo.hasAnesthesiaType())
                                serviceRequest.setAnesthesiaType(primaryInfo.getAnesthesiaType());
                            if (primaryInfo.hasApproach())
                                serviceRequest.setApproach(primaryInfo.getApproach());
                            if (primaryInfo.getCategory() != null)
                                serviceRequest.setCategory(primaryInfo.getCategory());
                            if (primaryInfo.hasDateOfProcedure())
                                serviceRequest.setDateOfProcedure(primaryInfo.getDateOfProcedure());
                            if (primaryInfo.hasDiagnosis())
                                serviceRequest.setDiagnosis(primaryInfo.getDiagnosis());
                            if (primaryInfo.hasEstimatedTherapyTime())
                                serviceRequest.setEstimatedTherapyTime(primaryInfo.getEstimatedTherapyTime());
                            if (primaryInfo.hasLaterality())
                                serviceRequest.setLaterality(primaryInfo.getLaterality());
                            if (primaryInfo.hasLMP())
                                serviceRequest.setLMP(primaryInfo.getLMP());

                        }

                    }
                }

            }
        } catch (Exception e) {
            throw new ServiceRequestCreationException(e.getMessage(), e);
        }
        if (serviceRequest != null) {
            ExtendedServiceRequest sendToMirthAfterSave = super.create(serviceRequest);
            if (isPatientReg != true) {
                integrationChannel.sendExtendedServiceRequest(sendToMirthAfterSave);
            }
            integrationChannel.sendEmailNotificationToRegPatient(serviceRequestFormBody.getExtendedServiceRequest(), serviceRequestFormBody.getExtendedPatient(), serviceRequestFormBody.getExtendedAppointment());
            return sendToMirthAfterSave;
        } else {
            throw new ServiceRequestCreationException("Service Request Object was null");
        }
    }

    private ExtendedServiceRequest generateSequenceNumber(ExtendedServiceRequest serviceRequest) {
        Long oldSequenceNumber;
        String unitPrefix = "";
        if (HttpUtils.getHeader(HttpHeaders.UNIT_CODE) != null) {
            unitPrefix = HttpUtils.getHeader(HttpHeaders.UNIT_CODE);
            logger.info("unitCode: " + unitPrefix);
        }
//		} else if (serviceRequest.getUnitCode().toString() != null
//				&& !(serviceRequest.getUnitCode().toString().isEmpty())) {
//			unitPrefix = serviceRequest.getUnitCode().toString().replace("\"", "");
//		}
        // Put Rule Engine call later with prefixed
        if (StringUtils.isNotBlank(unitPrefix)) {
            runAutoGenCodeRule(unitPrefix);
        } else {
            throw new ServiceRequestCreationException("Unit Code was null");
        }
        if (extendedServiceRequestRepository.findLast() != null) {
            oldSequenceNumber = extendedServiceRequestRepository.findLast().getSequenceNumber().getValueAsNumber()
                    .longValue();
            serviceRequest.setSequenceNumber(new DecimalType(oldSequenceNumber + 1));
        } else {
            oldSequenceNumber = 1L;
            serviceRequest.setSequenceNumber(new DecimalType(1L));
        }

        Long oldUnitSpecificNumber = this.startNo;

        if (extendedServiceRequestRepository.findLastForPrefix(unitPrefix) != null) {
            ExtendedServiceRequest lastUnitPrefixServiceRequest = extendedServiceRequestRepository
                    .findLastForPrefix(unitPrefix);
            oldUnitSpecificNumber = Long.valueOf(lastUnitPrefixServiceRequest.getIdentifier().stream()
                    .filter(identifier -> identifier.getUse().equals(IdentifierUse.USUAL)).collect(Collectors.toList())
                    .get(0).getValue()) + 1L;
            logger.info("oldUnitSpecificNumber from DB :" + oldUnitSpecificNumber);
        }
        Identifier unitPrefixIdentifer = new Identifier();
        unitPrefixIdentifer.setUse(IdentifierUse.USUAL);
        unitPrefixIdentifer.setType(new CodeableConcept().setText(unitPrefix));
        unitPrefixIdentifer.setValue(oldUnitSpecificNumber.toString());
        serviceRequest.getIdentifier().add(unitPrefixIdentifer);
        logger.info("current unit specific number :" + unitPrefixIdentifer.getValue());
        return serviceRequest;
    }

    // Clinic care request object
    public ExtendedServiceRequest createServiceRequest(ExtendedServiceRequest extendedServiceRequest) {
        logger.info("Clinic care request Object");
        if (extendedServiceRequest != null) {
            extendedServiceRequest = generateSequenceNumber(extendedServiceRequest);
            try {
                if (extendedServiceRequest.getSubject().getReference() != null
                        && !extendedServiceRequest.getSubject().getReference().isEmpty()) {
//					extendedServiceRequest = fetchPatientDetails(extendedServiceRequest);
                    addDonorDetails(extendedServiceRequest, fetchPatientDetails(
                            GeneralFhirUtility.trimResourceId(extendedServiceRequest.getSubject().getReference())));
                    extendedServiceRequest.setBusinessStatus(new StringType(BUSINESS_STATUS_REQUESTED));
                }
            } catch (Exception e) {
                throw new ServiceRequestCreationException(e.getMessage(), e);
            }
        } else
            throw new ServiceRequestCreationException("Extended Service Request was null");
        return super.create(extendedServiceRequest);
    }

    /**
     * fetching patient details from OPD-Service using Patient ID
     *
     * @param patientId
     * @return
     */
    private Donor fetchPatientDetails(String patientId) {

        logger.info("Fetch Patient Details Id :" + patientId);
        RestTemplate restTemplate = new RestTemplate();
        Application application = eurekaClient.getApplication(integrationConfigProperties.getOpdServiceID());
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String opdUrl = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/extendedPatient/"
                + patientId;
        logger.info("Fetch Patient Details opdUrl :" + opdUrl);
        String patientObject = restTemplate.getForObject(opdUrl, String.class);
        logger.info("PatientObjectString :" + patientObject);
        JsonNode patientJson;
        Donor donorObject = null;
        try {
            patientJson = objectMapper.readTree(patientObject);
            donorObject = objectMapper.treeToValue(patientJson, Donor.class);
            logger.info("ExtendedPatientObj :" + donorObject);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // convertor(restTemplate);
        logger.info("extendedPatientstr :" + donorObject.getDonorno().toString());
        return donorObject;

    }

    /*
     * private void convertor(RestTemplate restTemplate) {
     *
     *
     * List<HttpMessageConverter<?>> messageConverters = new
     * ArrayList<HttpMessageConverter<?>>(); //Add the Jackson Message converter
     * MappingJackson2HttpMessageConverter converter = new
     * MappingJackson2HttpMessageConverter();
     *
     * // Note: here we are making this converter to process any kind of response,
     * // not only application/*json, which is the default behaviour
     * converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
     * messageConverters.add(converter);
     * restTemplate.setMessageConverters(messageConverters); }
     */
    private void runAutoGenCodeRule(String unit) {
        try {
            Application application = eurekaClient.getApplication(integrationConfigProperties.getRuleEngineServiceID());
            InstanceInfo instanceInfo = application.getInstances().get(0);
            // FIXME : This code will not work if cross server microservices are deployed.
            // Then we need to change the url call using zuuldev/zuulqa
            String ruleUrl = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/rule/runRule";
            logger.info("ruleUrl " + ruleUrl);
            RestTemplate restTemplate = new RestTemplate();
            JsonObject requestBody = new JsonObject();
            requestBody.add("rules", new JsonArray());
            JsonObject payloadObject = new JsonObject();
            payloadObject.addProperty("type", "SERVICEREQUEST");
            payloadObject.addProperty("unit", unit);
            requestBody.add("payload", payloadObject);
            String response = restTemplate.postForObject(ruleUrl, requestBody.toString(), String.class).replaceAll("\"",
                    "");
            logger.info("Rule Engine Response " + response.toString());
            JsonObject responseObject = new JsonParser().parse(response).getAsJsonArray().get(0).getAsJsonObject();
            this.serviceCode = responseObject.get("code") != null ? responseObject.get("code").getAsString()
                    : servicecConfigProperties.getAutogenaratorCode();
            this.startNo = responseObject.get("startingNo") != null
                    ? Long.valueOf(responseObject.get("startingNo").getAsString())
                    : Long.valueOf(servicecConfigProperties.getAutogenaratorStartno());
        } catch (Exception e) {
            //	throw new RuleEngineException("Rule Engine not accesible : " + e.getMessage());
            this.startNo = 1001L;
            this.serviceCode = "PU";
        }
    }

    /**
     * list of service request based request object
     *
     * @param serviceRequestObject
     * @return
     */
    public List<ExtendedServiceRequest> findServiceRequestBySearchObject(JsonObject serviceRequestObject) {
        List<ExtendedServiceRequest> requestList = null;
        if (serviceRequestObject != null) {
            if (StringUtils.isNotBlank(HttpUtils.getHeader(HttpHeaders.UNIT_CODE)))
                serviceRequestObject.addProperty(ServiceRequestSearchEnum.UNIT_CODE.getValue(),
                        HttpUtils.getHeader(HttpHeaders.UNIT_CODE));

            requestList = extendedServiceRequestRepository.findByQuery(serviceRequestObject);
        }
        return requestList;

    }

    public long findServiceRequestCountBySearchObject(JsonObject serviceRequestObject) {
        if (serviceRequestObject != null) {
            if (StringUtils.isNotBlank(HttpUtils.getHeader(HttpHeaders.UNIT_CODE)))
                serviceRequestObject.addProperty(ServiceRequestSearchEnum.UNIT_CODE.getValue(),
                        HttpUtils.getHeader(HttpHeaders.UNIT_CODE));

            return extendedServiceRequestRepository.findCountByQuery(serviceRequestObject);
        }
        return 0;
    }

    /**
     * add patient reference details
     *
     * @param serviceRequest
     * @param donor
     */
    private void addDonorDetails(ExtendedServiceRequest serviceRequest, Donor donor) {
        Reference donorRef = new Reference();
        Identifier donorIdentifer = new Identifier();
        donorIdentifer.setUse(IdentifierUse.USUAL);
        donorIdentifer.setValue(donor.getDonorno().asStringValue());
        donorRef.setReference(donor.getId());
        donorRef.setIdentifier(donorIdentifer);
        donorRef.setId(donor.getId());
        JsonObject donorJsonObject = new JsonObject();
        donorJsonObject.addProperty("name",
                donor.getName().size() > 0 ? donor.getName().get(0).getText()
                        : donor.getFullName().toString());
        donorJsonObject.addProperty("otherName",
                donor.getName().size() > 0 ? donor.getName().get(1).getText()
                        : donor.getFullName().toString());
        donorJsonObject.addProperty("gender", donor.getGender().getDisplay());
        donorJsonObject.addProperty("donorNo", donor.getDonorno().asStringValue());
        donorJsonObject.addProperty("title",
                donor.getName().size() > 0 ? donor.getName().get(0).getPrefix().get(0).getValue()
                        : donor.getFullName().toString());
        donorJsonObject.addProperty("dob",
                donor.getBirthDate() != null
                        ? BloodBankUtils.convertDateToString(donor.getBirthDate()) + ""
                        : "");
        donorJsonObject.addProperty("phone",
                donor.getTelecom().size() > 0 ? donor.getTelecom().get(0).getValue() : "");
        donorJsonObject.addProperty("email",
                donor.getTelecom().size() > 1 ? donor.getTelecom().get(1).getValue() : "");
        donorJsonObject.addProperty("identificationType",
                donor.getIdentifier().size() > 1 ? donor.getIdentifier().get(1).getType().getText()
                        : "");
        donorJsonObject.addProperty("identificationId",
                donor.getIdentifier().size() > 1 ? donor.getIdentifier().get(1).getValue() : "");
        donorJsonObject.addProperty("donorCategory",
                donor.getDonorCategory() != null ? donor.getDonorCategory().asStringValue()
                        : "");

        donorRef.setDisplay(donorJsonObject.toString());
        isPatientReg = true;

        serviceRequest.setSubject(donorRef);
    }

    // add appointment reference details
    private void addAppointmentDetails(ExtendedServiceRequest serviceRequest, ExtendedAppointment extendedAppointment)
            throws JsonProcessingException {
        Reference appointmentRef = new Reference();
        Identifier appointmentIdentifer = new Identifier();
        String extendedAppointmentStr = objectMapper.writeValueAsString(extendedAppointment);
        com.google.gson.JsonParser gsonParser = new com.google.gson.JsonParser();
        JsonObject jsonObjectExtendedAppointment = gsonParser.parse(extendedAppointmentStr).getAsJsonObject();
        jsonObjectExtendedAppointment.remove("resourceType");
        jsonObjectExtendedAppointment.remove("meta");
        appointmentIdentifer.setUse(IdentifierUse.USUAL);
        // patientRef.setIdentifier(patientIdentifer);
        appointmentRef.setId(jsonObjectExtendedAppointment.get("id").getAsString());
        appointmentRef.setDisplay(jsonObjectExtendedAppointment.toString());
        DecimalType slotTime = new DecimalType();
        slotTime.setValue(BloodBankUtils.toEpochTime(extendedAppointment.getStart().getTime()));

        serviceRequest.setSlotStartTime(slotTime);

        serviceRequest.setAppointmentRefList(appointmentRef);
    }

    public List<ExtendedServiceRequest> createServiceRequestList(List<ServiceRequestFormBody> serviceRequestFormBody) {
        List<ExtendedServiceRequest> extendedServiceRequestList = new ArrayList<>();
        serviceRequestFormBody.forEach(serviceRequest -> {
            try {
                extendedServiceRequestList.add(create(serviceRequest));
            } catch (Exception e) {
                throw new ServiceRequestCreationException(e.getMessage(), e);
            }
        });
        return extendedServiceRequestList;
    }

    /* update service request based on Id */
    public ExtendedServiceRequest patchServiceRequest(String id, JsonObject patchObject) {
        ExtendedServiceRequest objectTopatch = findById(id);

        if (!objectTopatch.isEmpty()) {

            /* Append Appointment details to ExtendedServiceRequest */
            try {
                if (patchObject.get("resourceType") != null
                        && patchObject.get("resourceType").getAsString().equalsIgnoreCase("extendedAppointment")) {
                    ExtendedAppointment appointment = objectMapper.readValue(patchObject.toString(),
                            ExtendedAppointment.class);
                    addAppointmentDetails(objectTopatch, appointment);
                    objectTopatch.setBusinessStatus(new StringType(BUSINESS_STATUS_SCHEDULED));

                } else if (patchObject.get("status") != null) {
                    switch (patchObject.get("status").getAsString().toLowerCase()) {
                        case "cancelled":
                            objectTopatch.setStatus(ServiceRequestStatus.UNKNOWN);
                            objectTopatch.setBusinessStatus(new StringType(patchObject.get("status").getAsString().toLowerCase()));
                            if (objectTopatch.getAppointmentRefList().getId() != null) {
                                String appointmentId = objectTopatch.getAppointmentRefList().getId();
                                logger.info("appointmentId :" + appointmentId);
                                if (StringUtils.isNoneBlank(appointmentId)) {
                                    ExtendedAppointment extAppointment = appointmentService
                                            .findByAppointmentId(appointmentId);
                                    if (!extAppointment.isEmpty()) {
                                        // update status for appointment object
                                        extAppointment.setStatus(org.hl7.fhir.dstu3.model.Appointment.AppointmentStatus.CANCELLED);
                                        appointmentService.update(extAppointment, appointmentId);
                                    }
                                }
                            }

                            break;
                        case "active":
                            objectTopatch.setStatus(ServiceRequestStatus.ACTIVE);
                            break;
                        case "completed":
                            objectTopatch.setStatus(ServiceRequestStatus.COMPLETED);
                            break;
                        case "suspended":
                            objectTopatch.setStatus(ServiceRequestStatus.DRAFT);
                            break;
                        // TODO add more switch cases
                        default:
                            break;
                    }
                    /* update status */
                }
                if (patchObject.get("businessStatus") != null) {
                    objectTopatch.setBusinessStatus(
                            new StringType(patchObject.get("businessStatus").getAsString().toLowerCase()));
                }
                if (patchObject.get("encounter") != null) {
                    addEncounterDetails(objectTopatch, patchObject.get("encounter").getAsJsonObject());
                    objectTopatch.setBusinessStatus(
                            new StringType(patchObject.get("businessStatus").getAsString().toLowerCase()));
                    integrationChannel.sendExtendedServiceRequest(objectTopatch);

                }
                if (patchObject.get("checkedInDate") != null) {
                    objectTopatch.setCheckedInDate(new DecimalType(patchObject.get("checkedInDate").getAsLong()));
                    objectTopatch.setBusinessStatus(
                            new StringType(patchObject.get("businessStatus").getAsString().toLowerCase()));
                    procedureResourceOperations.createProcedureAsyncFromServiceRequest(objectTopatch);

                }
                if (patchObject.get("preferenceKartRequest") != null
                        && !patchObject.get("preferenceKartRequest").isJsonNull()) {
                    PreferenceKartRequestBody preferenceKart = new Gson()
                            .fromJson(patchObject.get("preferenceKartRequest"), PreferenceKartRequestBody.class);
                    logger.info("patch Request processing: " + preferenceKart.toString());
                    addPreferenceKartDetails(objectTopatch, preferenceKart);
                }
            } catch (Exception e) {
                throw new ServiceRequestCreationException(e.getMessage(), e);
            }
        } else {
            throw new ServiceRequestCreationException("Service Request Object was null");
        }
        logger.info(
                "Service Request Status to Updated " + objectTopatch.getId() + " status: " + objectTopatch.getStatus());
        return update(objectTopatch, id);
    }

    /*
        @Override
        public ExtendedServiceRequest update(ExtendedServiceRequest objectTopatch, String id) {
            if (objectTopatch != null) {
                ExtendedServiceRequest sendToMirthAfterSave = super.update(objectTopatch,id);
                //integrationChannel.sendExtendedServiceRequest(sendToMirthAfterSave);
                return sendToMirthAfterSave;
            } else {
                throw new ServiceRequestCreationException("Service Request Object was null");
            }
        }*/
    /*
     * public ExtendedServiceRequest updateServiceRequest(String id,
     * ExtendedAppointment extendedAppointment) throws JsonProcessingException {
     * ExtendedServiceRequest objectTopatch = findById(id);
     * if(!objectTopatch.isEmpty()) {
     * addAppointmentDetails(objectTopatch,extendedAppointment); } else { throw new
     * ServiceRequestCreationException("Service Request Object was null"); }
     *
     * return update(objectTopatch, id);
     *
     * }
     */
    public boolean rescheduleServiceRequestById(String serviceRequestId, ExtendedAppointment newAppointment, String oldAppointmentId) throws AppointmentCreationException {
        ExtendedServiceRequest serviceRequestObject = this.findById(serviceRequestId);
        ExtendedAppointment newAppointmentObject = appointmentService.rescheduleAppointment(newAppointment, oldAppointmentId);
        try {
            addAppointmentDetails(serviceRequestObject, newAppointmentObject);
            return this.update(serviceRequestObject, serviceRequestId).hasId();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            throw new AppointmentCreationException(e.getMessage(), e);
        }
    }

    private void addEncounterDetails(ExtendedServiceRequest serviceRequest, JsonObject encounterPatchObject) {
        Reference encounterRef = new Reference();
        String encounterId = encounterPatchObject.get("reference").toString().split("/")[1].trim().replace("\"", "");
        Identifier encounterIdentifer = new Identifier();
        encounterIdentifer.setUse(IdentifierUse.OFFICIAL);
        encounterIdentifer.setValue(encounterId);
        encounterRef.setIdentifier(encounterIdentifer);
        encounterRef.setDisplay(encounterPatchObject.get("reference").toString());
//        serviceRequest.setContext(encounterRef);
    }

    private PreferenceKartRequestBody fetchPreferenceKartDetails(String plannedProcedureId) {

        RestTemplate restTemplate = new RestTemplate();

        Application application = eurekaClient.getApplication(integrationConfigProperties.getMastersServiceID());
        InstanceInfo instanceInfo = application.getInstances().get(0);
        // FIXME: For Corss Server microservice deployment this url will not work.
        String masterUrl = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort()
                + "/Masters/PreferenceKartMaster/query?field=procedure&value=" + plannedProcedureId;

        /*
         * String masterUrl =
         * "http://192.168.120.14:9008/Masters/PreferenceKartMaster/query?field=procedure&value="
         * + plannedProcedureId;
         */
        String preferenceKartMaster = restTemplate.getForObject(masterUrl, String.class);
        try {
            JsonNode preferenceKartJsonObject = objectMapper.readTree(preferenceKartMaster);
            logger.info("preferenceKartMaster :" + preferenceKartMaster.toString());

            // FIXME: Trying luck for binding the rest template into jsonObject response
            // JsonArray preferenceKartJsonObject = restTemplate.getForObject(masterUrl,
            // JsonArray.class);
            if (preferenceKartJsonObject != null) {
                // TODO set the data into PreferenceKartRequestForm Body object.
                // JsonNode preferenceKartDetails =
                // preferenceKartJsonObject.get("preferenceKartDetails");
                // JsonNode preferenceKartDetails = preferenceKartJsonObject.get("");
                PreferenceKartRequestBody preferenceKartRequestObject = new PreferenceKartRequestBody();
                List<BaseItem> equipmentList = new ArrayList<>();
                List<BaseItem> implantList = new ArrayList<>();
                List<BaseItem> bladeOrSuturesList = new ArrayList<>();
                List<Instrument> instrumentSetList = new ArrayList<>();
                List<BaseItem> consumablesList = new ArrayList<>();
                List<org.poc.bloodbank.rest.model.Service> serviceList = new ArrayList<>();
                for (JsonNode eachDetails : preferenceKartJsonObject) {
                    // if (eachDetails instanceof JsonObject) {
                    JsonNode preferenceKartDetailsMasterObj = eachDetails.get("preferenceKartDetailsMaster");
                    BaseItem baseItem = new BaseItem();
                    Instrument instrument = new Instrument();
                    baseItem.setCode(preferenceKartDetailsMasterObj.get("code").toString());
                    baseItem.setDesc(preferenceKartDetailsMasterObj.get("desc").toString());
                    baseItem.setQuantity(preferenceKartDetailsMasterObj.get("quantity").asInt());
                    switch (preferenceKartDetailsMasterObj.get("cartType").toString().toLowerCase().replace("\"", "")) {
                        case "equipment":
                            equipmentList.add(baseItem);
                            // TODO: canbe put to common place. based on specific switch case data can be
                            // added.
                            break;
                        case "implants":
                            implantList.add(baseItem);

                            break;
                        case "bladesOrSutures":
                            bladeOrSuturesList.add(baseItem);

                            break;
                        case "instrument":
                            instrumentSetList.add(instrument);

                            break;
                        case "consumables":
                            consumablesList.add(baseItem);

                            break;
                        case "service":
                            org.poc.bloodbank.rest.model.Service serviceItem = new org.poc.bloodbank.rest.model.Service();
                            serviceItem.setCode(preferenceKartDetailsMasterObj.get("code").toString());
                            serviceItem.setDesc(preferenceKartDetailsMasterObj.get("desc").toString());
                            serviceItem.setQuantity(preferenceKartDetailsMasterObj.get("quantity").asInt());
                            serviceItem.setGroupCode(preferenceKartDetailsMasterObj.get("groupCode").toString());
                            serviceItem.setGroupDesc(preferenceKartDetailsMasterObj.get("groupDesc").toString());
                            serviceList.add(serviceItem);
                            break;

                    }
                }
                preferenceKartRequestObject.setEquipments(equipmentList);
                preferenceKartRequestObject.setImplants(implantList);
                preferenceKartRequestObject.setBladesAndSutures(bladeOrSuturesList);
                preferenceKartRequestObject.setConsumables(consumablesList);
                preferenceKartRequestObject.setInstrumentSet(instrumentSetList);
                preferenceKartRequestObject.setServices(serviceList);
                return preferenceKartRequestObject;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void addPreferenceKartDetails(ExtendedServiceRequest extendedServiceRequest,
                                          PreferenceKartRequestBody preferenceKartRequestBody) {
        try {
            if (preferenceKartRequestBody != null) {
                if (preferenceKartRequestBody.getBladesAndSutures() != null
                        && preferenceKartRequestBody.getBladesAndSutures().size() > 0) {
                    List<ExtendedServiceRequest.PreferenceCardItems> bladeAndSutureList = new ArrayList<>();
                    for (BaseItem blades : preferenceKartRequestBody.getBladesAndSutures()) {
                        ExtendedServiceRequest.PreferenceCardItems BladeAndSuture = new ExtendedServiceRequest.PreferenceCardItems();
                        BladeAndSuture.setCode(new StringType(blades.getCode()));
                        BladeAndSuture.setDesc(new StringType(blades.getDesc()));
                        BladeAndSuture.setQuantity(new IntegerType(blades.getQuantity()));
                        bladeAndSutureList.add(BladeAndSuture);
                    }
                    extendedServiceRequest.setBladesOrSuturesList(bladeAndSutureList);

                }
                if (preferenceKartRequestBody.getInstrumentSet() != null
                        && preferenceKartRequestBody.getInstrumentSet().size() > 0) {
                    List<ExtendedServiceRequest.PreferenceCardItems> instrumentList = new ArrayList<>();
                    for (Instrument instrument : preferenceKartRequestBody.getInstrumentSet()) {
                        ExtendedServiceRequest.PreferenceCardItems InstrumentItem = new ExtendedServiceRequest.PreferenceCardItems();
                        InstrumentItem.setCode(new StringType(instrument.getCode()));
                        InstrumentItem.setDesc(new StringType(instrument.getDesc()));
                        InstrumentItem.setQuantity(new IntegerType(instrument.getQuantity()));
                        InstrumentItem.setPriority(new StringType(instrument.getPriority()));
                        instrumentList.add(InstrumentItem);
                    }
                    extendedServiceRequest.setInstrumentSetsList(instrumentList);
                }
                if (preferenceKartRequestBody.getEquipments() != null
                        && preferenceKartRequestBody.getEquipments().size() > 0) {
                    List<ExtendedServiceRequest.PreferenceCardItems> equipmentList = new ArrayList<>();
                    for (BaseItem equipment : preferenceKartRequestBody.getEquipments()) {
                        ExtendedServiceRequest.PreferenceCardItems equipmentItem = new ExtendedServiceRequest.PreferenceCardItems();
                        equipmentItem.setCode(new StringType(equipment.getCode()));
                        // equipmentItem.setDesc(new StringType(equipment.getDesc()));
                        // equipmentItem.setQuantity(new IntegerType(equipment.getQuantity()));
                        equipmentList.add(equipmentItem);
                    }
                    extendedServiceRequest.setEquipmentsList(equipmentList);
                }
                if (preferenceKartRequestBody.getConsumables() != null
                        && preferenceKartRequestBody.getConsumables().size() > 0) {
                    List<ExtendedServiceRequest.PreferenceCardItems> consumableList = new ArrayList<>();
                    for (BaseItem consumable : preferenceKartRequestBody.getConsumables()) {
                        ExtendedServiceRequest.PreferenceCardItems consumableItem = new ExtendedServiceRequest.PreferenceCardItems();
                        consumableItem.setCode(new StringType(consumable.getCode()));
                        consumableItem.setDesc(new StringType(consumable.getDesc()));
                        consumableItem.setQuantity(new IntegerType(consumable.getQuantity()));
                        consumableList.add(consumableItem);
                    }
                    extendedServiceRequest.setConsumablesList(consumableList);
                }
                if (preferenceKartRequestBody.getImplants() != null
                        && preferenceKartRequestBody.getImplants().size() > 0) {
                    List<ExtendedServiceRequest.PreferenceCardItems> implantList = new ArrayList<>();
                    for (BaseItem implants : preferenceKartRequestBody.getImplants()) {
                        ExtendedServiceRequest.PreferenceCardItems implant = new ExtendedServiceRequest.PreferenceCardItems();
                        implant.setCode(new StringType(implants.getCode()));
                        implant.setDesc(new StringType(implants.getDesc()));
                        implant.setQuantity(new IntegerType(implants.getQuantity()));
                        implantList.add(implant);
                    }
                    extendedServiceRequest.setImplantsList(implantList);
                }
                if (preferenceKartRequestBody.getServices() != null
                        && preferenceKartRequestBody.getServices().size() > 0) {
                    List<ExtendedServiceRequest.PreferenceCardItems> serviceList = new ArrayList<>();
                    for (org.poc.bloodbank.rest.model.Service service : preferenceKartRequestBody.getServices()) {
                        ExtendedServiceRequest.PreferenceCardItems serviceItem = new ExtendedServiceRequest.PreferenceCardItems();
                        serviceItem.setCode(new StringType(service.getCode()));
                        serviceItem.setDesc(new StringType(service.getDesc()));
                        serviceItem.setQuantity(new IntegerType(service.getQuantity()));
                        serviceItem.setGroup(new StringType(service.getGroupDesc()));
                        serviceItem.setGroupName(new StringType(service.getGroupDesc()));
                        serviceList.add(serviceItem);
                    }
                    extendedServiceRequest.setServicesList(serviceList);
                }
                if (preferenceKartRequestBody.getPreparationAndPosition() != null) {
                    // TODO: Put validation check for if the preparation and position exists or not.
                    String preparation = preferenceKartRequestBody.getPreparationAndPosition().get("preparation")
                            .toString();
                    String position = preferenceKartRequestBody.getPreparationAndPosition().get("position").toString();

                    CodeableConcept preparationAndPosition = GeneralFhirUtility.createR4CodeableConcept(preparation,
                            position, position, "");
                    extendedServiceRequest.setPreparationAndPosition(preparationAndPosition);

                }
            }
        } catch (Exception e) {
            throw new ServiceRequestCreationException(
                    "PreferenceKart Details Request is not correct : " + e.getMessage(), e);
        }
    }

}
