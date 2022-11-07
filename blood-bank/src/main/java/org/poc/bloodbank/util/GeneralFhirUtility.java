package org.poc.bloodbank.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.hl7.fhir.instance.model.api.IBaseResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.hl7.fhir.r4.model.*;

public class GeneralFhirUtility {

	public static Reference createReference(String participantEndpoint, String participantId,
											String participantIdentifierType, String participantIdentifierValue, String referenceDisplayString,
											IBaseResource resource) {
		Reference reference;
		reference = new Reference();
		reference.setResource(resource);
		if (participantEndpoint.isEmpty()) {
			reference.setReference(participantId);
		} else {
			reference.setReference(participantEndpoint + "/" + participantId);
		}
		reference.setDisplay(referenceDisplayString);
		reference.setIdentifier(createIdentifier(participantIdentifierValue, Identifier.IdentifierUse.OFFICIAL, null,
				participantIdentifierType, participantIdentifierType, participantIdentifierType, null));
		return reference;
	}

	public static List<StringType> createStringTypeListFromStringList(List<String> stringList) {
		List<StringType> stringTypeList = new ArrayList<StringType>();
		for (String string : stringList) {
			if (StringUtils.isNotBlank(string)) {
				StringType stringType = new StringType(string);
				stringTypeList.add(stringType);
			}
		}
		return stringTypeList;
	}
	/**
	 * Removes the prefix string from the patientId.
	 *
	 * @param resourceId a {@link String} value
	 * @return a trimmed {@link String} value.
	 */
	public static String trimResourceId(final String resourceId) {
		return resourceId.split("/").length > 1 ? resourceId.split("/")[1] : resourceId;
	}
	public static boolean checkCodeDescExists(JsonObject codeDesc) {
		return codeDesc.get("code") !=null && !codeDesc.get("code").isJsonNull() && codeDesc.get("desc") !=null && !codeDesc.get("desc").isJsonNull();
		}
	public static org.hl7.fhir.dstu3.model.CodeableConcept createCodeableConcept(String codeableConceptText, String code, String display,
														String system) {
		org.hl7.fhir.dstu3.model.CodeableConcept codeableConcept = new org.hl7.fhir.dstu3.model.CodeableConcept();
		codeableConcept.setText(codeableConceptText);
		org.hl7.fhir.dstu3.model.Coding coding = new org.hl7.fhir.dstu3.model.Coding(system, code, display);
		codeableConcept.addCoding(coding);
		return codeableConcept;
	}
	public static org.hl7.fhir.r4.model.CodeableConcept createR4CodeableConcept(String codeableConceptText, String code, String display,
			String system) {
		org.hl7.fhir.r4.model.CodeableConcept codeableConcept = new org.hl7.fhir.r4.model.CodeableConcept();
		codeableConcept.setText(codeableConceptText);
		org.hl7.fhir.r4.model.Coding coding = new org.hl7.fhir.r4.model.Coding(system, code, display);
		codeableConcept.addCoding(coding);
		return codeableConcept;
	}

	public static Identifier createIdentifier(String value, Identifier.IdentifierUse use, String system,
			String typeCodeableConceptText, String typeCode, String typeDisplay, String typeSystem) {
		Identifier identifier = new Identifier();
		identifier.setValue(value);
		identifier.setType(createR4CodeableConcept(typeCodeableConceptText, typeCode, typeDisplay, typeSystem));
		identifier.setUse(use);
		identifier.setSystem(system);
		return identifier;
	}

	public static SimpleQuantity createQuantity(int value, Quantity.QuantityComparator comparator, String unit) {
		SimpleQuantity simpleQuantity = new SimpleQuantity();
		simpleQuantity.setValue(value);
		simpleQuantity.setComparator(comparator);
		simpleQuantity.setUnit(unit);
		return simpleQuantity;
	}

	public static org.hl7.fhir.dstu3.model.Reference createDstu3Reference(String participantEndpoint, String participantId,
											String participantIdentifierType, String participantIdentifierValue, String referenceDisplayString,
											IBaseResource resource) {
		org.hl7.fhir.dstu3.model.Reference reference;
		reference = new org.hl7.fhir.dstu3.model.Reference();
		reference.setResource(resource);
		if (participantEndpoint.isEmpty()) {
			reference.setReference(participantId);
		} else {
			reference.setReference(participantEndpoint + "/" + participantId);
		}
		reference.setDisplay(referenceDisplayString);
		reference.setIdentifier(createDstu3Identifier(participantIdentifierValue, org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.OFFICIAL, null,
				participantIdentifierType, participantIdentifierType, participantIdentifierType, null));
		return reference;
	}

	public static org.hl7.fhir.dstu3.model.Identifier createDstu3Identifier(String value, org.hl7.fhir.dstu3.model.Identifier.IdentifierUse use, String system,
											  String typeCodeableConceptText, String typeCode, String typeDisplay, String typeSystem) {
		org.hl7.fhir.dstu3.model.Identifier identifier = new org.hl7.fhir.dstu3.model.Identifier();
		identifier.setValue(value);
		identifier.setType(createCodeableConcept(typeCodeableConceptText, typeCode, typeDisplay, typeSystem));
		identifier.setUse(use);
		identifier.setSystem(system);
		return identifier;
	}


}
