/**
 *
 */
package org.poc.bloodbank.controller;

import com.google.gson.JsonObject;
import com.mongodb.MongoWriteException;
import org.poc.bloodbank.entity.model.DonorVisit;
import org.poc.bloodbank.exception.DonorNoGenerationException;
import org.poc.bloodbank.exception.DuplicateException;
import org.poc.bloodbank.exception.RuleEngineException;
import org.poc.bloodbank.rest.model.DonorVisitPatchRequestBody;
import org.poc.bloodbank.rest.model.ResourceListResponseBody;
import org.poc.bloodbank.service.DonorVisitService;
import org.poc.core.base.rest.controller.MongoRestController;
import org.poc.core.exceptions.DataException;
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
@RequestMapping(value = {"/donorvisit", "/DonorVisit", "/DONORVISIT"})
@CrossOrigin
public class DonorVisitController extends MongoRestController<DonorVisit> {

    private DonorVisitService donorVisitService;

    @Autowired
    public DonorVisitController(DonorVisitService donorVisitService) {
        super(donorVisitService);
        this.donorVisitService = donorVisitService;

    }


    @PostMapping(value = {"/advanceSearch", "/advancesearch"})
    public List<DonorVisit> advancedSearch(@RequestBody JsonObject jObject) {
        return donorVisitService.search(jObject);
    }


    @PostMapping(value = {"/advancedSearch", "/advancedsearch"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceListResponseBody> searchPatientWithCount(@RequestBody JsonObject searchRequest) {
        List<DonorVisit> searchedList = donorVisitService.search(searchRequest);
        ResourceListResponseBody searchResponse = new ResourceListResponseBody();
        searchResponse.setListOfResources(searchedList);
        searchResponse.setTotalCount(donorVisitService.getCountBySearch(searchRequest));
        return ResponseEntity.status(HttpStatus.OK).body(searchResponse);

    }


    @PatchMapping(value = {"/patch/{id}", "/Patch/{id}"}, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> patchEncounterById(@PathVariable(value = "id", required = true) String id, @RequestBody DonorVisitPatchRequestBody patchObject) {

        DonorVisit donorVisitUpdated = donorVisitService.patchByPatchObject(patchObject, id);
        JsonObject response = new JsonObject();
        response.addProperty("status", HttpStatus.OK.toString());
        response.addProperty("message", "Donor Visit " + donorVisitUpdated.getDonorVisitNumber() + " Updated");

        return ResponseEntity.ok(response.toString());
    }

    // Convert a predefined exception to an HTTP Status code
    @ExceptionHandler({DuplicateException.class, DonorNoGenerationException.class})
    public ResponseEntity<String> conflict(DuplicateException de) {
        // Nothing to do
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", HttpStatus.CONFLICT.toString());
        responseObject.addProperty("message", de.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObject.toString());
    }


    @ExceptionHandler({DataException.class, MongoWriteException.class})
    public ResponseEntity<String> dataServiceException(DataException de) {
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", HttpStatus.PRECONDITION_FAILED.toString());
        responseObject.addProperty("message", "Error: " + de.getMessage() + " Cause: " + de.getCause().getMessage());
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(responseObject.toString());

    }

    // Specify name of a specific view that will be used to display the error:
    @ExceptionHandler({RuleEngineException.class})
    public ResponseEntity<String> RuleEngineError(RuleEngineException re) {
        // Nothing to do. Returns the logical view name of an error page, passed
        // to the view-resolver(s) in usual way.
        // Note that the exception is NOT available to this view (it is not added
        // to the model) but see "Extending ExceptionHandlerExceptionResolver"
        // below.
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", HttpStatus.PRECONDITION_FAILED.toString());
        responseObject.addProperty("message", re.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseObject.toString());
    }

}
