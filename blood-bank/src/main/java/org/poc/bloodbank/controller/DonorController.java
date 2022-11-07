/**
 *
 */
package org.poc.bloodbank.controller;

import com.google.gson.JsonObject;
import com.mongodb.MongoWriteException;
import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.exception.DonorNoGenerationException;
import org.poc.bloodbank.exception.DuplicateException;
import org.poc.bloodbank.exception.RuleEngineException;
import org.poc.bloodbank.rest.model.ResourceListResponseBody;
import org.poc.bloodbank.service.DonorService;
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
@RequestMapping(value = {"/donor", "/Donor", "/DONOR"})
@CrossOrigin
public class DonorController extends MongoRestController<Donor> {

    private DonorService donorService;

    @Autowired
    public DonorController(DonorService extendedPatientService) {
        super(extendedPatientService);
        this.donorService = extendedPatientService;

    }

    @PostMapping(value = {"/page"}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Donor> findByPage(@RequestParam int start, @RequestParam int limit) {

        return donorService.findByPage(start, limit);
    }

    @PostMapping(value = {"/advanceSearch", "/advancesearch"})
    public List<Donor> advancedSearch(@RequestBody JsonObject jObject) {
        return donorService.search(jObject);
    }


    @PostMapping(value = {"/advancedSearch", "/advancedsearch"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResourceListResponseBody> searchPatientWithCount(@RequestBody JsonObject searchRequest) {
        List<Donor> searchedList = donorService.search(searchRequest);
        ResourceListResponseBody searchResponse = new ResourceListResponseBody();
        searchResponse.setListOfResources(searchedList);
        searchResponse.setTotalCount(donorService.getCountBySearch(searchRequest));
        return ResponseEntity.status(HttpStatus.OK).body(searchResponse);

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

//	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "MongoDB Database Error")
//	// Specify name of a specific view that will be used to display the error:
//	@ExceptionHandler({ SQLException.class, DataAccessException.class })
//	public String databaseError() {
//		// Nothing to do. Returns the logical view name of an error page, passed
//		// to the view-resolver(s) in usual way.
//		// Note that the exception is NOT available to this view (it is not added
//		// to the model) but see "Extending ExceptionHandlerExceptionResolver"
//		// below.
//		return "MongoDB Error";
//	}

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
