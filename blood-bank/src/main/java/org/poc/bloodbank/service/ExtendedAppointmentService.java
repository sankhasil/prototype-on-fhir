package org.poc.bloodbank.service;

import com.google.gson.JsonObject;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.Reference;
import org.poc.bloodbank.Async.IntegrationChannel;
import org.poc.bloodbank.entity.model.ExtendedAppointment;
import org.poc.bloodbank.repository.ExtendedAppointmentRepository;
import org.poc.core.base.rest.util.HttpUtils;
import org.poc.core.base.util.GeneralFhirUtility;
import org.poc.core.constants.HttpHeaders;
import org.poc.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtendedAppointmentService extends BaseService<ExtendedAppointment> {
    Logger appointmentLogger = LoggerFactory.getLogger(ExtendedAppointmentService.class);

    @Autowired
    IntegrationChannel integrationChannel;

    public static final String SLOT_BUSY = "BUSY";
    public static final String SLOT_FREE = "FREE";
    public static final String SLOT_FREEZE = "FREEZE";

    ExtendedAppointmentRepository extendedAppointmentRepository;

    @Autowired
    public ExtendedAppointmentService(ExtendedAppointmentRepository extendedAppointmentRepository,
                                      MongoTemplate mongoTemplate) {
        super(extendedAppointmentRepository, mongoTemplate);
        this.extendedAppointmentRepository = extendedAppointmentRepository;
    }

    public ExtendedAppointment findByAppointmentId(String appointmentId) {
        return extendedAppointmentRepository.findById(appointmentId).orElse(null);

    }

    @Override
    public ExtendedAppointment update(ExtendedAppointment objectTopatch, String id) {

        switch (objectTopatch.getStatus()) {
            case NOSHOW:
            case CANCELLED:
                changeSlotStatus(objectTopatch, SLOT_FREE, false);
                break;
            case ARRIVED:
            case BOOKED:
                changeSlotStatus(objectTopatch, SLOT_BUSY, false);
                break;
            case PENDING:
                changeSlotStatus(objectTopatch, SLOT_FREEZE, false);
                break;
            default:
                break;
        }
        ExtendedAppointment updatedAppointment = super.update(objectTopatch, id);
        try {
//			integrationChannel.sendResourceToElastic(updatedAppointment, true);
        } catch (Exception e) {
            appointmentLogger.error("Error in update appointment {}", e.getMessage());
        }
        return updatedAppointment;
    }

    @Override
    public ExtendedAppointment create(ExtendedAppointment object) {
        ExtendedAppointment extendedAppointment;
        changeSlotStatus(object, SLOT_BUSY, true);

        synchronized (object) {
            extendedAppointment = super.create(object);
        }
        try {
//			integrationChannel.sendResourceToElastic(extendedAppointment, false);
        } catch (Exception e) {
            appointmentLogger.error("Error in create appointment {}", e.getMessage());
        }
        return extendedAppointment;
    }


    @Override
    public List<ExtendedAppointment> search(JsonObject object) {

        object.addProperty("unitCode", HttpUtils.getHeader(HttpHeaders.UNIT_CODE));

        if (object != null)
            return extendedAppointmentRepository.findByQuery(object);
        else
            return null;

    }

    public Long getCountBySearch(JsonObject object) {

        object.addProperty("unitCode", HttpUtils.getHeader(HttpHeaders.UNIT_CODE));

        return object != null ? extendedAppointmentRepository.findCountByQuery(object) : 0;

    }

    public ExtendedAppointment rescheduleAppointment(ExtendedAppointment appointment, String oldAppointmentId) {
        appointmentLogger.info("Appointment to cancel ID " + oldAppointmentId);
        ExtendedAppointment appointmentToCancel = extendedAppointmentRepository.findById(oldAppointmentId).get();
        if (null != appointmentToCancel && !appointmentToCancel.isEmpty()) {
            appointmentToCancel.setStatus(Appointment.AppointmentStatus.CANCELLED);
            changeSlotStatus(appointmentToCancel, SLOT_FREE, true);
            save(appointmentToCancel);
            appointment.addSupportingInformation(
                    GeneralFhirUtility.createDstu3Reference("appointment", appointmentToCancel.getId(), "ID",
                            appointmentToCancel.getIdentifierFirstRep().getValue(), "", appointmentToCancel));
        }
        ExtendedAppointment newAppointmentCreated = create(appointment);
        return newAppointmentCreated;
    }

    private void changeSlotStatus(ExtendedAppointment appointment, String status, boolean overbookedFlag) {
        if (appointment.getSlot().size() > 0) {
            for (Reference reference : appointment.getSlot()) {
                try {
                    JsonObject slotDataRequestBody = new JsonObject();
                    slotDataRequestBody.addProperty("status", status);
                    slotDataRequestBody.addProperty("businessStatus", status);
                    slotDataRequestBody.addProperty("overbooked", overbookedFlag);
                    integrationChannel.sendSlotDataToScheduler(reference.getId(), slotDataRequestBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
