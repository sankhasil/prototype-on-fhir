/**
 * 
 */
package org.poc.bloodbank.enums;

/**
 * @author Sankha
 *
 */
public enum DonorSearchEnum {

	NAME("name"),DONORCATEGORY("donorCategory"),DONORNO("donornno"),MOBILE("mobile"),
	DATEOFBIRTH("birthdate"),IDENTIFICATION("identity"),GENDER("gender"),
	TYPE("type"),VALUE("value"),OFFSET("offset"),LIMIT("limit"),REDG_DATE("registrationDate"),
	REDG_STARTDATE("start"),REDG_ENDDATE("end"),COUNTRY("country"),BLOODGROUP("bloodGroup"),
	CITY("city"),UNIT("unit"),UNITID("unitId"),STATE("state"),PINCODE("pincode"),ADDRESS("address"),USE("use");
	
String value;

public String getValue() {
	return value;
}

private DonorSearchEnum(String value) {
	this.value = value;
}
}
