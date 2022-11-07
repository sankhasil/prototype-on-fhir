package org.poc.bloodbank.enums;

public enum AuditTrailEnum {
    PATIENTNAME("patientTest"),DOCTORNAME("doctorTest"),UPDATED_ACTION("Updated successfully"),CREATED_ACTION("Created successfully"),UPDATED_RESPONSE("Updated"),CREATED_RESPONSE("Created"),
    RESPONSE_CODE("201");
    String value;


    public String getValue() {
        return value;
    }

    private AuditTrailEnum(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return value;
    }
}
