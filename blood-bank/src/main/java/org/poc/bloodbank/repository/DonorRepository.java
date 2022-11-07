package org.poc.bloodbank.repository;



import org.poc.bloodbank.entity.model.Donor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonorRepository extends MongoRepository<Donor, String>, DonorCustomQuery {

}
