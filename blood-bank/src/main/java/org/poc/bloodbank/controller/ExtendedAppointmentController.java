package org.poc.bloodbank.controller;

import com.google.gson.JsonObject;
import org.poc.bloodbank.entity.model.ExtendedAppointment;
import org.poc.core.base.rest.model.ResourceListResponseBody;
import org.poc.bloodbank.service.ExtendedAppointmentService;
import org.poc.core.base.rest.controller.MongoRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(value = {"/appointment", "/Appointment", "/ExtendedAppointment", "/extendedAppointment"})
public class ExtendedAppointmentController extends MongoRestController<ExtendedAppointment> {

    private ExtendedAppointmentService extendedAppointmentSevice;


    @Autowired
    public ExtendedAppointmentController(ExtendedAppointmentService extendedAppointmentSevice) {
        super(extendedAppointmentSevice);
        this.extendedAppointmentSevice = extendedAppointmentSevice;
    }

    @PostMapping(value = {"/save", "/Save"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ExtendedAppointment createAppointmentr(@RequestBody ExtendedAppointment appointment) {
        return extendedAppointmentSevice.create(appointment);
    }

    @PostMapping(value = {"/search", "/Search"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ExtendedAppointment> searchEncounter(@RequestBody JsonObject searchRequest) {
        return extendedAppointmentSevice.search(searchRequest);
    }


    @PostMapping(value = {"/advancedSearch", "/advancedsearch"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceListResponseBody> searchAppointmentWithCount(@RequestBody JsonObject searchRequest) {
        List<ExtendedAppointment> searchedList = extendedAppointmentSevice.search(searchRequest);
        ResourceListResponseBody searchResponse = new ResourceListResponseBody();
        searchResponse.setListOfResources(searchedList);
        searchResponse.setTotalCount(extendedAppointmentSevice.getCountBySearch(searchRequest));
        return ResponseEntity.status(HttpStatus.OK).body(searchResponse);

    }


    @PostMapping(value = {"/reschedule/{id}", "/reschedule/{id}/"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ExtendedAppointment reschedule(@RequestBody ExtendedAppointment appointment, @PathVariable("id") String id) {
        return extendedAppointmentSevice.rescheduleAppointment(appointment, id);
    }

    @PatchMapping(value = {"/update/{id}"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ExtendedAppointment update(@RequestBody ExtendedAppointment appointment, @PathVariable("id") String id) {
        return extendedAppointmentSevice.update(appointment, id);
    }

}
