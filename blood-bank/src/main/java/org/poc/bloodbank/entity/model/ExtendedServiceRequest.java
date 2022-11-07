/**
 * 
 */
package org.poc.bloodbank.entity.model;

import java.util.List;

import org.hl7.fhir.dstu3.model.Configuration;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Property;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.RelatedPerson;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.StringType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

/**
 * @author Sankha
 *
 */
@ResourceDef(name = "ExtendedServiceRequest", profile = "http://hl7.org/fhir/Profile/ExtendedServiceRequest")
public class ExtendedServiceRequest extends ServiceRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonBackReference
	@Child(name = "businessStatus", type = {
			StringType.class }, order = 29, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType businessStatus;

	@JsonBackReference
	@Child(name = "anesthesiaType", type = {
			Coding.class }, order = 30, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private Coding anesthesiaType;

	@JsonBackReference
	@Child(name = "approach", type = {
			StringType.class }, order = 31, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType approach;

	@JsonBackReference
	@Child(name = "clinicareOrderId", type = {
			StringType.class }, order = 32, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType clinicareOrderId;

	@JsonBackReference
	@Child(name = "comment", type = {
			StringType.class }, order = 33, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType comment;

	@JsonBackReference
	@Child(name = "dateOfProcedure", type = {
			DecimalType.class }, order = 34, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private DecimalType dateOfProcedure;

	@JsonBackReference
	@Child(name = "diagnosis", type = {
			StringType.class }, order = 35, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType diagnosis;

	@JsonBackReference
	@Child(name = "estimatedTherapyTime", type = {
			StringType.class }, order = 36, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType estimatedTherapyTime;

	@JsonBackReference
	@Child(name = "icuReservation", type = {
			StringType.class }, order = 37, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private StringType icuReservation;

	@JsonBackReference
	@Child(name = "laterality", type = { Coding.class }, order = 38, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private Coding laterality;

	@JsonBackReference
	@Child(name = "operationType", type = {
			Coding.class }, order = 39, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private Coding operationType;

	@JsonBackReference
	@Child(name = "sequenceNumber", type = {
			StringType.class }, order = 40, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private DecimalType sequenceNumber;

	@Child(name = "anestheticList", type = {
			Practitioner.class }, order = 41, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	protected Practitioner anestheticList;

	@Child(name = "appointmentRefList", type = {
			Reference.class }, order = 5, min = 1, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
	@Description(shortDefinition = "The resource this Appointment resource is providing availability information for. These are expected to usually be one of Appointment information", formalDefinition = "The resource this Schedule resource is providing availability information for. These are expected to usually be one of Appointment Information")
	protected Reference appointmentRefList;

	@JsonBackReference
	@Child(name = "ward", type = {
			CodeableConcept.class }, order = 42, min = 0, max = 1, modifier = false, summary = true)
	private CodeableConcept ward;

	@JsonBackReference
	@Child(name = "preparationAndPosition", type = {
			CodeableConcept.class }, order = 43, min = 0, max = 1, modifier = false, summary = true)
	private CodeableConcept preparationAndPosition;

	@JsonBackReference
	@Child(name = "plannedProcedure", type = {
			Coding.class }, order = 45, min = 0, max = 1, modifier = false, summary = true)
	private Coding plannedProcedure;

	@JsonBackReference
	@Child(name = "checkedInDate", type = {
			DecimalType.class }, order = 46, min = 0, max = 1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private DecimalType checkedInDate;

	@Child(name = "bladesOrSuturesList", type = {
			PreferenceCardItems.class }, order = 47, min = 0, max = -1, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private List<PreferenceCardItems> bladesOrSuturesList;

	@Child(name = "instrumentSetsList", type = {}, order = 48, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private List<PreferenceCardItems> instrumentSetsList;

	@Child(name = "equipmentsList", type = {}, order = 49, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private List<PreferenceCardItems> equipmentsList;

	@Child(name = "consumablesList", type = {}, order = 50, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private List<PreferenceCardItems> consumablesList;

	@Child(name = "implantsList", type = {}, order = 51, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private List<PreferenceCardItems> implantsList;

	@Child(name = "servicesList", type = {}, order = 52, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private List<PreferenceCardItems> servicesList;

	@JsonBackReference
	@Child(name = "paymentMode", type = {
			Coding.class }, order = 53, min = 0, max = 1, modifier = false, summary = true)
	private Coding paymentMode;

	@JsonBackReference
	@Child(name = "LMP", type = { StringType.class }, order = 54, min = 0, max = 1, modifier = false, summary = true)
	private StringType LMP;

	@JsonBackReference
	@Child(name = "requirement", type = {
			StringType.class }, order = 55, min = 0, max = 1, modifier = false, summary = true)
	private StringType requirement;

	@JsonBackReference
	@Child(name = "locationCode", type = {
			CodeableConcept.class }, order = 56, min = 0, max = 1, modifier = false, summary = true)
	private CodeableConcept locationCode;

	@Child(name = "otInformationList", type = {}, order = 57, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
	@Description(shortDefinition = "", formalDefinition = "")
	private List<Information> otInformationList;


	@Child(name = "slotStartTime", type = {DecimalType.class}, order=58, min=1, max=1, modifier=false, summary=true)
	@Description(shortDefinition="Date/Time that the slot is to begin", formalDefinition="Date/Time that the slot is to begin." )
	protected DecimalType slotStartTime;

	public DecimalType getSlotStartTime() {
		return slotStartTime;
	}

	public void setSlotStartTime(DecimalType slotStartTime) {
		this.slotStartTime = slotStartTime;
	}

	public List<Information> getOtInformationList() {
		return otInformationList;
	}

	public void setOtInformationList(List<Information> otInformationList) {
		this.otInformationList = otInformationList;
	}


	@Block
	public static class Information extends BackboneElement implements IBaseBackboneElement {

		private static final long serialVersionUID = 8731903698778013531L;
		@Child(name = "code", type = {
				StringType.class }, order = 1, min = 0, max = 1, modifier = false, summary = true)
		protected StringType code;


		@Child(name = "itemId", type = {
				IntegerType.class }, order = 2, min = 0, max = 1, modifier = false, summary = true)
		protected IntegerType itemId;

		@Child(name = "laterality", type = {
				Coding.class }, order = 3, min = 0, max = 1, modifier = false, summary = true)
		@Description(shortDefinition = "", formalDefinition = "")
		protected Coding laterality;

		@Child(name = "isPrimary", type = {
				BooleanType.class }, order = 4, min = 0, max = 1, modifier = false, summary = true)
		protected BooleanType isPrimary;

		@Child(name = "operationType", type = {
				Coding.class }, order = 5, min = 0, max = 1, modifier = false, summary = true)
		@Description(shortDefinition = "", formalDefinition = "")
		protected Coding operationType;


		@Child(name = "estimatedTherapyTime", type = {
				StringType.class }, order = 6, min = 0, max = 1, modifier = false, summary = true)
		@Description(shortDefinition = "", formalDefinition = "")
		protected StringType estimatedTherapyTime;

		@Child(name = "dateOfProcedure", type = {
				DecimalType.class }, order = 7, min = 0, max = 1, modifier = false, summary = true)
		@Description(shortDefinition = "", formalDefinition = "")
		protected DecimalType dateOfProcedure;

		@Child(name = "anesthesiaType", type = {
				Coding.class }, order = 8, min = 0, max = 1, modifier = false, summary = true)
		@Description(shortDefinition = "", formalDefinition = "")
		protected Coding anesthesiaType;

		@JsonBackReference
		@Child(name = "paymentMode", type = {
				Coding.class }, order = 9, min = 0, max = 1, modifier = false, summary = true)
		protected Coding paymentMode;

		@Child(name = "diagnosis", type = {
				StringType.class }, order = 10, min = 0, max = 1, modifier = false, summary = true)
		@Description(shortDefinition = "", formalDefinition = "")
		protected StringType diagnosis;

		@Child(name = "plannedProcedure", type = {
				Coding.class }, order = 11, min = 0, max = 1, modifier = false, summary = true)
		protected Coding plannedProcedure;

		@Child(name = "performer", type = { Practitioner.class, PractitionerRole.class, Organization.class,
				CareTeam.class, HealthcareService.class, Patient.class, Device.class,
				RelatedPerson.class }, order = 12, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
		@Description(shortDefinition = "Requested performer", formalDefinition = "The desired performer for doing the requested service.  For example, the surgeon, dermatopathologist, endoscopist, etc.")
		protected List<Reference> performer;

		@Child(name = "category", type = {
				CodeableConcept.class }, order = 13, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
		@Description(shortDefinition = "Classification of service", formalDefinition = "A code that classifies the service for searching, sorting and display purposes (e.g. \"Surgical Procedure\").")
		@ca.uhn.fhir.model.api.annotation.Binding(valueSet = "http://hl7.org/fhir/ValueSet/servicerequest-category")
		protected List<CodeableConcept> category;

		@Child(name = "approach", type = {
				StringType.class }, order = 14, min = 0, max = 1, modifier = false, summary = true)
		@Description(shortDefinition = "", formalDefinition = "")
		protected StringType approach;

		@Child(name = "LMP", type = {
				StringType.class }, order = 15, min = 0, max = 1, modifier = false, summary = true)
		private StringType LMP;

		@Override
		public Information copy() {
			Information informationItem = new Information();
			copyValues(informationItem);
//	            if (type != null) {
//	            	prfKItem.type = new ArrayList<StringType>();
//	                for (StringType i : type)
//	                	prfKItem.type.add(i.copy());
//	            }
			if (informationItem != null) {
				informationItem.code = code == null ? null : code.copy();
			}

			return informationItem;
		}

		public Information() {
			super();
			// TODO Auto-generated constructor stub
		}
		//code
		public StringType getCodeElement() {
			if (this.code == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create Code");
				}

				if (Configuration.doAutoCreate()) {
					this.code = new StringType();
				}
			}
			return this.code;
		}

		public boolean hasCodeElement() {
			return this.code != null && !this.code.isEmpty();
		}

		public boolean hasCode() {
			return this.code != null && !this.code.isEmpty();
		}

		public Information setCodeElement(StringType value) {
			this.code = value;
			return this;
		}

		public StringType getCode() {
			return this.code == null ? null : this.code;
		}

		public Information setCode(StringType value) {
			if (value == null) {
				this.code = null;
			} else {
				if (this.code == null) {
					this.code = new StringType();
				}

				this.code.setValue(String.valueOf(value));
			}

			return this;
		}
		//itemId
		public IntegerType getItemIdElement() {
			if (this.itemId == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create itemId");
				}

				if (Configuration.doAutoCreate()) {
					this.itemId = new IntegerType();
				}
			}
			return this.itemId;
		}

		public boolean hasItemIdElement() {
			return this.itemId != null && !this.itemId.isEmpty();
		}

		public boolean hasItemId() {
			return this.itemId != null && !this.itemId.isEmpty();
		}

		public Information setItemIdElement(IntegerType value) {
			this.itemId = value;
			return this;
		}

		public IntegerType getItemId() {
			return this.itemId == null ? null : this.itemId;
		}

		public Information setItemId(IntegerType value) {
			if (value == null) {
				this.itemId = null;
			} else {
				if (this.itemId == null) {
					this.itemId = new IntegerType();
				}

				this.itemId.setValue(value.getValue());
			}

			return this;
		}
		
		//isPrimary
		public BooleanType getIsPrimaryElement() {
			if (this.isPrimary == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create laterality");
				}

				if (Configuration.doAutoCreate()) {
					this.isPrimary = new BooleanType();
				}
			}
			return this.isPrimary;
		}

		public boolean hasIsPrimaryElement() {
			return this.isPrimary != null && !this.isPrimary.isEmpty();
		}

		public boolean hasIsPrimary() {
			return this.isPrimary != null && !this.isPrimary.isEmpty();
		}

		public Information setIsPrimaryElement(BooleanType value) {
			this.isPrimary = value;
			return this;
		}

		public BooleanType getIsPrimary() {
			return this.isPrimary == null ? null : this.isPrimary;
		}

		public Information setIsPrimary(BooleanType value) {
			if (value == null) {
				this.isPrimary = null;
			} else {
				if (this.isPrimary == null) {
					this.isPrimary = new BooleanType();
				}

				this.isPrimary.setValue(Boolean.valueOf(value.booleanValue()));
			}

			return this;
		}
		
		//laterality
		public Coding getLateralityElement() {
			if (this.laterality == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create laterality");
				}

				if (Configuration.doAutoCreate()) {
					this.laterality = new Coding();
				}
			}
			return this.laterality;
		}

		public boolean hasLateralityElement() {
			return this.laterality != null && !this.laterality.isEmpty();
		}

		public boolean hasLaterality() {
			return this.laterality != null && !this.laterality.isEmpty();
		}

		public Information setLateralityElement(Coding value) {
			this.laterality = value;
			return this;
		}

		public Coding getLaterality() {
			return this.laterality == null ? null : this.laterality;
		}

		public Information setLaterality(Coding value) {
			if (value == null) {
				this.laterality = null;
			} else {
				if (this.laterality == null) {
					this.laterality = new Coding();
				}

				this.laterality.setCode(value.getCode());
				this.laterality.setDisplay(value.getDisplay());
				this.laterality.setSystem(value.getSystem());
			}

			return this;
		}
		//operationType
		public Coding getOperationTypeElement() {
			if (this.operationType == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create operationType");
				}

				if (Configuration.doAutoCreate()) {
					this.operationType = new Coding();
				}
			}
			return this.operationType;
		}

		public boolean hasOperationTypeElement() {
			return this.operationType != null && !this.operationType.isEmpty();
		}

		public boolean hasOperationType() {
			return this.operationType != null && !this.operationType.isEmpty();
		}

		public Information setOperationTypeElement(Coding value) {
			this.operationType = value;
			return this;
		}

		public Coding getOperationType() {
			return this.operationType == null ? null : this.operationType;
		}

		public Information setOperationType(Coding value) {
			if (value == null) {
				this.operationType = null;
			} else {
				if (this.operationType == null) {
					this.operationType = new Coding();
				}

				this.operationType.setCode(value.getCode());
				this.operationType.setDisplay(value.getDisplay());
				this.operationType.setSystem(value.getSystem());
			}

			return this;
		}
		
		//estimatedTherapyTime
		public StringType getEstimatedTherapyTimeElement() {
			if (this.estimatedTherapyTime == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create EstimatedSurgeryTime");
				}

				if (Configuration.doAutoCreate()) {
					this.estimatedTherapyTime = new StringType();
				}
			}
			return this.estimatedTherapyTime;
		}

		public boolean hasEstimatedTherapyTimeElement() {
			return this.estimatedTherapyTime != null && !this.estimatedTherapyTime.isEmpty();
		}

		public boolean hasEstimatedTherapyTime() {
			return this.estimatedTherapyTime != null && !this.estimatedTherapyTime.isEmpty();
		}

		public Information setEstimatedTherapyTimeElement(StringType value) {
			this.estimatedTherapyTime = value;
			return this;
		}

		public StringType getEstimatedTherapyTime() {
			return this.estimatedTherapyTime == null ? null : this.estimatedTherapyTime;
		}

		public List<Reference> getPerformer() {
			return performer;
		}

		public void setPerformer(List<Reference> performer) {
			this.performer = performer;
		}

		public List<CodeableConcept> getCategory() {
			return category;
		}

		public void setCategory(List<CodeableConcept> category) {
			this.category = category;
		}

		public Information setEstimatedTherapyTime(StringType value) {
			if (value == null) {
				this.estimatedTherapyTime = null;
			} else {
				if (this.estimatedTherapyTime == null) {
					this.estimatedTherapyTime = new StringType();
				}

				this.estimatedTherapyTime.setValue(String.valueOf(value));
			}

			return this;
		}
		//dateOfProcedure
		public DecimalType getDateOfProcedureElement() {
			if (this.dateOfProcedure == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create DateOfProcedure");
				}

				if (Configuration.doAutoCreate()) {
					this.dateOfProcedure = new DecimalType();
				}
			}
			return this.dateOfProcedure;
		}

		public boolean hasDateOfProcedureElement() {
			return this.dateOfProcedure != null && !this.dateOfProcedure.isEmpty();
		}

		public boolean hasDateOfProcedure() {
			return this.dateOfProcedure != null && !this.dateOfProcedure.isEmpty();
		}

		public Information setDateOfProcedureElement(DecimalType value) {
			this.dateOfProcedure = value;
			return this;
		}

		public DecimalType getDateOfProcedure() {
			return this.dateOfProcedure == null ? null : this.dateOfProcedure;
		}

		public Information setDateOfProcedure(DecimalType value) {
			if (value == null) {
				this.dateOfProcedure = null;
			} else {
				if (this.dateOfProcedure == null) {
					this.dateOfProcedure = new DecimalType();
				}

				this.dateOfProcedure.setValue(Long.valueOf(value.asStringValue()));
			}

			return this;
		}
		//anesthesiaType
		public Coding getAnesthesiaTypeElement() {
			if (this.anesthesiaType == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create AnesthesiaType");
				}

				if (Configuration.doAutoCreate()) {
					this.anesthesiaType = new Coding();
				}
			}
			return this.anesthesiaType;
		}

		public boolean hasAnesthesiaTypeElement() {
			return this.anesthesiaType != null && !this.anesthesiaType.isEmpty();
		}

		public boolean hasAnesthesiaType() {
			return this.anesthesiaType != null && !this.anesthesiaType.isEmpty();
		}

		public Information setAnesthesiaTypeElement(Coding value) {
			this.anesthesiaType = value;
			return this;
		}

		public Coding getAnesthesiaType() {
			return this.anesthesiaType == null ? null : this.anesthesiaType;
		}

		public Information setAnesthesiaType(Coding value) {
			if (value == null) {
				this.anesthesiaType = null;
			} else {
				if (this.anesthesiaType == null) {
					this.anesthesiaType = new Coding();
				}

				this.anesthesiaType.setCode(value.getCode());
				this.anesthesiaType.setDisplay(value.getDisplay());
				this.anesthesiaType.setSystem(value.getSystem());
			}

			return this;
		}
		//paymentMode
		public Coding getPaymentModeElement() {
			if (this.paymentMode == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create PaymentMode");
				}

				if (Configuration.doAutoCreate()) {
					this.paymentMode = new Coding();
				}
			}
			return this.paymentMode;
		}

		public boolean hasPaymentModeElement() {
			return this.paymentMode != null && !this.paymentMode.isEmpty();
		}

		public boolean hasPaymentMode() {
			return this.paymentMode != null && !this.paymentMode.isEmpty();
		}

		public Information setPaymentModeElement(Coding value) {
			this.paymentMode = value;
			return this;
		}

		public Coding getPaymentMode() {
			return this.paymentMode == null ? null : this.paymentMode;
		}

		public Information setPaymentMode(Coding value) {
			if (value == null) {
				this.paymentMode = null;
			} else {
				if (this.paymentMode == null) {
					this.paymentMode = new Coding();
				}

				this.paymentMode.setCode(value.getCode());
				this.paymentMode.setDisplay(value.getDisplay());
				this.paymentMode.setSystem(value.getSystem());
			}

			return this;
		}
		//diagnosis
		public StringType getDiagnosisElement() {
			if (this.diagnosis == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create Diagnosis");
				}

				if (Configuration.doAutoCreate()) {
					this.diagnosis = new StringType();
				}
			}
			return this.diagnosis;
		}

		public boolean hasDiagnosisElement() {
			return this.diagnosis != null && !this.diagnosis.isEmpty();
		}

		public boolean hasDiagnosis() {
			return this.diagnosis != null && !this.diagnosis.isEmpty();
		}

		public Information setDiagnosisElement(StringType value) {
			this.diagnosis = value;
			return this;
		}

		public StringType getDiagnosis() {
			return this.diagnosis == null ? null : this.diagnosis;
		}

		public Information setDiagnosis(StringType value) {
			if (value == null) {
				this.diagnosis = null;
			} else {
				if (this.diagnosis == null) {
					this.diagnosis = new StringType();
				}

				this.diagnosis.setValue(String.valueOf(value));
			}

			return this;
		}
		//plannedProcedure
		public Coding getPlannedProcedureElement() {
			if (this.plannedProcedure == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create PlannedProcedure");
				}

				if (Configuration.doAutoCreate()) {
					this.plannedProcedure = new Coding();
				}
			}
			return this.plannedProcedure;
		}

		public boolean hasPlannedProcedureElement() {
			return this.plannedProcedure != null && !this.plannedProcedure.isEmpty();
		}

		public boolean hasPlannedProcedure() {
			return this.plannedProcedure != null && !this.plannedProcedure.isEmpty();
		}

		public Information setPlannedProcedureElement(Coding value) {
			this.plannedProcedure = value;
			return this;
		}

		public Coding getPlannedProcedure() {
			return this.plannedProcedure == null ? null : this.plannedProcedure;
		}

		public Information setPlannedProcedure(Coding value) {
			if (value == null) {
				this.plannedProcedure = null;
			} else {
				if (this.plannedProcedure == null) {
					this.plannedProcedure = new Coding();
				}

				this.plannedProcedure.setCode(value.getCode());
				this.plannedProcedure.setDisplay(value.getDisplay());
				this.plannedProcedure.setSystem(value.getSystem());
			}

			return this;
		}
		//performer
		//TODO: find reference List setter getter example
		//category
		//TODO: find CodeableConcept List setter getter example
		//approach
		public StringType getApproachElement() {
			if (this.approach == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create Approach");
				}

				if (Configuration.doAutoCreate()) {
					this.approach = new StringType();
				}
			}
			return this.approach;
		}

		public boolean hasApproachElement() {
			return this.approach != null && !this.approach.isEmpty();
		}

		public boolean hasApproach() {
			return this.approach != null && !this.approach.isEmpty();
		}

		public Information setApproachElement(StringType value) {
			this.approach = value;
			return this;
		}

		public StringType getApproach() {
			return this.approach == null ? null : this.approach;
		}

		public Information setApproach(StringType value) {
			if (value == null) {
				this.approach = null;
			} else {
				if (this.approach == null) {
					this.approach = new StringType();
				}

				this.approach.setValue(String.valueOf(value));
			}

			return this;
		}
		//LMP
		public StringType getLMPElement() {
			if (this.LMP == null) {
				if (Configuration.errorOnAutoCreate()) {
					throw new Error("Attempt to auto-create LMP");
				}

				if (Configuration.doAutoCreate()) {
					this.LMP = new StringType();
				}
			}
			return this.LMP;
		}

		public boolean hasLMPElement() {
			return this.LMP != null && !this.LMP.isEmpty();
		}

		public boolean hasLMP() {
			return this.LMP != null && !this.LMP.isEmpty();
		}

		public Information setLMPElement(StringType value) {
			this.LMP = value;
			return this;
		}

		public StringType getLMP() {
			return this.LMP == null ? null : this.LMP;
		}

		public Information setLMP(StringType value) {
			if (value == null) {
				this.LMP = null;
			} else {
				if (this.LMP == null) {
					this.LMP = new StringType();
				}

				this.LMP.setValue(String.valueOf(value));
			}

			return this;
		}
		
		
		@Override
		public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
			switch (_hash) {
			case -1718432877:
				/* code */ return new Property("code", "string", "", 0, 1, code);
//			case -1718432899:
//				/* desc */ return new Property("desc", "string", "", 0, 1, desc);
		case -1718432844:
				/* itemId */ return new Property("itemId", "integer", "", 0, 1, itemId);
//			case -1718432833:
//				/* priority */ return new Property("priority", "string", "", 0, 1, priority);
//			case -1718432822:
//				/* type */ return new Property("type", "string", "", 0, 1, type);
//			case -1718432855:
//				/* group */ return new Property("group", "string", "", 0, 1, group);
//			case -1718432866:
//				/* groupName */ return new Property("groupName", "string", "", 0, 1, groupName);
			default:
				return super.getNamedProperty(_hash, _name, _checkValid);
			}

		}

		@Override
		public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
			switch (hash) {
			case -1718432877:
				/* code */ return this.code == null ? new Base[0] : new Base[] { this.code }; // code
//			case -1718432899:
//				/* desc */ return this.desc == null ? new Base[0] : new Base[] { this.desc }; // desc
			case -1718432844:
				/* itemId */ return this.itemId == null ? new Base[0] : new Base[] { this.itemId }; // itemId
//			case -1718432833:
//				/* priority */ return this.priority == null ? new Base[0] : new Base[] { this.priority }; // priority
//			case -1718432822:
//				/* type */ return this.type == null ? new Base[0] : new Base[] { this.type }; // type
//			case -1718432855:
//				/* group */ return this.group == null ? new Base[0] : new Base[] { this.group }; // group
//			case -1718432866:
//				/* groupName */ return this.groupName == null ? new Base[0] : new Base[] { this.groupName }; // groupName
			default:
				return super.getProperty(hash, name, checkValid);
			}

		}

		@Override
		public Base setProperty(int hash, String name, Base value) throws FHIRException {
			switch (hash) {
			case -1718432877: // code
				this.code = castToString(value); // code
//			case -1718432899: // desc
//				this.desc = castToString(value); // desc
			case -1718432844: // itemId
				this.itemId = castToInteger(value); // itemId
//			case -1718432833: // priority
//				this.priority = castToString(value); // priority
//			case -1718432822: // type
//				this.type = castToString(value); // type
//			case -1718432855: // group
//				this.group = castToString(value); // group
//			case -1718432866: // groupName
//				this.groupName = castToString(value); // groupName
			default:
				return super.setProperty(hash, name, value);
			}

		}

		@Override
		public Base setProperty(String name, Base value) throws FHIRException {
			if (name.equals("code")) {
				this.code = castToString(value); // StringType
//			} else if (name.equals("desc")) {
//				this.desc = castToString(value); // StringType
			} else if (name.equals("itemId")) {
				this.itemId = castToInteger(value); // IntegerType
//			} else if (name.equals("priority")) {
//				this.priority = castToString(value); // StringType
//			} else if (name.equals("type")) {
//				this.type = castToString(value); // StringType
//			} else if (name.equals("group")) {
//				this.group = castToString(value); // StringType
//			} else if (name.equals("groupName")) {
//				this.groupName = castToString(value); // StringType
			} else
				return super.setProperty(name, value);
			return value;
		}

		@Override
		public Base makeProperty(int hash, String name) throws FHIRException {
			switch (hash) {
			case -1718432877: // code
				return getCode();
//			case -1718432899: // desc
//				return getDesc();
//			case -1718432844: // itemId
//				return getItemId();
//			case -1718432833: // priority
//				return getPriority();
//			case -1718432822: // type
//				return getType();
//			case -1718432855: // group
//				return getGroup();
//			case -1718432866: // groupName
//				return getGroupName();
			default:
				return super.makeProperty(hash, name);
			}

		}

		@Override
		public String[] getTypesForProperty(int hash, String name) throws FHIRException {
			switch (hash) {
			case -1718432877: // code
			case -1718432899: // desc
			case -1718432833: // priority
			case -1718432822: // type
			case -1718432855: // group
			case -1718432866: // groupName
				return new String[] { "string" };
			case -1718432844: // itemId
				return new String[] { "integer" };
			default:
				return super.getTypesForProperty(hash, name);
			}

		}

		@Override
		public Base addChild(String name) throws FHIRException {
			if (name.equals("code") || name.equals("desc") || name.equals("itemId") || name.equals("quantity")
					|| name.equals("priority") || name.equals("group") || name.equals("groupName")
					|| name.equals("type")) {
				throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
			} else
				return super.addChild(name);
		}

		@Override
		public boolean equalsDeep(Base other_) {
			if (!super.equalsDeep(other_))
				return false;
			if (!(other_ instanceof Information))
				return false;
			Information o = (Information) other_;
			return compareDeep(code, o.code, true); // FIXME: put proper compare logic
		}

		@Override
		public boolean equalsShallow(Base other_) {
			if (!super.equalsShallow(other_))
				return false;
			if (!(other_ instanceof Information))
				return false;
			Information o = (Information) other_;
			return true;
		}



		public String fhirType() {
			return "ExtendedServiceRequest.Information";

		}

		

		public boolean isEmpty() {
			return super.isEmpty() && ( code== null || code.isEmpty()) && (itemId == null || itemId.isEmpty())
					&& (laterality == null || laterality.isEmpty()) && (operationType == null || operationType.isEmpty())
					&& (estimatedTherapyTime == null || estimatedTherapyTime.isEmpty()) && (dateOfProcedure == null || dateOfProcedure.isEmpty())
					&& (anesthesiaType == null || anesthesiaType.isEmpty()) && (paymentMode == null || paymentMode.isEmpty())
					&& (diagnosis == null || diagnosis.isEmpty()) && (plannedProcedure == null || plannedProcedure.isEmpty())
					&& (performer == null || performer.isEmpty()) && (category == null || category.isEmpty())
					&& (approach == null || approach.isEmpty()) && (LMP == null || LMP.isEmpty())
					&& (isPrimary == null || isPrimary.isEmpty());
		}

		protected void listChildren(List<Property> children) {
			super.listChildren(children);
			children.add(new Property("code", "string", "", 0, 1, code));
			children.add(new Property("itemId", "Integer", "", 0, 1, itemId));
			children.add(new Property("laterality", "Coding", "", 0, 1, laterality));
			children.add(new Property("operationType", "string", "", 0, 1, operationType));
			children.add(new Property("estimatedTherapyTime", "string", "", 0, 1, estimatedTherapyTime));
			children.add(new Property("dateOfProcedure", "decimal", "", 0, 1, dateOfProcedure));
			children.add(new Property("anesthesiaType", "Coding", "", 0, 1, anesthesiaType));
			children.add(new Property("paymentMode", "Coding", "", 0, 1, paymentMode));
			children.add(new Property("diagnosis", "string", "", 0, 1, diagnosis));
			children.add(new Property("plannedProcedure", "Coding", "", 0, 1, plannedProcedure));
			children.add(new Property("performer", "", "", 0, 1, performer));
			children.add(new Property("category", "", "", 0, 1, category));
			children.add(new Property("approach", "string", "", 0, 1, approach));
			children.add(new Property("LMP", "string", "", 0, 1, LMP));
			children.add(new Property("isPrimary", "boolean", "", 0, 1, isPrimary));


		}

	}

	@Block
	public static class PreferenceCardItems extends BackboneElement implements IBaseBackboneElement {

		private static final long serialVersionUID = 4456633L;

		@Child(name = "code", type = {
				StringType.class }, order = 8, min = 0, max = 1, modifier = false, summary = true)
		protected StringType code;

		@Child(name = "itemId", type = {
				IntegerType.class }, order = 1, min = 0, max = 1, modifier = false, summary = true)
		protected IntegerType itemId;

		@Child(name = "desc", type = {
				StringType.class }, order = 2, min = 0, max = 1, modifier = false, summary = true)
		protected StringType desc;

		@Child(name = "quantity", type = {
				IntegerType.class }, order = 3, min = 0, max = 1, modifier = false, summary = true)
		protected IntegerType quantity;

		@Child(name = "type", type = {
				StringType.class }, order = 4, min = 0, max = 1, modifier = false, summary = true)
		protected StringType type;

		@Child(name = "priority", type = {
				StringType.class }, order = 5, min = 0, max = 1, modifier = false, summary = true)
		protected StringType priority;

		@Child(name = "group", type = {
				StringType.class }, order = 6, min = 0, max = 1, modifier = false, summary = true)
		protected StringType group;

		@Child(name = "groupName", type = {
				StringType.class }, order = 7, min = 0, max = 1, modifier = false, summary = true)
		protected StringType groupName;

		public IntegerType getItemId() {
			return itemId;
		}

		public void setItemId(IntegerType itemId) {
			this.itemId = itemId;
		}

		public StringType getDesc() {
			return desc;
		}

		public void setDesc(StringType desc) {
			this.desc = desc;
		}

		public IntegerType getQuantity() {
			return quantity;
		}

		public void setQuantity(IntegerType quantity) {
			this.quantity = quantity;
		}

		public StringType getType() {
			return type;
		}

		public void setType(StringType type) {
			this.type = type;
		}

		public StringType getPriority() {
			return priority;
		}

		public void setPriority(StringType priority) {
			this.priority = priority;
		}

		public StringType getGroup() {
			return group;
		}

		public void setGroup(StringType group) {
			this.group = group;
		}

		public StringType getGroupName() {
			return groupName;
		}

		public void setGroupName(StringType groupName) {
			this.groupName = groupName;
		}

		public StringType getCode() {
			return code;
		}

		public void setCode(StringType code) {
			this.code = code;
		}

		@Override
		public PreferenceCardItems copy() {
			PreferenceCardItems prfKItem = new PreferenceCardItems();
			copyValues(prfKItem);
//	            if (type != null) {
//	            	prfKItem.type = new ArrayList<StringType>();
//	                for (StringType i : type)
//	                	prfKItem.type.add(i.copy());
//	            }
			prfKItem.code = code == null ? null : code.copy();

			return prfKItem;
		}

		public PreferenceCardItems() {
			super();
			// TODO Auto-generated constructor stub
		}

		protected void listChildren(List<Property> children) {
			super.listChildren(children);
			children.add(new Property("code", "string", "", 0, 1, code));
			children.add(new Property("desc", "string", "", 0, 1, desc));
			children.add(new Property("itemId", "string", "", 0, 1, itemId));
			children.add(new Property("priority", "string", "", 0, 1, priority));
			children.add(new Property("type", "string", "", 0, 1, type));
			children.add(new Property("group", "string", "", 0, 1, group));
			children.add(new Property("groupName", "string", "", 0, 1, groupName));
		}

		@Override
		public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
			switch (_hash) {
			case -1718432877:
				/* code */ return new Property("code", "string", "", 0, 1, code);
			case -1718432899:
				/* desc */ return new Property("desc", "string", "", 0, 1, desc);
			case -1718432844:
				/* itemId */ return new Property("itemId", "string", "", 0, 1, itemId);
			case -1718432833:
				/* priority */ return new Property("priority", "string", "", 0, 1, priority);
			case -1718432822:
				/* type */ return new Property("type", "string", "", 0, 1, type);
			case -1718432855:
				/* group */ return new Property("group", "string", "", 0, 1, group);
			case -1718432866:
				/* groupName */ return new Property("groupName", "string", "", 0, 1, groupName);
			default:
				return super.getNamedProperty(_hash, _name, _checkValid);
			}

		}

		@Override
		public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
			switch (hash) {
			case -1718432877:
				/* code */ return this.code == null ? new Base[0] : new Base[] { this.code }; // code
			case -1718432899:
				/* desc */ return this.desc == null ? new Base[0] : new Base[] { this.desc }; // desc
			case -1718432844:
				/* itemId */ return this.itemId == null ? new Base[0] : new Base[] { this.itemId }; // itemId
			case -1718432833:
				/* priority */ return this.priority == null ? new Base[0] : new Base[] { this.priority }; // priority
			case -1718432822:
				/* type */ return this.type == null ? new Base[0] : new Base[] { this.type }; // type
			case -1718432855:
				/* group */ return this.group == null ? new Base[0] : new Base[] { this.group }; // group
			case -1718432866:
				/* groupName */ return this.groupName == null ? new Base[0] : new Base[] { this.groupName }; // groupName
			default:
				return super.getProperty(hash, name, checkValid);
			}

		}

		@Override
		public Base setProperty(int hash, String name, Base value) throws FHIRException {
			switch (hash) {
			case -1718432877: // code
				this.code = castToString(value); // code
			case -1718432899: // desc
				this.desc = castToString(value); // desc
			case -1718432844: // itemId
				this.itemId = castToInteger(value); // itemId
			case -1718432833: // priority
				this.priority = castToString(value); // priority
			case -1718432822: // type
				this.type = castToString(value); // type
			case -1718432855: // group
				this.group = castToString(value); // group
			case -1718432866: // groupName
				this.groupName = castToString(value); // groupName
			default:
				return super.setProperty(hash, name, value);
			}

		}

		@Override
		public Base setProperty(String name, Base value) throws FHIRException {
			if (name.equals("code")) {
				this.code = castToString(value); // StringType
			} else if (name.equals("desc")) {
				this.desc = castToString(value); // StringType
			} else if (name.equals("itemId")) {
				this.itemId = castToInteger(value); // IntegerType
			} else if (name.equals("priority")) {
				this.priority = castToString(value); // StringType
			} else if (name.equals("type")) {
				this.type = castToString(value); // StringType
			} else if (name.equals("group")) {
				this.group = castToString(value); // StringType
			} else if (name.equals("groupName")) {
				this.groupName = castToString(value); // StringType
			} else
				return super.setProperty(name, value);
			return value;
		}

		@Override
		public Base makeProperty(int hash, String name) throws FHIRException {
			switch (hash) {
			case -1718432877: // code
				return getCode();
			case -1718432899: // desc
				return getDesc();
			case -1718432844: // itemId
				return getItemId();
			case -1718432833: // priority
				return getPriority();
			case -1718432822: // type
				return getType();
			case -1718432855: // group
				return getGroup();
			case -1718432866: // groupName
				return getGroupName();
			default:
				return super.makeProperty(hash, name);
			}

		}

		@Override
		public String[] getTypesForProperty(int hash, String name) throws FHIRException {
			switch (hash) {
			case -1718432877: // code
			case -1718432899: // desc
			case -1718432833: // priority
			case -1718432822: // type
			case -1718432855: // group
			case -1718432866: // groupName
				return new String[] { "string" };
			case -1718432844: // itemId
				return new String[] { "integer" };
			default:
				return super.getTypesForProperty(hash, name);
			}

		}

		@Override
		public Base addChild(String name) throws FHIRException {
			if (name.equals("code") || name.equals("desc") || name.equals("itemId") || name.equals("quantity")
					|| name.equals("priority") || name.equals("group") || name.equals("groupName")
					|| name.equals("type")) {
				throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
			} else
				return super.addChild(name);
		}

		@Override
		public boolean equalsDeep(Base other_) {
			if (!super.equalsDeep(other_))
				return false;
			if (!(other_ instanceof PreferenceCardItems))
				return false;
			PreferenceCardItems o = (PreferenceCardItems) other_;
			return compareDeep(code, o.code, true) && compareDeep(desc, o.desc, true)
					&& compareDeep(quantity, o.quantity, true); // FIXME: put proper compare logic
		}

		@Override
		public boolean equalsShallow(Base other_) {
			if (!super.equalsShallow(other_))
				return false;
			if (!(other_ instanceof PreferenceCardItems))
				return false;
			PreferenceCardItems o = (PreferenceCardItems) other_;
			return true;
		}

		public boolean isEmpty() {
			return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, desc, type, quantity, itemId, priority,
					group, groupName);
		}

		public String fhirType() {
			return "ExtendedServiceRequest.PreferenceKartItems";

		}

	}

	public CodeableConcept getPreparationAndPosition() {
		return preparationAndPosition;
	}

	public void setPreparationAndPosition(CodeableConcept preparationAndPosition) {
		this.preparationAndPosition = preparationAndPosition;
	}

	public Coding getPlannedProcedure() {
		return plannedProcedure;
	}

	public void setPlannedProcedure(Coding plannedProcedure) {
		this.plannedProcedure = plannedProcedure;
	}

	public List<PreferenceCardItems> getBladesOrSuturesList() {
		return bladesOrSuturesList;
	}

	public void setBladesOrSuturesList(List<PreferenceCardItems> bladesOrSuturesList) {
		this.bladesOrSuturesList = bladesOrSuturesList;
	}

	public List<PreferenceCardItems> getInstrumentSetsList() {
		return instrumentSetsList;
	}

	public void setInstrumentSetsList(List<PreferenceCardItems> instrumentSetsList) {
		this.instrumentSetsList = instrumentSetsList;
	}

	public List<PreferenceCardItems> getEquipmentsList() {
		return equipmentsList;
	}

	public void setEquipmentsList(List<PreferenceCardItems> equipmentsList) {
		this.equipmentsList = equipmentsList;
	}

	public List<PreferenceCardItems> getConsumablesList() {
		return consumablesList;
	}

	public void setConsumablesList(List<PreferenceCardItems> consumablesList) {
		this.consumablesList = consumablesList;
	}

	public List<PreferenceCardItems> getImplantsList() {
		return implantsList;
	}

	public void setImplantsList(List<PreferenceCardItems> implantsList) {
		this.implantsList = implantsList;
	}

	public List<PreferenceCardItems> getServicesList() {
		return servicesList;
	}

	public void setServicesList(List<PreferenceCardItems> servicesList) {
		this.servicesList = servicesList;
	}

	public DecimalType getCheckedInDate() {
		return checkedInDate;
	}

	public void setCheckedInDate(DecimalType checkedInDate) {
		this.checkedInDate = checkedInDate;
	}

	public CodeableConcept getWard() {
		return ward;
	}

	public void setWard(CodeableConcept ward) {
		this.ward = ward;
	}

	public Reference getAppointmentRefList() {
		return appointmentRefList;
	}

	public void setAppointmentRefList(Reference appointmentRefList) {
		this.appointmentRefList = appointmentRefList;
	}

	public Practitioner getAnestheticList() {
		return anestheticList;
	}

	public void setAnestheticList(Practitioner anestheticList) {
		this.anestheticList = anestheticList;
	}

	public StringType getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(StringType businessStatus) {
		this.businessStatus = businessStatus;
	}

	public Coding getAnesthesiaType() {
		return anesthesiaType;
	}

	public void setAnesthesiaType(Coding anesthesiaType) {
		this.anesthesiaType = anesthesiaType;
	}

	public StringType getApproach() {
		return approach;
	}

	public void setApproach(StringType approach) {
		this.approach = approach;
	}

	public StringType getClinicareOrderId() {
		return clinicareOrderId;
	}

	public void setClinicareOrderId(StringType clinicareOrderId) {
		this.clinicareOrderId = clinicareOrderId;
	}

	public StringType getComment() {
		return comment;
	}

	public void setComment(StringType comment) {
		this.comment = comment;
	}

	public DecimalType getDateOfProcedure() {
		return dateOfProcedure;
	}

	public void setDateOfProcedure(DecimalType dateOfProcedure) {
		this.dateOfProcedure = dateOfProcedure;
	}

	public StringType getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(StringType diagnosis) {
		this.diagnosis = diagnosis;
	}

	public StringType getEstimatedTherapyTime() {
		return estimatedTherapyTime;
	}

	public void setEstimatedTherapyTime(StringType estimatedTherapyTime) {
		this.estimatedTherapyTime = estimatedTherapyTime;
	}

	public StringType getIcuReservation() {
		return icuReservation;
	}

	public void setIcuReservation(StringType icuReservation) {
		this.icuReservation = icuReservation;
	}

	public Coding getLaterality() {
		return laterality;
	}

	public void setLaterality(Coding laterality) {
		this.laterality = laterality;
	}

	public Coding getOperationType() {
		return operationType;
	}

	public void setOperationType(Coding operationType) {
		this.operationType = operationType;
	}

	public DecimalType getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(DecimalType sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Coding getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(Coding paymentMode) {
		this.paymentMode = paymentMode;
	}

	public StringType getLMP() {
		return LMP;
	}

	public void setLMP(StringType lMP) {
		LMP = lMP;
	}

	public StringType getRequirement() {
		return requirement;
	}

	public void setRequirement(StringType requirement) {
		this.requirement = requirement;
	}

	public CodeableConcept getLocationCCode() {
		return locationCode;
	}

	public void setLocationCode(CodeableConcept locationCode) {
		this.locationCode = locationCode;
	}
}
