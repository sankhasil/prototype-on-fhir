/**
 * 
 */
package org.poc.core.model;

import java.io.Serializable;

/**
 * @author Sankha
 *
 */
public class ResourceFailureRecovery implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8040418008963975628L;
	private String id;
	private String resourceName, action, reason,resourceType;
	private Long failureTimeStamp;
	private boolean isFhir;
	
	public ResourceFailureRecovery() {
	}
	public ResourceFailureRecovery(String id, String resourceName, String action, String reason, Long failureTimeStamp) {
		this.id = id;
		this.resourceName = resourceName;
		this.action = action;
		this.reason = reason;
		this.failureTimeStamp = failureTimeStamp;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the resourceName
	 */
	public String getResourceName() {
		return resourceName;
	}
	/**
	 * @param resourceName the resourceName to set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the failureTimeStamp
	 */
	public Long getFailureTimeStamp() {
		return failureTimeStamp;
	}
	/**
	 * @param failureTimeStamp the failureTimeStamp to set
	 */
	public void setFailureTimeStamp(Long failureTimeStamp) {
		this.failureTimeStamp = failureTimeStamp;
	}
	/**
	 * @return the resourceType
	 */
	public String getResourceType() {
		return resourceType;
	}
	/**
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	/**
	 * @return the isFhir
	 */
	public boolean isFhir() {
		return isFhir;
	}
	/**
	 * @param isFhir the isFhir to set
	 */
	public void setFhir(boolean isFhir) {
		this.isFhir = isFhir;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceFailureRecovery other = (ResourceFailureRecovery) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
