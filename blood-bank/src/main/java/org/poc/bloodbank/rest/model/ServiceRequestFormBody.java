package org.poc.bloodbank.rest.model;

import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.entity.model.ExtendedAppointment;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;

public class ServiceRequestFormBody {
    private Donor extendedPatient;
    private ExtendedServiceRequest extendedServiceRequest;
    private ExtendedAppointment extendedAppointment;

    public ExtendedAppointment getExtendedAppointment() {
        return extendedAppointment;
    }

    public void setExtendedAppointment(ExtendedAppointment extendedAppointment) {
        this.extendedAppointment = extendedAppointment;
    }

    public ExtendedServiceRequest getExtendedServiceRequest() {
        return extendedServiceRequest;
    }

    public void setExtendedServiceRequest(ExtendedServiceRequest extendedServiceRequest) {
        this.extendedServiceRequest = extendedServiceRequest;
    }

    public Donor getExtendedPatient() {
        return extendedPatient;
    }

    public void setExtendedPatient(Donor extendedPatient) {
        this.extendedPatient = extendedPatient;
    }

}
