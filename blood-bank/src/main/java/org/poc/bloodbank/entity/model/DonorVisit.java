/**
 * 
 */
package org.poc.bloodbank.entity.model;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SampledData;
import org.hl7.fhir.r4.model.StringType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;

/**
 * This model is an extension of Observation , this helps to capture the donor vitalities and visit information
 * @author Sankha
 *
 */
public class DonorVisit extends Observation {
	
	private static final long serialVersionUID = 1L;
	@JsonBackReference
	@Child(name = "businessStatus", type = { StringType.class }, order = 24, min = 0, max = 1, modifier = false, summary = true)
	private StringType businessStatus;
	@JsonBackReference
	@Child(name = "isAutologous", type = {
			BooleanType.class }, order = 25, min = 0, max = 1, modifier = false, summary = true)
	private BooleanType isAutologous;
	@JsonBackReference
	@Child(name = "donorVisitTime", type = {
			DecimalType.class }, order = 26, min = 0, max = 1, modifier = false, summary = true)
	private DecimalType donorVisitTime;
	
	@JsonBackReference
	@Child(name = "donorVisitNumber", type = {
			StringType.class }, order = 27, min = 0, max = 1, modifier = false, summary = true)
	private StringType donorVisitNumber;
	

	@JsonBackReference
	@Child(name = "bloodBagDetails", type = {
			BloodBag.class }, order = 28, min = 0, max = 1, modifier = false, summary = true)
	private BloodBag bloodBagDetails;


	/**
	 * @return the bloodBagDetails
	 */
	public BloodBag getBloodBagDetails() {
		return bloodBagDetails;
	}

	/**
	 * @param bloodBagDetails the bloodBagDetails to set
	 */
	public void setBloodBagDetails(BloodBag bloodBagDetails) {
		this.bloodBagDetails = bloodBagDetails;
	}

	/**
	 * @return the businessStatus
	 */
	public StringType getBusinessStatus() {
		return businessStatus;
	}

	/**
	 * @param businessStatus the businessStatus to set
	 */
	public void setBusinessStatus(StringType businessStatus) {
		this.businessStatus = businessStatus;
	}

	/**
	 * @return the isAutologous
	 */
	public BooleanType getIsAutologous() {
		return isAutologous;
	}

	/**
	 * @param isAutologous the isAutologous to set
	 */
	public void setIsAutologous(BooleanType isAutologous) {
		this.isAutologous = isAutologous;
	}

	/**
	 * @return the donorVisitTime
	 */
	public DecimalType getDonorVisitTime() {
		return donorVisitTime;
	}

	/**
	 * @param donorVisitTime the donorVisitTime to set
	 */
	public void setDonorVisitTime(DecimalType donorVisitTime) {
		this.donorVisitTime = donorVisitTime;
	}

	/**
	 * @return the donorVisitNumber
	 */
	public StringType getDonorVisitNumber() {
		return donorVisitNumber;
	}

	/**
	 * @param donorVisitNumber the donorVisitNumber to set
	 */
	public void setDonorVisitNumber(StringType donorVisitNumber) {
		this.donorVisitNumber = donorVisitNumber;
	}
	
	@Block
	public static class BloodBag extends SampledData{
		@JsonBackReference
		@Child(name = "collectionTimeFrom", type = {
				DecimalType.class }, order = 7, min = 0, max = 1, modifier = false, summary = true)
		private DecimalType collectionTimeFrom;
		
		@JsonBackReference
		@Child(name = "collectionTimeTo", type = {
				DecimalType.class }, order = 8, min = 0, max = 1, modifier = false, summary = true)
		private DecimalType collectionTimeTo;
		
		@JsonBackReference
		@Child(name = "barCode", type = {
				StringType.class }, order = 9, min = 0, max = 1, modifier = false, summary = true)
		private StringType barCode;
		
		@JsonBackReference
		@Child(name = "unitNo", type = {
				StringType.class }, order = 10, min = 0, max = 1, modifier = false, summary = true)
		private StringType unitNo;
		
		@JsonBackReference
		@Child(name = "segmentNo", type = {
				StringType.class }, order = 11, min = 0, max = 1, modifier = false, summary = true)
		private StringType segmentNo;
		
		@JsonBackReference
		@Child(name = "batchNo", type = {
				StringType.class }, order = 12, min = 0, max = 1, modifier = false, summary = true)
		private StringType batchNo;
		
		@JsonBackReference
		@Child(name = "location", type = {
				Coding.class }, order = 13, min = 0, max = 1, modifier = false, summary = true)
		private Coding location;
		
		@JsonBackReference
		@Child(name = "type", type = {
				Coding.class }, order = 14, min = 0, max = 1, modifier = false, summary = true)
		private Coding type;
		
		/**
		 * @return the barCode
		 */
		public StringType getBarCode() {
			return barCode;
		}

		/**
		 * @param barCode the barCode to set
		 */
		public void setBarCode(StringType barCode) {
			this.barCode = barCode;
		}

		/**
		 * @return the unitNo
		 */
		public StringType getUnitNo() {
			return unitNo;
		}

		/**
		 * @param unitNo the unitNo to set
		 */
		public void setUnitNo(StringType unitNo) {
			this.unitNo = unitNo;
		}

		/**
		 * @return the segmentNo
		 */
		public StringType getSegmentNo() {
			return segmentNo;
		}

		/**
		 * @param segmentNo the segmentNo to set
		 */
		public void setSegmentNo(StringType segmentNo) {
			this.segmentNo = segmentNo;
		}

		/**
		 * @return the batchNo
		 */
		public StringType getBatchNo() {
			return batchNo;
		}

		/**
		 * @param batchNo the batchNo to set
		 */
		public void setBatchNo(StringType batchNo) {
			this.batchNo = batchNo;
		}

		/**
		 * @return the location
		 */
		public Coding getLocation() {
			return location;
		}

		/**
		 * @param location the location to set
		 */
		public void setLocation(Coding location) {
			this.location = location;
		}

		/**
		 * @return the type
		 */
		public Coding getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(Coding type) {
			this.type = type;
		}

		/**
		 * @return the collectionTimeFrom
		 */
		public DecimalType getCollectionTimeFrom() {
			return collectionTimeFrom;
		}

		/**
		 * @param collectionTimeFrom the collectionTimeFrom to set
		 */
		public void setCollectionTimeFrom(DecimalType collectionTimeFrom) {
			this.collectionTimeFrom = collectionTimeFrom;
		}

		/**
		 * @return the collectionTimeTo
		 */
		public DecimalType getCollectionTimeTo() {
			return collectionTimeTo;
		}

		/**
		 * @param collectionTimeTo the collectionTimeTo to set
		 */
		public void setCollectionTimeTo(DecimalType collectionTimeTo) {
			this.collectionTimeTo = collectionTimeTo;
		}
		
		
		//TODO add the bag details data 
		
		
	}
}
