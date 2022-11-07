package org.poc.bloodbank.enums;

public enum ServiceRequestSearchEnum {

	STATUS("status"), BUSINESS_STATUS("businessStatus"), ANESTHESIA_TYPE("anesthesiaType"), LATERALITY("laterality"),NAME("name"),
	OPERATION_TYPE("operationType"), DATE_OF_PROCEDURE("dateOfProcedure"), DIAGNOSIS("diagnosis"),START_DATE("start"),END_DATE("end"),
	ICU_RESERVATION("icuReservation"), OFFSET("offset"), LIMIT("limit"), CREATED_DATE("createdDate"),UNIT_CODE("unitCode"),MRNNO("mrnno"),PERFORMER("performer"),PROCEDURE("procedure"),
	SORT_BY("sortBy"),LOCATION_CODE("locationCode");

	String value;

	public String getValue() {
		return value;
	}

	private ServiceRequestSearchEnum(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value;
	}

}
