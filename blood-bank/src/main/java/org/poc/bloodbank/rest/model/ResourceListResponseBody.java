/**
 * 
 */
package org.poc.bloodbank.rest.model;

import java.util.List;

import org.hl7.fhir.r4.model.BaseResource;

/**
 * @author Sankha
 *
 */
public class ResourceListResponseBody {

	private List<? extends BaseResource> listOfResources;
	private List<? extends org.hl7.fhir.dstu3.model.BaseResource> resourcesList;
	private Long totalCount;
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public List<? extends BaseResource> getListOfResources() {
		return listOfResources;
	}
	public void setListOfResources(List<? extends BaseResource> listOfResources) {
		this.listOfResources = listOfResources;
	}

	public List<? extends org.hl7.fhir.dstu3.model.BaseResource> getResourcesList() {
		return resourcesList;
	}

	public void setResourcesList(List<? extends org.hl7.fhir.dstu3.model.BaseResource> resourcesList) {
		this.resourcesList = resourcesList;
	}
}
