package org.poc.bloodbank.enums;

public enum AppointmentSearchEnum {

    MRNNO("mrnno"),DEPARTMENT_ID("departmentId"),AP_STARTDATE("start"),AP_ENDDATE("end"),DOCTOR("doctor"),MOBILE("mobileNumber"),PRIMARY_ID("primaryDocNo"),UNIT_CODE("unitCode"),
    CLINIC_NAME("clinicname"),UNIT_ID("unitId"),CLINIC_ID("clinicId"),APPOINTMENT_DATE("appointmentDate"),STATUS("status"),NAME("name"),OFFSET("offset"),LIMIT("limit");
    String value;

    public String getValue() {
	return value;
    }

    private AppointmentSearchEnum(String value) {
	this.value = value;
    }
    @Override
    public String toString() {
	// TODO Auto-generated method stub
	return value;
    }
}
