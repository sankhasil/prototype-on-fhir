package org.poc.bloodbank.enums;

public enum DonorVisitEnum {
	BLOOD_GROUP("bloodGroup"), VISIT_STATUS("visitStatus"), VISIT_TYPE("type"),
	DONOR_GENDER("gender");
	String value;

	public String getValue() {
		return value;
	}

	private DonorVisitEnum(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
