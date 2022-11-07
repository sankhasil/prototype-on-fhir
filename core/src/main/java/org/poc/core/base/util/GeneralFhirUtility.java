package org.poc.core.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import org.hl7.fhir.instance.model.api.IBaseResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.hl7.fhir.r4.model.*;

public class GeneralFhirUtility {
	private static String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static String DATE_FORMAT_VIEW_PATTERN = "dd-MM-YYYY";
	private static String DATE_FORMAT_NEW = "dd/mm/yy hh:mm:ss";

	public static Long getCurrentEpochTime() {
		Date d = new Date(System.currentTimeMillis());
		return d.getTime() / 1000;
	}

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

	public static boolean checkSearchKey(JsonObject jObject, String key) {
		if (jObject.has(key) && !jObject.get(key).isJsonNull() && jObject.get(key) != null) {
			String content = StringUtils.removeStart(jObject.get(key).toString(), "\"");
			content = StringUtils.removeEnd(content, "\"");
			return StringUtils.isNotBlank(content);

		}
		return false;
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
		return codeDesc.get("code") != null && !codeDesc.get("code").isJsonNull() && codeDesc.get("desc") != null
				&& !codeDesc.get("desc").isJsonNull();
	}

	public static org.hl7.fhir.dstu3.model.CodeableConcept createCodeableConcept(String codeableConceptText,
			String code, String display, String system) {
		org.hl7.fhir.dstu3.model.CodeableConcept codeableConcept = new org.hl7.fhir.dstu3.model.CodeableConcept();
		codeableConcept.setText(codeableConceptText);
		org.hl7.fhir.dstu3.model.Coding coding = new org.hl7.fhir.dstu3.model.Coding(system, code, display);
		codeableConcept.addCoding(coding);
		return codeableConcept;
	}

	public static org.hl7.fhir.r4.model.CodeableConcept createR4CodeableConcept(String codeableConceptText, String code,
			String display, String system) {
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

	public static org.hl7.fhir.dstu3.model.Reference createDstu3Reference(String participantEndpoint,
			String participantId, String participantIdentifierType, String participantIdentifierValue,
			String referenceDisplayString, IBaseResource resource) {
		org.hl7.fhir.dstu3.model.Reference reference;
		reference = new org.hl7.fhir.dstu3.model.Reference();
		reference.setResource(resource);
		if (participantEndpoint.isEmpty()) {
			reference.setReference(participantId);
		} else {
			reference.setReference(participantEndpoint + "/" + participantId);
		}
		reference.setDisplay(referenceDisplayString);
		reference.setIdentifier(createDstu3Identifier(participantIdentifierValue,
				org.hl7.fhir.dstu3.model.Identifier.IdentifierUse.OFFICIAL, null, participantIdentifierType,
				participantIdentifierType, participantIdentifierType, null));
		return reference;
	}

	public static org.hl7.fhir.dstu3.model.Identifier createDstu3Identifier(String value,
			org.hl7.fhir.dstu3.model.Identifier.IdentifierUse use, String system, String typeCodeableConceptText,
			String typeCode, String typeDisplay, String typeSystem) {
		org.hl7.fhir.dstu3.model.Identifier identifier = new org.hl7.fhir.dstu3.model.Identifier();
		identifier.setValue(value);
		identifier.setType(createCodeableConcept(typeCodeableConceptText, typeCode, typeDisplay, typeSystem));
		identifier.setUse(use);
		identifier.setSystem(system);
		return identifier;
	}

	public static String getFhirInstantObjectFromEpochTime(String epochTime) {
		if (!Pattern.matches("[^0-9?!\\.]", epochTime)) {
			Long timeInMillis = Long.parseLong(epochTime) * 1000;
			Date dateObject = new Date(timeInMillis);
			SimpleDateFormat fhireDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
			return fhireDateFormat.format(dateObject);

		}
		return null;
	}

	public static Integer toEpochTime(String timeStamp) {
		if (timeStamp == null)
			return null;
		try {
			SimpleDateFormat yearDateFormat = new SimpleDateFormat("YYYY-MM-DDHH:mm:ss.SSS zzz");
			Date date1 = yearDateFormat.parse(timeStamp);
			long epoch = date1.getTime();
			return (int) (epoch / 1000);

		} catch (ParseException e) {
			return null;
		}
	}

	public static String convertDateToString(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW_PATTERN);
			return dateFormat.format(date);
		}
		return "";
	}

	public static String convertDateToUTCString(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
			return dateFormat.format(date);
		}
		return "";
	}

	public static DateTimeFormatter smsTime(Date date) {
		if (date != null) {
			DateTimeFormatter smsdateTime = DateTimeFormatter.ofPattern(DATE_FORMAT_NEW);
			return smsdateTime;
		}
		return null;
	}

	public static String getRandomNumber() {
		Random random = new Random();
		String generatedRandomNo = String.format("%04d", random.nextInt(10000));
		return generatedRandomNo;
	}

}
