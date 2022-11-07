/**
 *
 */
package org.poc.bloodbank.repository;

import org.poc.bloodbank.entity.model.DonorVisit;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author Sankha
 *
 */
public interface DonorVisitRepository extends MongoRepository<DonorVisit, String>, DonorVisitCustomQuery {

    //FIXME: Change the linkage
    @Query("{'subject.reference' : {$regex : ?0}}")
    List<DonorVisit> findOldDonorVisitByPatientId(String patientMongoId, Sort sort);
}
