/**
 * 
 */
package org.poc.bloodbank.rest.model;

/**
 * @author Sankha
 *
 */
public class Measurement {
	
	private String topic,unit,type;
	private Double lowValue,highValue;
	private Long dateTime;
	
	
	/**
	 * @return the dateTime
	 */
	public Long getDateTime() {
		return dateTime;
	}
	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(Long dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the lowValue
	 */
	public Double getLowValue() {
		return lowValue;
	}
	/**
	 * @param lowValue the lowValue to set
	 */
	public void setLowValue(Double lowValue) {
		this.lowValue = lowValue;
	}
	/**
	 * @return the highValue
	 */
	public Double getHighValue() {
		return highValue;
	}
	/**
	 * @param highValue the highValue to set
	 */
	public void setHighValue(Double highValue) {
		this.highValue = highValue;
	}
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
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

}
