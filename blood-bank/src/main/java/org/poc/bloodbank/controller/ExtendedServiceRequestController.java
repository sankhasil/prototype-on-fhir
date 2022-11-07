/**
 *
 */
package org.poc.bloodbank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import org.poc.bloodbank.entity.model.ExtendedAppointment;
import org.poc.bloodbank.entity.model.ExtendedProcedure;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;
import org.poc.bloodbank.exception.AppointmentCreationException;
import org.poc.bloodbank.exception.ServiceRequestCreationException;
import org.poc.bloodbank.rest.model.ResourceListResponseBody;
import org.poc.bloodbank.rest.model.ServiceRequestFormBody;
import org.poc.bloodbank.service.ExtendedProcedureService;
import org.poc.bloodbank.service.ExtendedServiceRequestService;
import org.poc.core.base.rest.controller.MongoRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Sankha
 *
 */

@RestController
@RequestMapping({"/extendedServiceRequest", "/ExtendedServiceRequest"})
@CrossOrigin
public class ExtendedServiceRequestController extends MongoRestController<ExtendedServiceRequest> {

    Logger extendedServiceRequestLogger = LoggerFactory.getLogger(ExtendedServiceRequestController.class);
    @Autowired
    ExtendedServiceRequestService extendedServiceRequestService;

    @Autowired
    ExtendedProcedureService extendedProcedureService;

    @Autowired
    public ExtendedServiceRequestController(ExtendedServiceRequestService extendedServiceRequestService) {
        super(extendedServiceRequestService);
        extendedServiceRequestLogger.info("Controller Initiated");
        this.extendedServiceRequestService = extendedServiceRequestService;
    }

    @CrossOrigin
    @GetMapping({"/ping"})
    public ResponseEntity<String> ping() {
        extendedServiceRequestLogger.info("Pinging Pong");
        return ResponseEntity.ok("Pong");
    }

    @CrossOrigin
    @PostMapping(value = {"/appointment/reschedule/{servcieRequestId}",
            "/Appointment/Reschedule/{servcieRequestId}"}, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> rescheduleAppointmentForServiceRequest(@PathVariable String servcieRequestId, @RequestParam("oldAppointmentId") String oldAppointmentId,
                                                                         @RequestBody ExtendedAppointment newAppointment)
            throws JsonProcessingException, AppointmentCreationException {

        StringBuilder responseBuilder = new StringBuilder();
        extendedServiceRequestService.rescheduleServiceRequestById(servcieRequestId, newAppointment, oldAppointmentId);
        //FIXME : Work on service code to call the reschedule
        return ResponseEntity.status(HttpStatus.OK).body(responseBuilder.toString());
    }

    @CrossOrigin
    @PostMapping(value = {"/save"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ExtendedServiceRequest serviceRequestCreation(@RequestBody ServiceRequestFormBody serviceRequest)
            throws JsonProcessingException {
        return extendedServiceRequestService.create(serviceRequest);
    }

    @PostMapping(value = {"/list"}, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public List<ExtendedServiceRequest> createForList(@RequestBody List<ServiceRequestFormBody> serviceRequestBody) {
        return extendedServiceRequestService.createServiceRequestList(serviceRequestBody);
    }

    @Override
    @PostMapping(value = {"/", ""}, produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ExtendedServiceRequest create(@RequestBody ExtendedServiceRequest serviceRequest) {
        return extendedServiceRequestService.createServiceRequest(serviceRequest);
    }

    @CrossOrigin
    @PostMapping(value = {"/search"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public List<ExtendedServiceRequest> getServiceRequestBySearchBody(@RequestBody JsonObject serviceRequestObject) {
        return extendedServiceRequestService.findServiceRequestBySearchObject(serviceRequestObject);
    }

    @CrossOrigin
    @PostMapping(value = {"/advancedSearch", "/advancedsearch"}, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceListResponseBody> getServiceRequestBySearchBodyWithPagination(
            @RequestBody JsonObject serviceRequestObject) {
        List<ExtendedServiceRequest> searchedList = extendedServiceRequestService
                .findServiceRequestBySearchObject(serviceRequestObject);
        ResourceListResponseBody searchResponse = new ResourceListResponseBody();
        searchResponse.setListOfResources(searchedList);
        searchResponse.setTotalCount(
                extendedServiceRequestService.findServiceRequestCountBySearchObject(serviceRequestObject));
        return ResponseEntity.status(HttpStatus.OK).body(searchResponse);

    }

    @CrossOrigin
    @PatchMapping(value = {"/update/{id}"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ExtendedServiceRequest updateStatusById(@PathVariable String id,
                                                   @RequestBody JsonObject serviceRequestBody) {
        return extendedServiceRequestService.patchServiceRequest(id, serviceRequestBody);
    }

    @ExceptionHandler({ServiceRequestCreationException.class})
    public ResponseEntity<String> createServiceRequestException(ServiceRequestCreationException de) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", HttpStatus.BAD_REQUEST.toString());
        responseObject.addProperty("message", "Error: " + de.getMessage() + " Cause: " + de.getCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject.toString());

    }

    // FIXME: Why separte controller is not used? I already see the controller is
    // created.
    @GetMapping(value = {"{identifier}/procedure/", ""}, produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public List<ExtendedProcedure> getProcedureRequest(@PathVariable("identifier") String id) {
        return extendedProcedureService.getProcedureForServiceRequest(id);
    }
}
