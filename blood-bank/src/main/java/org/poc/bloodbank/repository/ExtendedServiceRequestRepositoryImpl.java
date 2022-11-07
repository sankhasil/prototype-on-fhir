/**
 *
 */
package org.poc.bloodbank.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;
import org.poc.bloodbank.enums.ServiceRequestSearchEnum;
import org.poc.bloodbank.util.BloodBankUtils;
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
 */
public class ExtendedServiceRequestRepositoryImpl implements ExtendedServiceRequestCustomQuery {
    private static final String MONGO_KEY_PERFORMER_IDENTIFIER_VALUE = "performer.identifier.value";
    private static final String MONGO_KEY_PERFORMER_IDENTIFIER_DISPLAY = "performer.identifier.type.coding.display";
    private static final String MONGO_KEY_PERFORMER_DISPLAY = "performer.identifier.display";
    private static final String MONGO_KEY_PERFORMER_IDENTIFIER_CODE = "performer.identifier.type.coding.code";
    private static final String MONGO_KEY_PROCEDURE_CODE = "plannedProcedure.code";
    private static final String MONGO_KEY_PROCEDURE_DISPLAY = "plannedProcedure.display";
    private static final String MONGO_KEY_SUBJECT_DISPLAY = "subject.display";
    private static final String MONGO_KEY_LOCATION_CODING_CODE = "locationCode.coding.code";
    private static final String MONGO_KEY_LOCATION_CODING_DISPLAY = "locationCode.coding.display";
    private static final String MONGO_KEY_BUSINESS_STATUS = "businessStatus";
    private static final String MONGODB_KEY_UNIT_CODE = "unitCode";
    private static final String MONGO_DB_KEY_SLOT_START_TIME = "slotStartTime";
    private static final String MONGO_DB_KEY_IDENTIFIER_TYPE_TEXT = "identifier.0.type.text";
    private static final String FHIR_INDENTIFIER_USUAL = "usual";
    private static final String MONGO_DB_KEY_IDENTIFIER_USE = "identifier.0.use";
    private static final String MONGO_DB_KEY_SERVICEREQUEST_IDENTIFIER = "identifier";
    public static final String BUSINESS_STATUS_REQUESTED = "requested";
    public static final String BUSINESS_STATUS_SCHEDULED = "scheduled";
    public static final String BUSINESS_STATUS_CHECKEDIN = "checkedin";
    public static final String BUSINESS_STATUS_CHECKEDOUT = "checkedout";
    public static final String BUSINESS_STATUS_ARRIVED = "arrived";
    // private static final String MONGODB_KEY_STATUS= "status";
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Long findCountByQuery(JsonObject searchObject) {
        searchObject.remove(ServiceRequestSearchEnum.OFFSET.getValue());
        searchObject.remove(ServiceRequestSearchEnum.LIMIT.getValue());
        return mongoTemplate.count(prepareSearchQuery(searchObject), ExtendedServiceRequest.class);
    }

    @Override
    public ExtendedServiceRequest findLast() {
        Query query = new Query();
        query.with(Sort.by(Direction.DESC, MONGO_DB_KEY_SLOT_START_TIME)).limit(1);
        List<ExtendedServiceRequest> serviceRequestList = mongoTemplate.find(query, ExtendedServiceRequest.class);
        return serviceRequestList != null && !serviceRequestList.isEmpty() ? serviceRequestList.get(0) : null;
    }

    @Override
    public ExtendedServiceRequest findLastForPrefix(String unitPrefix) {
        Query query = new Query();
        query.addCriteria(Criteria.where(MONGO_DB_KEY_IDENTIFIER_USE).is(FHIR_INDENTIFIER_USUAL)
                .and(MONGO_DB_KEY_IDENTIFIER_TYPE_TEXT).is(unitPrefix));
        query.with(Sort.by(Direction.DESC, MONGO_DB_KEY_SERVICEREQUEST_IDENTIFIER)).limit(1);
        List<ExtendedServiceRequest> serviceRequestList = mongoTemplate.find(query, ExtendedServiceRequest.class);
        return serviceRequestList != null && !serviceRequestList.isEmpty() ? serviceRequestList.get(0) : null;
    }

    /*
     * @Override public List<ExtendedServiceRequest> findServiceStatus(String
     * status){ Query query = new Query();
     * query.addCriteria(Criteria.where(MONGODB_KEY_STATUS).is(status));
     * List<ExtendedServiceRequest> serviceRequestList = mongoTemplate.find(query,
     * ExtendedServiceRequest.class); return serviceRequestList;
     *
     * }
     */

    @Override
    public List<ExtendedServiceRequest> findByQuery(JsonObject searchObject) {
        Query query = prepareSearchQuery(searchObject);
        return mongoTemplate.find(query, ExtendedServiceRequest.class);
    }

    private Query prepareSearchQuery(JsonObject searchObject) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.STATUS.getValue())) {
            query.addCriteria(Criteria.where("status")
                    .regex(Pattern.compile(searchObject.get(ServiceRequestSearchEnum.STATUS.getValue()).getAsString(),
                            Pattern.CASE_INSENSITIVE)));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.OFFSET.getValue())) {
            query.skip(Integer.parseInt(searchObject.get(ServiceRequestSearchEnum.OFFSET.getValue()).getAsString()));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.LIMIT.getValue())) {
            query.limit(Integer.parseInt(searchObject.get(ServiceRequestSearchEnum.LIMIT.getValue()).getAsString()));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.BUSINESS_STATUS.getValue())) {
            Object json = searchObject.get(ServiceRequestSearchEnum.BUSINESS_STATUS.getValue());
            if (json instanceof JsonArray) {
                criteria.orOperator(Criteria.where(MONGO_KEY_BUSINESS_STATUS).is(BUSINESS_STATUS_SCHEDULED),
                        Criteria.where(MONGO_KEY_BUSINESS_STATUS).is(BUSINESS_STATUS_CHECKEDIN),
                        Criteria.where(MONGO_KEY_BUSINESS_STATUS).is(BUSINESS_STATUS_CHECKEDOUT),
                        Criteria.where(MONGO_KEY_BUSINESS_STATUS).is(BUSINESS_STATUS_ARRIVED));
                query.addCriteria(criteria);
            } else {
                query.addCriteria(Criteria.where(MONGO_KEY_BUSINESS_STATUS)
                        .regex(Pattern.compile(
                                searchObject.get(ServiceRequestSearchEnum.BUSINESS_STATUS.getValue()).getAsString(),
                                Pattern.CASE_INSENSITIVE)));

            }
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.CREATED_DATE.getValue())
                && getCreatedtDateCriteria(
                searchObject.get(ServiceRequestSearchEnum.CREATED_DATE.getValue())) != null) {
            query.addCriteria(
                    getCreatedtDateCriteria(searchObject.get(ServiceRequestSearchEnum.CREATED_DATE.getValue())));

        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.UNIT_CODE.getValue())) {
            query.addCriteria(Criteria.where(ServiceRequestSearchEnum.UNIT_CODE.getValue())
                    .is(searchObject.get(ServiceRequestSearchEnum.UNIT_CODE.getValue()).getAsString()));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.MRNNO.getValue())) {
            query.addCriteria(getMrnSearchCriteria(searchObject));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.PERFORMER.getValue())) {
            query.addCriteria(getPerformerCriteria(searchObject));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.PROCEDURE.getValue())) {
            query.addCriteria(getProcedureCriteria(searchObject));
        }

        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.NAME.getValue())) {
            query.addCriteria(getPatientNameSearchCriteria(searchObject));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.DATE_OF_PROCEDURE.getValue())
                && getDateProcedureCriteria(
                searchObject.get(ServiceRequestSearchEnum.DATE_OF_PROCEDURE.getValue())) != null) {
            query.addCriteria(
                    getDateProcedureCriteria(searchObject.get(ServiceRequestSearchEnum.DATE_OF_PROCEDURE.getValue())));

        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.SORT_BY.getValue())
                && searchObject.get(ServiceRequestSearchEnum.SORT_BY.getValue()) != null) {
            query.with(Sort.by(Direction.ASC, ServiceRequestSearchEnum.SORT_BY.getValue()));

        } else {
            query.with(Sort.by(Direction.ASC, MONGO_DB_KEY_SLOT_START_TIME));
        }
        if (BloodBankUtils.checkSearchKey(searchObject, ServiceRequestSearchEnum.LOCATION_CODE.getValue())) {
            query.addCriteria(getLocationCodeSearchCriteria(searchObject));
        }
        return query;
    }

    private CriteriaDefinition getProcedureCriteria(JsonObject searchObject) {
        return Criteria.where(MONGO_KEY_PROCEDURE_DISPLAY).regex(Pattern.compile(
                searchObject.get(ServiceRequestSearchEnum.PROCEDURE.getValue()).getAsString(), Pattern.CASE_INSENSITIVE));
    }

    private CriteriaDefinition getCreatedtDateCriteria(JsonElement searchObject) {
        JsonObject operationDate = searchObject.getAsJsonObject();
        if (BloodBankUtils.checkSearchKey(operationDate, ServiceRequestSearchEnum.START_DATE.getValue())) {
            if (BloodBankUtils.checkSearchKey(operationDate, ServiceRequestSearchEnum.END_DATE.getValue()))
                return Criteria.where("createdDate").gte(operationDate.get("start").getAsInt())
                        .lte(operationDate.get("end").getAsInt());

        }

        return null;
    }

    private Criteria getPerformerCriteria(JsonObject searchObject) {
        return Criteria.where(MONGO_KEY_PERFORMER_IDENTIFIER_DISPLAY).regex(Pattern.compile(
                searchObject.get(ServiceRequestSearchEnum.PERFORMER.getValue()).getAsString(), Pattern.CASE_INSENSITIVE));
    }

    private Criteria getMrnSearchCriteria(JsonObject searchObject) {

        return Criteria.where(MONGO_KEY_SUBJECT_DISPLAY)
                .regex(Pattern.compile(searchObject.get(ServiceRequestSearchEnum.MRNNO.getValue()).getAsString(),
                        Pattern.CASE_INSENSITIVE));
    }

    private Criteria getPatientNameSearchCriteria(JsonObject searchObject) {

        return Criteria.where(MONGO_KEY_SUBJECT_DISPLAY)
                .regex(Pattern.compile(searchObject.get(ServiceRequestSearchEnum.NAME.getValue()).getAsString(),
                        Pattern.CASE_INSENSITIVE));
    }

    private Criteria getLocationCodeSearchCriteria(JsonObject searchObject) {

        return Criteria.where(MONGO_KEY_LOCATION_CODING_CODE)
                .regex(Pattern.compile(searchObject.get(ServiceRequestSearchEnum.LOCATION_CODE.getValue()).getAsString(),
                        Pattern.CASE_INSENSITIVE));
    }

    private CriteriaDefinition getDateProcedureCriteria(JsonElement searchObject) {
        JsonObject operationDate = searchObject.getAsJsonObject();
        if (BloodBankUtils.checkSearchKey(operationDate, ServiceRequestSearchEnum.START_DATE.getValue())) {
            if (BloodBankUtils.checkSearchKey(operationDate, ServiceRequestSearchEnum.END_DATE.getValue()))
                return Criteria.where("dateOfProcedure").gte(operationDate.get("start").getAsInt())
                        .lt(operationDate.get("end").getAsInt());

        }

        return null;
    }
}
