package org.poc.bloodbank.entity.model;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Property;


import java.util.List;

@ResourceDef(name = "ExtendedAppointment", profile = "http://hl7.org/fhir/Profile/ExtendedAppointment")
public class ExtendedAppointment extends Appointment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StringType getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(StringType businessStatus) {
		this.businessStatus = businessStatus;
	}

	public DateTimeType getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(DateTimeType checkInTime) {
		this.checkInTime = checkInTime;
	}

	@JsonBackReference
	@Child(name = "businessStatus", type = {
			StringType.class }, order = 20, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType businessStatus;

	@JsonBackReference
	@Child(name = "checkInTime", type = {
			DateTimeType.class }, order = 21, min = 0, max = 1, modifier = false, summary = false)
	@Description(shortDefinition = "", formalDefinition = "")
	protected DateTimeType checkInTime;

	public StringType getTRN() {
		return TRN;
	}

	public void setTRN(StringType TRN) {
		this.TRN = TRN;
	}

	@JsonBackReference
	@Child(name = "TRN", type = { StringType.class }, order = 22, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType TRN;

	public StringType getRemarks() {
		return remarks;
	}

	public void setRemarks(StringType remarks) {
		this.remarks = remarks;
	}

	@JsonBackReference
	@Child(name = "remarks", type = {
			StringType.class }, order = 23, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType remarks;

	@Child(name = "extAppointmentparticipant", type = {}, order=24, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
	@Description(shortDefinition="Participants involved in appointment", formalDefinition="List of participants involved in the appointment." )
	protected List<ExtendedAppointmentParticipantComponent> extAppointmentparticipant;

	public List<ExtendedAppointmentParticipantComponent> getExtAppointmentparticipant() {
		return extAppointmentparticipant;
	}

	public void setExtAppointmentparticipant(List<ExtendedAppointmentParticipantComponent> extAppointmentparticipant) {
		this.extAppointmentparticipant = extAppointmentparticipant;
	}

	@Block
	public static class ExtendedAppointmentParticipantComponent extends AppointmentParticipantComponent{

		private static final long serialVersionUID = -1939292177L;
		
		//TODO: Add doctors/participants Reference -> MongoID
		//TODO: Add doctors/participants Code -> MasterCode

		@Child(name = "startTime", type = {InstantType.class}, order=1, min=0, max=1, modifier=false, summary=true)
		@Description(shortDefinition="When appointment is to take place", formalDefinition="Date/Time that the appointment is to take place." )
		protected InstantType startTime;

		@Child(name = "endTime", type = {InstantType.class}, order=2, min=0, max=1, modifier=false, summary=true)
		@Description(shortDefinition="When appointment is to take place", formalDefinition="Date/Time that the appointment is to take place." )
		protected InstantType endTime;

		public InstantType getStartTime() {
			return startTime;
		}

		public void setStartTime(InstantType startTime) {
			this.startTime = startTime;
		}

		public InstantType getEndTime() {
			return endTime;
		}

		public void setEndTime(InstantType endTime) {
			this.endTime = endTime;
		}

		public ExtendedAppointmentParticipantComponent() {
			super();
		}


		protected void listChildren(List<Property> children) {
			super.listChildren(children);
			children.add(new Property("startTime", "instant", "Date/Time that the appointment is to take place.", 0, 1, startTime));
			children.add(new Property("endTime", "instant", "Date/Time that the appointment is to take place.", 0, 1, endTime));

		}



	}


}
