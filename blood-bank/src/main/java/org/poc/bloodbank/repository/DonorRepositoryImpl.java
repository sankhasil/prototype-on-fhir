
/**
 *
 */
package org.poc.bloodbank.repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.poc.bloodbank.constants.DonorRepoConstants;
import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.enums.DonorSearchEnum;
import org.poc.bloodbank.util.BloodBankUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Sankha
 *
 */
public class DonorRepositoryImpl implements DonorCustomQuery {

    @Autowired
    MongoTemplate mongoTemplate;

    Logger patientRepoLogger = LoggerFactory.getLogger(DonorRepositoryImpl.class);

    @Override
    public List<Donor> findAll(int skip, int limit) {
        Query query = new Query();
        query.skip(skip).limit(limit);

        return mongoTemplate.find(query, Donor.class);
    }

    @Override
    public Donor findLast() {
        Query query = new Query();
        query.with(Sort.by(Direction.DESC, DonorRepoConstants.MONGO_DB_KEY_DONOR_AUTOSEQ_NO)).limit(1);
        List<Donor> patientList = mongoTemplate.find(query, Donor.class);
        return patientList != null && !patientList.isEmpty() ? patientList.get(0) : null;
    }

    @Override
    public Donor findLastForUnit(String unitPrefix) {
        Query query = new Query();
        query.addCriteria(Criteria.where(DonorRepoConstants.MONGO_DB_KEY_IDENTIFIER_USE).is(DonorRepoConstants.FHIR_INDENTIFIER_USUAL)
                .and(DonorRepoConstants.MONGO_DB_KEY_IDENTIFIER_TYPE_TEXT).is(unitPrefix));
        query.with(Sort.by(Direction.DESC, DonorRepoConstants.MONGO_DB_KEY_DONOR_IDENTIFIER)).limit(1);
        List<Donor> patientList = mongoTemplate.find(query, Donor.class);
        return patientList != null && !patientList.isEmpty() ? patientList.get(0) : null;
    }

    @Override
    public List<Donor> findByQuery(JsonObject jObject) {
        try {
            Query query = prepareSearchQuery(jObject);
            return mongoTemplate.find(query, Donor.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Long findCountByQuery(JsonObject searchObject) {
        searchObject.remove(DonorSearchEnum.OFFSET.getValue());
        searchObject.remove(DonorSearchEnum.LIMIT.getValue());
        return mongoTemplate.count(prepareSearchQuery(searchObject), Donor.class);
    }

    private Query prepareSearchQuery(JsonObject jObject) {
        Query query = new Query();
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.NAME.getValue()))
            query.addCriteria(getNameCriteria(jObject.get(DonorSearchEnum.NAME.getValue())));
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.GENDER.getValue()))
            query.addCriteria(Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_GENDER)
                    .is(jObject.get(DonorSearchEnum.GENDER.getValue()).getAsString()));
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.IDENTIFICATION.getValue())
                && getIdentityCriteria(jObject.get(DonorSearchEnum.IDENTIFICATION.getValue())) != null)
            query.addCriteria(getIdentityCriteria(jObject.get(DonorSearchEnum.IDENTIFICATION.getValue())));
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.ADDRESS.getValue())
                && getAddressCriteria(jObject.get(DonorSearchEnum.ADDRESS.getValue())) != null)
            query.addCriteria(getAddressCriteria(jObject.get(DonorSearchEnum.ADDRESS.getValue())));
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.MOBILE.getValue()))
            query.addCriteria(getMobileCriteria(jObject.get(DonorSearchEnum.MOBILE.getValue())));
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.DATEOFBIRTH.getValue()))
            query.addCriteria(getDobCriteria(jObject.get(DonorSearchEnum.DATEOFBIRTH.getValue())));
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.DONORCATEGORY.getValue()))
            query.addCriteria(Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_CATEGORY)
                    .is(jObject.get(DonorSearchEnum.DONORCATEGORY.getValue()).getAsString()));
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.DONORNO.getValue())) {
            query.addCriteria(getMrnCriteria(jObject.get(DonorSearchEnum.DONORNO.getValue())));
        }
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.UNITID.getValue())) {
            query.addCriteria(Criteria.where(DonorSearchEnum.UNITID.getValue())
                    .is(jObject.get(DonorSearchEnum.UNITID.getValue())));
        }
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.UNIT.getValue())) {
            query.addCriteria(Criteria.where(DonorRepoConstants.MONGODB_KEY_UNIT_CODE).regex(Pattern
                    .compile(jObject.get(DonorSearchEnum.UNIT.getValue()).getAsString(), Pattern.CASE_INSENSITIVE)));
        }
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.BLOODGROUP.getValue())) {
            query.addCriteria(Criteria.where(DonorRepoConstants.MONGO_DB_DONOR_BLOOD_GROUP).regex(Pattern
                    .compile(jObject.get(DonorSearchEnum.BLOODGROUP.getValue()).getAsString(), Pattern.CASE_INSENSITIVE)));
        }

        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.OFFSET.getValue())) {
            query.skip(Integer.parseInt(jObject.get(DonorSearchEnum.OFFSET.getValue()).getAsString()));
        }
        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.LIMIT.getValue())) {
            query.limit(Integer.parseInt(jObject.get(DonorSearchEnum.LIMIT.getValue()).getAsString()));
        }

        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.REDG_DATE.getValue())
                && getRedgByDateCriteria(jObject.get(DonorSearchEnum.REDG_DATE.getValue())) != null)
            query.addCriteria(getRedgByDateCriteria(jObject.get(DonorSearchEnum.REDG_DATE.getValue())));

        if (BloodBankUtils.checkSearchKey(jObject, DonorSearchEnum.PINCODE.getValue()))
            query.addCriteria(getAddressCriteriaForPincode(jObject.get(DonorSearchEnum.PINCODE.getValue())));

        patientRepoLogger.info("prepared Query : {}", query.toString());
        return query;
    }

    private CriteriaDefinition getDobCriteria(JsonElement jsonElement) {

        String birthDate = jsonElement.getAsString();
        if (StringUtils.isNotBlank(birthDate)) {
            return Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_BIRTH_DATE).is(birthDate);
        }
        return null;
    }

    private CriteriaDefinition getMrnCriteria(JsonElement jsonElement) {
        String mrnno = jsonElement.getAsString();
        if (StringUtils.isNotBlank(mrnno)) {
            return Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_NO).regex((Pattern.compile(mrnno, Pattern.CASE_INSENSITIVE)));
        }
        return null;
    }

    private CriteriaDefinition getMobileCriteria(JsonElement jsonElement) {

        String mobile = jsonElement.getAsString();
        if (StringUtils.isNotBlank(mobile)) {
            return Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_TELECOM).elemMatch(Criteria.where("value").is(mobile));
        }
        return null;
    }

    private CriteriaDefinition getAddressCriteriaForPincode(JsonElement jsonElement) {
        String pincode = jsonElement.getAsString();
        if (StringUtils.isNotBlank(pincode)) {
            return Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_ADDRESS).elemMatch(Criteria.where("postalCode").is(pincode));
        }
        return null;
    }

    private CriteriaDefinition getRedgByDateCriteria(JsonElement jsonElement) {
        JsonObject registrationDate = jsonElement.getAsJsonObject();
        if (BloodBankUtils.checkSearchKey(registrationDate, DonorSearchEnum.REDG_STARTDATE.getValue())) {
            if (BloodBankUtils.checkSearchKey(registrationDate, DonorSearchEnum.REDG_ENDDATE.getValue()))
                ;
            return Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_REGISTRATION_DATE).gte(registrationDate.get("start").getAsInt())
                    .lte(registrationDate.get("end").getAsInt());
        }
        return null;
    }

    private CriteriaDefinition getNameCriteria(JsonElement jsonElement) {
        String name = jsonElement.getAsString().trim();
        if (StringUtils.isNotBlank(name)) {
            Criteria nameCriteria = new Criteria();
            /*
             * nameCriteria.orOperator(Criteria.where(MONGO_DB_KEY_PATIENT_NAME).elemMatch(
             * Criteria.where(MONGO_DB_KEY_DONOR_NAME_TEXT).regex(Pattern.compile(name))),
             * Criteria.where(MONGO_DB_KEY_PATIENT_NAME).elemMatch(Criteria.where(
             * MONGO_DB_KEY_DONOR_NAME_FAMILY).regex(Pattern.compile(name))),
             * Criteria.where(MONGO_DB_KEY_PATIENT_NAME).elemMatch(Criteria.where(
             * MONGO_DB_KEY_DONOR_NAME_GIVEN).regex(Pattern.compile(name))));
             */
            nameCriteria.orOperator(
                    Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_NAME)
                            .elemMatch(Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_NAME_TEXT)
                                    .regex(Pattern.compile(name, Pattern.CASE_INSENSITIVE))),
                    Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_NAME)
                            .elemMatch(Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_NAME_FAMILY)
                                    .regex(Pattern.compile(name, Pattern.CASE_INSENSITIVE))),
                    Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_NAME).elemMatch(Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_NAME_GIVEN)
                            .regex(Pattern.compile(name, Pattern.CASE_INSENSITIVE))));
            return nameCriteria;
        }
        return null;
    }

    private CriteriaDefinition getIdentityCriteria(JsonElement jsonElement) {
        JsonObject identiy = jsonElement.getAsJsonObject();
        if (BloodBankUtils.checkSearchKey(identiy, DonorSearchEnum.TYPE.getValue())) {
            if (BloodBankUtils.checkSearchKey(identiy, DonorSearchEnum.VALUE.getValue().trim()))
                return new Criteria()
                        .orOperator(
                                Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_IDENTIFIER + "." + DonorRepoConstants.MONGO_DB_KEY_DONOR_TYPE_CODE)
                                        .is(identiy.get(DonorSearchEnum.TYPE.getValue()).getAsString()),
                                Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_IDENTIFIER + "." + DonorRepoConstants.MONGO_DB_KEY_DONOR_TYPE_TEXT)
                                        .is(identiy.get(DonorSearchEnum.TYPE.getValue()).getAsString()))
                        .and(DonorRepoConstants.MONGO_DB_KEY_DONOR_IDENTIFIER + "." + "value")
                        .regex(Pattern.compile(identiy.get(DonorSearchEnum.VALUE.getValue()).getAsString(), Pattern.CASE_INSENSITIVE));
            else
                return Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_IDENTIFIER)
                        .elemMatch(new Criteria().orOperator(
                                Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_TYPE_TEXT)
                                        .is(identiy.get(DonorSearchEnum.TYPE.getValue()).getAsString()),
                                Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_TYPE_CODE)
                                        .is(identiy.get(DonorSearchEnum.TYPE.getValue()).getAsString())));

        } else if (BloodBankUtils.checkSearchKey(identiy, DonorSearchEnum.VALUE.getValue().trim())) {
            return Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_IDENTIFIER + "." + "value")
                    .regex(Pattern.compile(identiy.get(DonorSearchEnum.VALUE.getValue()).getAsString(), Pattern.CASE_INSENSITIVE));
        }

        return null;
    }

    private CriteriaDefinition getAddressCriteria(JsonElement jsonElement) {
        JsonObject address = jsonElement.getAsJsonObject();
        if (BloodBankUtils.checkSearchKey(address, DonorSearchEnum.USE.getValue())) {
            if (BloodBankUtils.checkSearchKey(address, DonorSearchEnum.COUNTRY.getValue())) {
                if (BloodBankUtils.checkSearchKey(address, DonorSearchEnum.STATE.getValue())) {
                    if (BloodBankUtils.checkSearchKey(address, DonorSearchEnum.CITY.getValue()))
                        return Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_ADDRESS)
                                .elemMatch(
                                        new Criteria()
                                                .andOperator(
                                                        Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_ADDRESS_USE)
                                                                .is(address.get(DonorSearchEnum.USE.getValue())
                                                                        .getAsString()),
                                                        Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_ADDRESS_COUNTRY)
                                                                .is(address.get(DonorSearchEnum.COUNTRY.getValue())
                                                                        .getAsString()),
                                                        Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_ADDRESS_STATE)
                                                                .is(address.get(DonorSearchEnum.STATE.getValue())
                                                                        .getAsString()),
                                                        Criteria.where(DonorRepoConstants.MONGO_DB_KEY_DONOR_ADDRESS_CITY)
                                                                .is(address.get(DonorSearchEnum.CITY.getValue())
                                                                        .getAsString())));
                }
            }
        }
        return null;

    }

}
