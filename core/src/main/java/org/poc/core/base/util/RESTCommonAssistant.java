/**
 * 
 */
package org.poc.core.base.util;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *  Utils class for Rest 
 *
 * <p>SD Global Technologies. Copyright &copy; 2019. All rights reserved.</p>
 * @author Karthik
 *
 */
public class RESTCommonAssistant {

	  private static Logger logger = LoggerFactory.getLogger(RESTCommonAssistant.class);
	  /**
	   * Populates list response.
	   *
	   * @param collection a response list to return.
	   * @return a valid {@link ResponseEntity}
	   */
	  public static ResponseEntity buildListResponse(final Collection collection) {
	    if (collection == null) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	          .body("Something went wrong. Please check the logs");
	    }
	    if (CollectionUtils.isEmpty(collection)) {
	      return ResponseEntity.noContent().build();
	    }

	    return ResponseEntity.ok(collection);
	  }

	  /**
	   * Populates object response.
	   *
	   * @param o a response {@link Object} to return.
	   * @return a valid {@link ResponseEntity}
	   */
	  public static ResponseEntity buildObjectResponse(final Object o) {
	    if (o == null) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	          .body("Oops! Something went wrong. Please check the logs.");
	    }
	    ObjectMapper objectMapper = new ObjectMapper();
	    String responseObject = null;
	    try {
	      responseObject = objectMapper.writeValueAsString(o);
	    } catch (JsonProcessingException e) {
	      logger.error(e.getMessage(), e.getCause());
	    }

	    return ResponseEntity.ok(responseObject);
	  }

}
