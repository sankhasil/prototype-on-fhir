package org.poc.bloodbank.rest.model;

public class Instrument extends BaseItem {
	
	private String type,priority;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Instrument [type=" + type + ", priority=" + priority + "]";
	}
	
	

}
