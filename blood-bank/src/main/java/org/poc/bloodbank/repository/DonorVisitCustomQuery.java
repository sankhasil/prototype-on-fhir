/**
 *
 */
package org.poc.bloodbank.repository;

import com.google.gson.JsonObject;
import org.bson.Document;
import org.poc.bloodbank.entity.model.DonorVisit;
import org.poc.bloodbank.enums.DonorVisitEnum;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.List;

/**
 * @author Sankha
 *
 */
public interface DonorVisitCustomQuery {

    DonorVisit findLastDonorVisit();

    DonorVisit findLastDonorVisitByUnit(String unitCode);

    List<DonorVisit> findByQuery(JsonObject searchObject, Boolean isNotCheck);

    AggregationResults<Document> getAggregationData(String donorVisitId);

    Long findCountByQuery(JsonObject searchObject, boolean isNotCheck);

    AggregationResults<Document> findDataByAggregation(DonorVisitEnum type, boolean isAsscending, Long fromDate, Long toDate);

}
