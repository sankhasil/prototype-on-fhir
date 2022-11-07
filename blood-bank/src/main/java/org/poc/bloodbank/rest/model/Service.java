/**
 * 
 */
package org.poc.bloodbank.rest.model;

/**
 * @author Sankha
 *
 */
public class Service extends BaseItem {
	
	private String groupCode,groupDesc;

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	@Override
	public String toString() {
		return "Service [groupCode=" + groupCode + ", groupDesc=" + groupDesc + "]";
	}
	
	
	

}
