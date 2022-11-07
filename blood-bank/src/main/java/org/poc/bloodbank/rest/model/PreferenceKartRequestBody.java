/**
 * 
 */
package org.poc.bloodbank.rest.model;

import java.util.List;

import com.google.gson.JsonObject;

/**
 * @author Venkata Manusha, Sankha
 *
 */
public class PreferenceKartRequestBody {
	
	private JsonObject preparationAndPosition;
	private List<BaseItem> bladesAndSutures;
	private List<Instrument> instrumentSet;
	private List<BaseItem> consumables;
	private List<BaseItem> implants;
	private List<BaseItem> equipments;
	private List<Service> services;
	public List<BaseItem> getBladesAndSutures() {
		return bladesAndSutures;
	}
	public void setBladesAndSutures(List<BaseItem> bladesAndSutures) {
		this.bladesAndSutures = bladesAndSutures;
	}
	public List<Instrument> getInstrumentSet() {
		return instrumentSet;
	}
	public void setInstrumentSet(List<Instrument> instrumentSet) {
		this.instrumentSet = instrumentSet;
	}
	public List<BaseItem> getConsumables() {
		return consumables;
	}
	public void setConsumables(List<BaseItem> consumables) {
		this.consumables = consumables;
	}
	public List<BaseItem> getImplants() {
		return implants;
	}
	public void setImplants(List<BaseItem> implants) {
		this.implants = implants;
	}
	public List<BaseItem> getEquipments() {
		return equipments;
	}
	public void setEquipments(List<BaseItem> equipments) {
		this.equipments = equipments;
	}
	public List<Service> getServices() {
		return services;
	}
	public JsonObject getPreparationAndPosition() {
		return preparationAndPosition;
	}
	public void setPreparationAndPosition(JsonObject preparationAndPosition) {
		this.preparationAndPosition = preparationAndPosition;
	}
	public void setServices(List<Service> services) {
		this.services = services;
	}
	
	@Override
	public String toString() {
		return "PreferenceKartRequestBody [bladesAndSutures="
				+ bladesAndSutures + ", instrumentSet=" + instrumentSet + ", consumables=" + consumables + ", implants="
				+ implants + ", equipments=" + equipments + ", services=" + services + "]";
	}
	
	
	

}
