package org.poc.bloodbank.repository;

import org.poc.bloodbank.entity.model.ExtendedAppointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExtendedAppointmentRepository extends MongoRepository<ExtendedAppointment, String>, ExtendedAppointmentCustomQuery {

}
