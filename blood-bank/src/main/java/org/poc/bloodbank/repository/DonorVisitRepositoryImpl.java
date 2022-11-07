/**
 *
 */
package org.poc.bloodbank.repository;

import com.google.gson.JsonObject;
import org.bson.Document;
import org.poc.bloodbank.constants.DonorRepoConstants;
import org.poc.bloodbank.entity.model.DonorVisit;
import org.poc.bloodbank.enums.DonorVisitEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


/**
 * @author Sankha
 */
public class DonorVisitRepositoryImpl implements DonorVisitCustomQuery {


    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public DonorVisit findLastDonorVisit() {
        Query query = new Query();
        query.with(Sort.by(Direction.DESC, DonorRepoConstants.MONGO_DB_DONOR_VISIT_NO)).limit(1);
        List<DonorVisit> donorVisitList = mongoTemplate.find(query, DonorVisit.class);
        return donorVisitList != null && !donorVisitList.isEmpty() ? donorVisitList.get(0) : null;

    }


    @Override
    public DonorVisit findLastDonorVisitByUnit(String unitCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DonorVisit> findByQuery(JsonObject searchObject, Boolean isNotCheck) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AggregationResults<Document> getAggregationData(String donorVisitId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long findCountByQuery(JsonObject searchObject, boolean isNotCheck) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AggregationResults<Document> findDataByAggregation(DonorVisitEnum type, boolean isAsscending, Long fromDate,
                                                              Long toDate) {
        // TODO Auto-generated method stub
        return null;
    }
}
