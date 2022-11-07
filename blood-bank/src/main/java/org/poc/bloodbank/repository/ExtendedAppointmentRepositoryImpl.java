package org.poc.bloodbank.repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.poc.bloodbank.entity.model.ExtendedAppointment;
import org.poc.bloodbank.enums.AppointmentSearchEnum;
import org.poc.bloodbank.util.BloodBankUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.regex.Pattern;

public class ExtendedAppointmentRepositoryImpl implements ExtendedAppointmentCustomQuery {

    private static final String MONGO_ACTOR_REFERENCE = "actor.reference";
    private static final String MONGO_TYPE_CODING_CODE = "type.coding.code";
    private static final String MONGO_ACTOR_DISPLAY = "actor.display";
    private static final String MONGO_ACTOR_IDENTIFIER_VALUE = "actor.identifier.value";
    @Autowired
    MongoTemplate mongoTemplate;

    // added for new search query
    @Override
    public List<ExtendedAppointment> findByQuery(JsonObject searchObject) {
        Query query = prepareSearchQuery(searchObject);
        return mongoTemplate.find(query, ExtendedAppointment.class);
    }

    private Query prepareSearchQuery(JsonObject searchObject) {
        Query query = new Query();
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.MRNNO.getValue())) {
            query.addCriteria(getMrnSearchCriteria(searchObject));
        }
        /*
         * if (rehabUtils.checkSearchKey(searchObject,
         * AppointmentSearchEnum.APPOINTMENT_DATE.getValue()) &&
         * getAppointmentDateCriteria(
         * searchObject.get(AppointmentSearchEnum.APPOINTMENT_DATE.getValue())) != null)
         * { query.addCriteria(
         * getAppointmentDateCriteria(searchObject.get(AppointmentSearchEnum.
         * APPOINTMENT_DATE.getValue())));
         *
         * }
         */
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.STATUS.getValue())) {
            query.addCriteria(Criteria.where("status")
                    .regex(Pattern.compile(searchObject.get(AppointmentSearchEnum.STATUS.getValue()).getAsString(),
                            Pattern.CASE_INSENSITIVE)));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.DOCTOR.getValue().toString())) {

            query.addCriteria(getDoctorCriteria(searchObject));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.DEPARTMENT_ID.getValue())) {
            query.addCriteria(Criteria.where("serviceType")
                    .elemMatch(Criteria.where("coding.display")
                            .regex(Pattern.compile(
                                    searchObject.get(AppointmentSearchEnum.DEPARTMENT_ID.getValue()).getAsString(),
                                    Pattern.CASE_INSENSITIVE))));

        }
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.CLINIC_ID.getValue())) {
            query.addCriteria(Criteria.where(AppointmentSearchEnum.CLINIC_ID.getValue())
                    .is(searchObject.get(AppointmentSearchEnum.CLINIC_ID.getValue()).getAsString()));

        }
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.UNIT_ID.getValue())) {
            query.addCriteria(Criteria.where(AppointmentSearchEnum.UNIT_ID.getValue())
                    .is(searchObject.get(AppointmentSearchEnum.UNIT_ID.getValue()).getAsString()));

        }
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.UNIT_CODE.getValue())) {
            query.addCriteria(Criteria.where(AppointmentSearchEnum.UNIT_CODE.getValue())
                    .is(searchObject.get(AppointmentSearchEnum.UNIT_CODE.getValue()).getAsString()));

        }
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.OFFSET.getValue())) {
            query.skip(Integer.parseInt(searchObject.get(AppointmentSearchEnum.OFFSET.getValue()).getAsString()));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.LIMIT.getValue())) {
            query.limit(Integer.parseInt(searchObject.get(AppointmentSearchEnum.LIMIT.getValue()).getAsString()));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.NAME.getValue()))
            query.addCriteria(getNameCriteria(searchObject.get(AppointmentSearchEnum.NAME.getValue())));

        if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.PRIMARY_ID.getValue())) {
            query.addCriteria(getPrimaryIdCriteria(searchObject.get(AppointmentSearchEnum.PRIMARY_ID.getValue())));
        } else if (BloodBankUtils.checkSearchKey(searchObject, AppointmentSearchEnum.MOBILE.getValue())) {
            query.addCriteria(getMobileCriteria(searchObject.get(AppointmentSearchEnum.MOBILE.getValue())));
        }
        return query;
    }

    @Override
    public Long findCountByQuery(JsonObject searchObject) {
        return mongoTemplate.count(prepareSearchQuery(searchObject), ExtendedAppointment.class);
    }

    private CriteriaDefinition getNameCriteria(JsonElement jsonElement) {
        String name = jsonElement.getAsString().trim();
        if (StringUtils.isNotBlank(name)) {
            return Criteria.where("participant").elemMatch(Criteria.where(MONGO_ACTOR_DISPLAY)
                    .regex(Pattern.compile(name, Pattern.CASE_INSENSITIVE)).and(MONGO_TYPE_CODING_CODE).is("Patient"));
        }
        return null;
    }

    private Criteria getDoctorCriteria(JsonObject searchObject) {
        Criteria criteriaDoctorDisplay = Criteria.where("participant")
                .elemMatch(Criteria.where(MONGO_ACTOR_DISPLAY)
                        .regex(Pattern.compile(searchObject.get(AppointmentSearchEnum.DOCTOR.getValue()).getAsString(),
                                Pattern.CASE_INSENSITIVE))
                        .and(MONGO_TYPE_CODING_CODE).is("Doctor"));
        Criteria criteriaDoctorReference = Criteria.where("participant")
                .elemMatch(Criteria.where(MONGO_ACTOR_REFERENCE)
                        .regex(Pattern.compile(searchObject.get(AppointmentSearchEnum.DOCTOR.getValue()).getAsString(),
                                Pattern.CASE_INSENSITIVE))
                        .and(MONGO_TYPE_CODING_CODE).is("Doctor"));
        return new Criteria().orOperator(criteriaDoctorDisplay, criteriaDoctorReference);

    }

    /*
     * private Criteria getMrnSearchCriteria(JsonObject searchObject) { Criteria
     * mrnCriteria = new Criteria();
     * mrnCriteria.orOperator(Criteria.where(MONGO_SUBJECT_DISPLAY).regex("\""+
     * EncounterSearchEnum.MRNNO.getValue()+
     * "\":"+searchObject.get(EncounterSearchEnum.MRNNO.getValue()).toString()),
     * Criteria.where(MONGO_SUBJECT_IDENTIFIER_VALUE).is(searchObject.get(
     * EncounterSearchEnum.MRNNO.getValue()).getAsString())); return mrnCriteria; }
     */
    private Criteria getMrnSearchCriteria(JsonObject searchObject) {

        return Criteria.where("participant." + MONGO_ACTOR_IDENTIFIER_VALUE).regex(Pattern.compile(
                searchObject.get(AppointmentSearchEnum.MRNNO.getValue()).getAsString(), Pattern.CASE_INSENSITIVE));
    }

    /*
     * private CriteriaDefinition getAppointmentDateCriteria(JsonElement
     * searchObject) { JsonObject encounterDate = searchObject.getAsJsonObject(); if
     * (rehabUtils.checkSearchKey(encounterDate,
     * AppointmentSearchEnum.AP_STARTDATE.getValue())) { if
     * (rehabUtils.checkSearchKey(encounterDate,
     * AppointmentSearchEnum.AP_ENDDATE.getValue())) return Criteria.where("start")
     * .gte(rehabUtils.getFhirInstantObjectFromEpochTime(
     * encounterDate.get(AppointmentSearchEnum.AP_STARTDATE.getValue()).getAsString(
     * ))) .lte(rehabUtils.getFhirInstantObjectFromEpochTime(
     * encounterDate.get(AppointmentSearchEnum.AP_ENDDATE.getValue()).getAsString())
     * );
     *
     * } return null; }
     */

    private Criteria getPrimaryIdCriteria(JsonElement jsonElement) {
        String primaryIdentificationID = jsonElement.getAsString().trim();
        if (StringUtils.isNotBlank(primaryIdentificationID)) {
            return Criteria.where("participant").elemMatch(Criteria.where(MONGO_ACTOR_IDENTIFIER_VALUE)
                    .regex(Pattern.compile(primaryIdentificationID, Pattern.CASE_INSENSITIVE)));
        }
        return null;
    }

    private Criteria getMobileCriteria(JsonElement jsonElement) {
        String mobile = jsonElement.getAsString().trim();
        if (StringUtils.isNotBlank(mobile)) {
            return Criteria.where("participant").elemMatch(
                    Criteria.where(MONGO_ACTOR_DISPLAY).regex(Pattern.compile(mobile, Pattern.CASE_INSENSITIVE)));
        }
        return null;
    }
}