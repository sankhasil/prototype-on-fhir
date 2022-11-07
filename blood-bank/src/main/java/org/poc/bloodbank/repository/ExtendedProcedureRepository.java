package org.poc.bloodbank.repository;

import org.poc.bloodbank.entity.model.ExtendedProcedure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtendedProcedureRepository extends MongoRepository<ExtendedProcedure, String> {
    public List<ExtendedProcedure> getByBasedOn_Identifier_Value(String id);
}
