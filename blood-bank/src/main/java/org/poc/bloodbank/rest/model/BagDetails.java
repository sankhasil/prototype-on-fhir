/**
 * 
 */
package org.poc.bloodbank.rest.model;

/**
 * @author Sankha
 *
 */
public class BagDetails {
	
	private String type,collectLocation;
	private Long barCode,unitNo,segmentNo,capacity,batchNo,collectionDateTimeFrom,collectionDateTimeTo;
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the collectLocation
	 */
	public String getCollectLocation() {
		return collectLocation;
	}
	/**
	 * @param collectLocation the collectLocation to set
	 */
	public void setCollectLocation(String collectLocation) {
		this.collectLocation = collectLocation;
	}
	/**
	 * @return the barCode
	 */
	public Long getBarCode() {
		return barCode;
	}
	/**
	 * @param barCode the barCode to set
	 */
	public void setBarCode(Long barCode) {
		this.barCode = barCode;
	}
	/**
	 * @return the unitNo
	 */
	public Long getUnitNo() {
		return unitNo;
	}
	/**
	 * @param unitNo the unitNo to set
	 */
	public void setUnitNo(Long unitNo) {
		this.unitNo = unitNo;
	}
	/**
	 * @return the segmentNo
	 */
	public Long getSegmentNo() {
		return segmentNo;
	}
	/**
	 * @param segmentNo the segmentNo to set
	 */
	public void setSegmentNo(Long segmentNo) {
		this.segmentNo = segmentNo;
	}
	/**
	 * @return the capacity
	 */
	public Long getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the batchNo
	 */
	public Long getBatchNo() {
		return batchNo;
	}
	/**
	 * @param batchNo the batchNo to set
	 */
	public void setBatchNo(Long batchNo) {
		this.batchNo = batchNo;
	}
	/**
	 * @return the collectionDateTimeFrom
	 */
	public Long getCollectionDateTimeFrom() {
		return collectionDateTimeFrom;
	}
	/**
	 * @param collectionDateTimeFrom the collectionDateTimeFrom to set
	 */
	public void setCollectionDateTimeFrom(Long collectionDateTimeFrom) {
		this.collectionDateTimeFrom = collectionDateTimeFrom;
	}
	/**
	 * @return the collectionDateTimeTo
	 */
	public Long getCollectionDateTimeTo() {
		return collectionDateTimeTo;
	}
	/**
	 * @param collectionDateTimeTo the collectionDateTimeTo to set
	 */
	public void setCollectionDateTimeTo(Long collectionDateTimeTo) {
		this.collectionDateTimeTo = collectionDateTimeTo;
	}
	
	
	
}
