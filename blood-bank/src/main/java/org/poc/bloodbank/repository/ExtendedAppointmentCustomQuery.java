package org.poc.bloodbank.repository;

import com.google.gson.JsonObject;
import org.poc.bloodbank.entity.model.ExtendedAppointment;

import java.util.List;

public interface ExtendedAppointmentCustomQuery {

    List<ExtendedAppointment> findByQuery(JsonObject searchObject);

    Long findCountByQuery(JsonObject searchObject);


}
