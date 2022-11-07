/**
 *
 */
package org.poc.bloodbank.service;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.Reference;
import org.poc.bloodbank.Async.IntegrationChannel;
import org.poc.bloodbank.Async.ParallelExecution;
import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.entity.model.DonorVisit;
import org.poc.bloodbank.property.DonorConfigProperties;
import org.poc.bloodbank.property.IntegrationConfigProperties;
import org.poc.bloodbank.repository.DonorVisitRepository;
import org.poc.bloodbank.rest.model.DonorVisitPatchRequestBody;
import org.poc.bloodbank.rest.model.Measurement;
import org.poc.bloodbank.util.BloodBankUtils;
import org.poc.core.base.rest.util.HttpUtils;
import org.poc.core.constants.HttpHeaders;
import org.poc.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * @author Sankha
 *
 */
public class DonorVisitService extends BaseService<DonorVisit> {
    Logger logger = LoggerFactory.getLogger(DonorVisitService.class);
    JsonObject notification = new JsonObject();
    String donorVisitCode, startNo;
    IntegrationChannel asyncClass;
    ParallelExecution parallelClass;
    DonorVisitRepository donorVisitRepository;
    DonorConfigProperties patientConfigProperties;
    IntegrationConfigProperties integrationConfigProperties;
//	@Autowired
//	private EurekaClient eurekaClient;

    /**
     * add patient reference details
     *
     * @param donorVisit
     * @param donor
     */
    public void addDonorDetails(DonorVisit donorVisit, Donor donor) {
        Reference donorRef = new Reference();
        Identifier donorIdentifer = new Identifier();
        donorIdentifer.setUse(IdentifierUse.USUAL);
        donorIdentifer.setValue(donor.getDonorno().asStringValue());
        donorRef.setReference(donor.getId());
        donorRef.setIdentifier(donorIdentifer);
        donorRef.setId(donor.getId());
        JsonObject donorJsonObject = new JsonObject();
        donorJsonObject.addProperty("name",
                donor.getName().size() > 0 ? donor.getName().get(0).getText() : donor.getFullName().toString());
        donorJsonObject.addProperty("otherName",
                donor.getName().size() > 0 ? donor.getName().get(1).getText() : donor.getFullName().toString());
        donorJsonObject.addProperty("gender", donor.getGender().getDisplay());
        donorJsonObject.addProperty("donorNo", donor.getDonorno().asStringValue());
        donorJsonObject.addProperty("title",
                donor.getName().size() > 0 ? donor.getName().get(0).getPrefix().get(0).getValue()
                        : donor.getFullName().toString());
        donorJsonObject.addProperty("dob",
                donor.getBirthDate() != null ? BloodBankUtils.convertDateToString(donor.getBirthDate()) + "" : "");
        donorJsonObject.addProperty("phone", donor.getTelecom().size() > 0 ? donor.getTelecom().get(0).getValue() : "");
        donorJsonObject.addProperty("email", donor.getTelecom().size() > 1 ? donor.getTelecom().get(1).getValue() : "");
        donorJsonObject.addProperty("identificationType",
                donor.getIdentifier().size() > 1 ? donor.getIdentifier().get(1).getType().getText() : "");
        donorJsonObject.addProperty("identificationId",
                donor.getIdentifier().size() > 1 ? donor.getIdentifier().get(1).getValue() : "");
        donorJsonObject.addProperty("donorCategory",
                donor.getDonorCategory() != null ? donor.getDonorCategory().asStringValue() : "");

        donorRef.setDisplay(donorJsonObject.toString());
        donorVisit.setSubject(donorRef);
    }

    public List<DonorVisit> search(JsonObject object, Boolean isNotCheck) {
        if (object.get("patientMongoId") != null) {

            List<DonorVisit> donorVisitList = donorVisitRepository.findOldDonorVisitByPatientId(
                    object.get("patientMongoId").getAsString(), Sort.by(Direction.DESC, "donorVisitTime"));
            return donorVisitList;
        } else {
            String unitCode = HttpUtils.getHeader(HttpHeaders.UNIT_CODE);
            if (StringUtils.isNotBlank(unitCode)) {
                object.addProperty("unitCode", unitCode);
            }
            return donorVisitRepository.findByQuery(object, isNotCheck);
        }
    }

    public Long getCountBySearch(JsonObject searchObject) {
        String unitCode = HttpUtils.getHeader(HttpHeaders.UNIT_CODE);
        if (StringUtils.isNotBlank(unitCode)) {
            searchObject.addProperty("unitCode", unitCode);
        }
        return donorVisitRepository.findCountByQuery(searchObject, false);
    }

    public DonorVisit patchByPatchObject(DonorVisitPatchRequestBody donorRequestBody, String id) {
        if (StringUtils.isNotBlank(id) && donorRequestBody != null) {
            final DonorVisit oldDonorVisit = this.findById(id);
            if (oldDonorVisit != null) {
                if (donorRequestBody.getScreeningList() != null && donorRequestBody.getScreeningList().size() > 0) {
                    for (Measurement measurement : donorRequestBody.getScreeningList()) {
                        oldDonorVisit.getReferenceRange()
                                .removeIf(reference -> reference.getText().equalsIgnoreCase(measurement.getTopic()));
                        switch (measurement.getTopic().toLowerCase()) {
                            case "weight":
                            case "height":
                            case "hemoglobin":
                            case "pulse":
                                oldDonorVisit.addReferenceRange(BloodBankUtils.createObservationReferenceRangeComponent(
                                        measurement.getLowValue(), measurement.getLowValue(), new String[]{
                                                measurement.getTopic(), measurement.getType(), measurement.getUnit()}));
                                break;
                            case "bloodpressure":
                                oldDonorVisit.addReferenceRange(BloodBankUtils.createObservationReferenceRangeComponent(
                                        measurement.getLowValue(), measurement.getHighValue(), new String[]{
                                                measurement.getTopic(), measurement.getType(), measurement.getUnit()}));
                                break;
                            default:
                                break;
                        }
                    }
                }
                if (donorRequestBody.getBloodBagDetails() != null) {
                    DonorVisit.BloodBag bloodBag = new DonorVisit.BloodBag();
                    bloodBag.setCollectionTimeTo(new DecimalType(donorRequestBody.getBloodBagDetails().getCollectionDateTimeTo()));
                    bloodBag.setCollectionTimeFrom(new DecimalType(donorRequestBody.getBloodBagDetails().getCollectionDateTimeFrom()));
                    //TODO : finish the blood bag details set
                    oldDonorVisit.setBloodBagDetails(bloodBag);
                }

            }
        }

        return null;
    }

}
