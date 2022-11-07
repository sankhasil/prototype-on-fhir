/**
 *
 */
package org.poc.bloodbank.repository;

import org.poc.bloodbank.entity.model.ExtendedServiceRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sankha
 *
 */
@Repository
public interface ExtendedServiceRequestRepository extends MongoRepository<ExtendedServiceRequest, String>, ExtendedServiceRequestCustomQuery {


}
