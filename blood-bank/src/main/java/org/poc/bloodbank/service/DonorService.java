package org.poc.bloodbank.service;

import com.google.gson.*;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.StringType;
import org.poc.bloodbank.Async.IntegrationChannel;
import org.poc.bloodbank.Async.ParallelExecution;
import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.enums.DonorSearchEnum;
import org.poc.bloodbank.exception.DonorNoGenerationException;
import org.poc.bloodbank.exception.DuplicateException;
import org.poc.bloodbank.exception.RuleEngineException;
import org.poc.bloodbank.property.DonorConfigProperties;
import org.poc.bloodbank.property.IntegrationConfigProperties;
import org.poc.bloodbank.repository.DonorRepository;
import org.poc.core.base.rest.util.HttpUtils;
import org.poc.core.constants.HttpHeaders;
import org.poc.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Sankha
 */
@Service
public class DonorService extends BaseService<Donor> {
    Logger logger = LoggerFactory.getLogger(DonorService.class);
    JsonObject notification = new JsonObject();
    String donorCode, startNo;
    IntegrationChannel asyncClass;
    ParallelExecution parallelClass;
    DonorRepository donorRepository;
    DonorConfigProperties donorConfigProperties;
    IntegrationConfigProperties integrationConfigProperties;
    @Autowired
    private EurekaClient eurekaClient;

    DonorVisitService donorVisitService;

    @Autowired
    public DonorService(DonorRepository donorRepository,
                        DonorConfigProperties patientConfigProperties, MongoTemplate mongoTemplate, IntegrationChannel asyncClass,
                        IntegrationConfigProperties integrationProperties, ParallelExecution parallelClass,
                        DonorVisitService donorVisitService) {
        super(donorRepository, mongoTemplate);
        this.donorRepository = donorRepository;
        this.donorConfigProperties = patientConfigProperties;
        this.asyncClass = asyncClass;
        this.parallelClass = parallelClass;
        this.donorVisitService = donorVisitService;
        this.parallelClass.setDonorVisitService(donorVisitService);
        this.asyncClass.setIntegrationConfigProperties(integrationProperties);
        this.integrationConfigProperties = integrationProperties;
    }

    public List<Donor> findByPage(int skip, int limit) {

        return donorRepository.findAll(skip, limit);
    }

    public List<Donor> search(JsonObject jObject) {

        return donorRepository.findByQuery(jObject);
    }

    public Long getCountBySearch(JsonObject jObject) {

        return donorRepository.findCountByQuery(jObject);
    }

    @Override
    public Donor update(Donor objectTopatch, String id) {
        Donor extendedPatientFound = donorRepository.findById(id).get();
        if (objectTopatch.getDonorAutoSequenceNo() == null || objectTopatch.getDonorAutoSequenceNo().isEmpty()) {
            objectTopatch.setDonorAutoSequenceNo(extendedPatientFound.getDonorAutoSequenceNo());
        }
//        if (objectTopatch.getCreatedBy() == null || objectTopatch.getCreatedBy().isEmpty()) {
//            objectTopatch.setCreatedBy(extendedPatientFound.getCreatedBy());
//        }
//        if (objectTopatch.getUnitCode() == null || objectTopatch.getUnitCode().isEmpty()) {
//            objectTopatch.setUnitCode(extendedPatientFound.getUnitCode());
//        }
//        if (objectTopatch.getOrgCode() == null || objectTopatch.getOrgCode().isEmpty()) {
//            objectTopatch.setOrgCode(extendedPatientFound.getOrgCode());
//        }
//        if (objectTopatch.getCreatedDate() == null || objectTopatch.getCreatedDate().isEmpty()) {
//            objectTopatch.setCreatedDate(extendedPatientFound.getCreatedDate());
//        }
        if (objectTopatch.getIdentifier().get(0) != null
                && objectTopatch.getIdentifier().get(0).getUse().name().equalsIgnoreCase("usual")
                && !objectTopatch.getIdentifier().get(0).getType().getCoding().isEmpty()
                && objectTopatch.getIdentifier().get(0).getType().getCoding().get(0).getCode().equals("MR")) {
            objectTopatch.getIdentifier().get(0).setType(extendedPatientFound.getIdentifier().get(0).getType());
            objectTopatch.getIdentifier().get(0).setValue(extendedPatientFound.getIdentifier().get(0).getValue());

        }
        Donor donorResponse;
        synchronized (objectTopatch) {
            donorResponse = super.update(objectTopatch, id);
        }
        try {
//			asyncClass.sendRegistrationToMirth(extendedPatientRes);
            asyncClass.sendResourceToElastic(donorResponse, true);
            // FIXME: Update the encounters which has their display objects.
            parallelClass.updateEncounterDetails(donorResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donorResponse;
    }

    @Override
    public Donor create(Donor object) {
        String unitCode = HttpUtils.getHeader(HttpHeaders.UNIT_CODE);
        if (StringUtils.isNotBlank(unitCode))
            runAutoGenCodeRule(unitCode);
//        else
//            runAutoGenCodeRule(object.getUnitCode().getValueAsString());

        Donor lastDonor = donorRepository.findLast();
        Long lastDonorAutoSeqNo;
        if (checkLastDonorAutoSeq(lastDonor)) {
            lastDonorAutoSeqNo = Long.parseLong(lastDonor.getDonorAutoSequenceNo().asStringValue()) + 1;
        } else {
            lastDonorAutoSeqNo = 1L;
        }
        // FIXME add the check of Request body for Identifier.0.use.type.0.code for MR
        // is coming or not
        Donor lastUnitDonor = donorRepository.findLastForUnit(this.donorCode);
        Long lastNo;
        if (checkLastDonorNo(lastUnitDonor)) {
            lastNo = Long.parseLong(getDonorNo(lastUnitDonor).replaceAll("[^0-9?!\\.]", "")) + 1;
        } else {
            lastNo = Long.parseLong(this.startNo);
        }
        object.setDonorAutoSequenceNo(new DecimalType(lastDonorAutoSeqNo.toString()));
        object.setRegistrationDate(new DecimalType(org.poc.core.base.util.GeneralFhirUtility.getCurrentEpochTime().toString()));
        Identifier identifier = object.getIdentifier().stream().filter(identity -> identity.getUseElement()
                .getValueAsString().equals(IdentifierUse.USUAL.getDisplay().toLowerCase())).findFirst().orElse(null);
        if (identifier != null) {
            CodeableConcept cc = new CodeableConcept();
            cc.setText(this.donorCode); // call rule engine to get prefix
            identifier.setType(cc);
            identifier.setValue("" + lastNo);
        } else {
            throw new DonorNoGenerationException(
                    "Donor don't contains Proper Identifier : [" + identifier.getType().getText() + "]");
        }

        object.setDonorno(new StringType(this.donorCode + lastNo));

        Donor donor;

        // Restrict user from adding duplicate patient for Primary Identifier
        List<Identifier> officialIdentifierList = object.getIdentifier().stream().filter(identity -> identity
                        .getUseElement().getValueAsString().equals(IdentifierUse.OFFICIAL.getDisplay().toLowerCase()))
                .collect(Collectors.toList());
        if (!officialIdentifierList.isEmpty()) {
            Identifier offIdentifier = officialIdentifierList.get(0);
            if (checkIdentifierData(offIdentifier) && isDonorAlreadyRegistered(offIdentifier)) {
                String donorNo = !notification.isJsonNull() ? notification.get("donor").getAsString() : "";
                throw new DuplicateException("Donor: " + donorNo + " Donor already registered. For ["
                        + offIdentifier.getType().getText() + "] Type, Value [" + offIdentifier.getValue() + "]");
            }
        }
        // Restrict user from adding duplicate patient for Secondary Identifier
        List<Identifier> secondaryIdentifierList = object.getIdentifier().stream().filter(identity -> identity
                        .getUseElement().getValueAsString().equals(IdentifierUse.SECONDARY.getDisplay().toLowerCase()))
                .collect(Collectors.toList());
        if (!secondaryIdentifierList.isEmpty()) {
            Identifier secondaryIdentifier = secondaryIdentifierList.get(0);
            if (checkIdentifierData(secondaryIdentifier) && isDonorAlreadyRegistered(secondaryIdentifier)) {
                String donorNo = !notification.isJsonNull() ? notification.get("donor").getAsString() : "";

                throw new DuplicateException(
                        "Donor: " + donorNo + " Donor already registered. For [" + secondaryIdentifier.getType().getText()
                                + "] Type, Value [" + secondaryIdentifier.getValue() + "]");
            }
        }
        synchronized (object) {
            donor = super.create(object);
        }
        try {

//			asyncClass.sendRegistrationToMirth(donor);
            asyncClass.sendResourceToElastic(donor, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donor;

    }

    private boolean checkIdentifierData(Identifier identifier) {
        return StringUtils.isNoneEmpty(identifier.getType().getText(), identifier.getValue());
    }

    /**
     * Checks if patient is already registered.
     *
     * @param identifier an {@link Identifier} object.
     * @return a boolean value.
     */
    private boolean isDonorAlreadyRegistered(final Identifier identifier) {
        if (identifier.getValue() != null) {
            final JsonObject identityValueJSONObject = new JsonObject();

            JsonObject typeObj = new JsonObject();
            typeObj.addProperty(DonorSearchEnum.TYPE.getValue(), identifier.getType().getText());
            typeObj.addProperty(DonorSearchEnum.VALUE.getValue(), identifier.getValue());
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(typeObj.toString(), JsonElement.class);
            identityValueJSONObject.add(DonorSearchEnum.IDENTIFICATION.getValue(), typeObj);

            List<Donor> donorList = search(identityValueJSONObject);
            if (!donorList.isEmpty()) {
                notification.addProperty("error", "Duplicate Donor");
                notification.addProperty("donor", donorList.get(0).getDonorno().asStringValue());
                // FIXME : Use this notification for showing detailed exceptions to UI
            }

            return !donorList.isEmpty();
        }
        return false;
    }

    @Override
    public Donor save(Donor object) {
        Donor donor = super.save(object);
        try {
//			asyncClass.sendRegistrationToMirth(donor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return donor;
    }

    private void runAutoGenCodeRule(String unit) {
        try {
            Application application = eurekaClient.getApplication(integrationConfigProperties.getRuleEngineServiceID());
            InstanceInfo instanceInfo = application.getInstances().get(0);
            String ruleUrl = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/rule/runRule";
            RestTemplate restTemplate = new RestTemplate();
            JsonObject requestBody = new JsonObject();
            requestBody.add("rules", new JsonArray());
            JsonObject payloadObject = new JsonObject();
            payloadObject.addProperty("type", "Donor");
            payloadObject.addProperty("unit", unit);
            requestBody.add("payload", payloadObject);
            String response = restTemplate.postForObject(ruleUrl, requestBody.toString(), String.class).replaceAll("\"",
                    "");
            logger.info("Rule Engine Response " + response.toString());
            JsonObject responseObject = new JsonParser().parse(response).getAsJsonArray().get(0).getAsJsonObject();
            this.donorCode = responseObject.get("code") != null ? responseObject.get("code").getAsString()
                    : donorConfigProperties.getAutogenaratorCode();
            this.startNo = responseObject.get("startingNo") != null ? responseObject.get("startingNo").getAsString()
                    : donorConfigProperties.getAutogenaratorStartno();
        } catch (Exception e) {
			this.donorCode = donorConfigProperties.getAutogenaratorCode();
			this.startNo = donorConfigProperties.getAutogenaratorStartno();
            throw new RuleEngineException("Rule Engine not accesible : " + e.getMessage());
        }
    }

    private boolean checkLastDonorNo(Donor lastDonor) {
        if (lastDonor != null && StringUtils.isNotBlank(getDonorNo(lastDonor))) {
            return true;
        }
        return false;
    }

    private boolean checkLastDonorAutoSeq(Donor lastDonor) {
        if (lastDonor != null && lastDonor.getDonorAutoSequenceNo() != null
                && Long.parseLong(lastDonor.getDonorAutoSequenceNo().getValueAsString()) > 0) {
            return true;
        }
        return false;
    }

    private String getDonorNo(Donor donor) {
        // FIXME: Change the logic to MongoDB Query
        if (!donor.getIdentifier().isEmpty()) {
            Optional<String> donorno = donor.getIdentifier().stream().filter(identity -> identity.getUseElement()
                    .getValueAsString().equals(IdentifierUse.USUAL.getDisplay().toLowerCase())).map(identification -> {
                if (identification != null) {
                    return identification.getValueElement() != null
                            ? identification.getValueElement().asStringValue()
                            : "";
                }
                return "";
            }).findFirst();
            return donorno.isPresent() ? donorno.get() : "";
        } else {

            return donor.getDonorno() != null && StringUtils.isNotBlank(donor.getDonorno().asStringValue())
                    ? donor.getDonorno().asStringValue()
                    : "";
        }

    }

}
