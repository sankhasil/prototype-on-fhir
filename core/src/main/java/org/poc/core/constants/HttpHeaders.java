package org.poc.core.constants;

/**
 * 
 * @author Sankha
 *
 */
public enum HttpHeaders {

	UNIT_ID("x-unitid"),UNIT_CODE("x-unitcode"),ORG_ID("x-orgid"),ORG_CODE("x-orgcode")
	,USER_NAME("x-username"),USER_ID("x-userid"),ACTION("x-action"),ROLE("x-role");

	String value;

	public String getValue() {
		return value;
	}

private HttpHeaders(String value) {
	this.value = value;
}
}
