/**
 *
 */
package org.poc.bloodbank.repository;

import com.google.gson.JsonObject;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;

import java.util.List;

/**
 * @author Sankha
 */
public interface ExtendedServiceRequestCustomQuery {
    Long findCountByQuery(JsonObject searchObject);

    List<ExtendedServiceRequest> findByQuery(JsonObject jObject);

    ExtendedServiceRequest findLast();

    ExtendedServiceRequest findLastForPrefix(String unitPrefix);


}
