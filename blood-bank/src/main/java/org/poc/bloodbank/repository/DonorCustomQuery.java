package org.poc.bloodbank.repository;

import com.google.gson.JsonObject;
import org.poc.bloodbank.entity.model.Donor;

import java.util.List;

public interface DonorCustomQuery {

    List<Donor> findAll(int skip, int limit);

    Long findCountByQuery(JsonObject searchObject);

    List<Donor> findByQuery(JsonObject jObject);

    Donor findLast();

    Donor findLastForUnit(String unitPrefix);


}
