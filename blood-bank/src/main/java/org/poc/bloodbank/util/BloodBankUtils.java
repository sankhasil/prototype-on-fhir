package org.poc.bloodbank.util;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.SimpleQuantity;
import org.poc.bloodbank.entity.model.ExtendedProcedure;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class BloodBankUtils {
    private static Logger logger = LoggerFactory.getLogger(BloodBankUtils.class);
    private static String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static String DATE_FORMAT_VIEW_PATTERN = "dd-MM-YYYY";
    private static String DATE_FORMAT_NEW = "dd/mm/yy hh:mm:ss";

    /*
     * public static Long getCurrentEpochTime() { Date d = new
     * Date(System.currentTimeMillis()); return d.getTime() / 1000; }
     */

    public static ObservationReferenceRangeComponent createObservationReferenceRangeComponent(Double lowValue, Double highValue, String... contents) {
        ObservationReferenceRangeComponent obserComponent = new ObservationReferenceRangeComponent();
        if (contents.length == 3) {
            obserComponent.addAppliesTo(GeneralFhirUtility.createR4CodeableConcept(contents[0],
                    contents[0].toUpperCase(), contents[0], ""));
            obserComponent.setType(GeneralFhirUtility.createR4CodeableConcept(contents[1], contents[1].toUpperCase(),
                    contents[1], ""));
            obserComponent.setText(contents[0]);
            obserComponent.setLow(createSimpleQuantity(lowValue, contents[2]));
            obserComponent.setHigh(createSimpleQuantity(highValue, contents[2]));
        }
        return obserComponent;

    }

    /**
     * @param value
     * @param unit
     * @return
     */
    private static SimpleQuantity createSimpleQuantity(Double value, String unit) {
        SimpleQuantity quantity = new SimpleQuantity();
        quantity.setUnit(unit);
        quantity.setValue(value);
        return quantity;
    }

    public static void setUuidForObject(Object object) {
        if (object instanceof IBaseResource && ((IBaseResource) object).getIdElement().isEmpty()) {
            ((IBaseResource) object).setId(UUID.randomUUID().toString());
        }
        /*
         * if (object instanceof TemporaryPatient &&
         * StringUtils.isNotBlank(((TemporaryPatient) object).getId())) {
         * ((TemporaryPatient) object).setId(UUID.randomUUID().toString()); }
         */
    }

    public static boolean checkSearchKey(JsonObject jObject, String key) {
        if (jObject.has(key) && !jObject.get(key).isJsonNull() && jObject.get(key) != null) {
            String content = StringUtils.removeStart(jObject.get(key).toString(), "\"");
            content = StringUtils.removeEnd(content, "\"");
            return StringUtils.isNotBlank(content);

        }
        return false;
    }

    public static String convertDateToString(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW_PATTERN);
            return dateFormat.format(date);
        }
        return "";
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

    public static Integer toEpochTime(Long timeStamp) {
        if (timeStamp == null)
            return null;
        else {
            return (int) (timeStamp / 1000);
        }
    }

    /*
     * public static String getRandomNumber() { Random random = new Random(); String
     * generatedRandomNo = String.format("%04d", random.nextInt(10000));
     * logger.info("Generated Password : " + generatedRandomNo); return
     * generatedRandomNo; }
     *
     * public static String convertDateToUTCString(Date date) { if (date != null) {
     * SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
     * return dateFormat.format(date); } return ""; }
     *
     * public static DateTimeFormatter smsTime(Date date) { if(date!=null) {
     * DateTimeFormatter smsdateTime=DateTimeFormatter.ofPattern(DATE_FORMAT_NEW);
     * return smsdateTime; } return null; }
     */
    public static ExtendedProcedure craeteProcedureFromServiceRequest(ExtendedServiceRequest extendedServiceRequest) {
        ExtendedProcedure extendedProcedure = new ExtendedProcedure();
        if (extendedServiceRequest.getAnesthesiaType() != null) {
            extendedProcedure.setAnesthesiaType(extendedServiceRequest.getAnesthesiaType());
        }
//		if (extendedServiceRequest.getUnitId() != null) {
//			extendedProcedure.setUnitId(extendedServiceRequest.getUnitId());
//		}
//		if (extendedServiceRequest.getUnitCode() != null) {
//			extendedProcedure.setUnitCode(extendedServiceRequest.getUnitCode());
//		}
//		if (extendedServiceRequest.getOrgId() != null) {
//			extendedProcedure.setOrgId(extendedServiceRequest.getOrgId());
//		}
//		if (extendedServiceRequest.getOrgCode() != null) {
//			extendedProcedure.setOrgCode(extendedServiceRequest.getOrgCode());
//		}
        if (extendedServiceRequest.getIdentifierFirstRep() != null) {
            extendedProcedure.addBasedOn(GeneralFhirUtility.createReference("extendedServiceRequest",
                    extendedServiceRequest.getIdElement().getIdPart(), "usual",
                    extendedServiceRequest.getIdentifierFirstRep().getValue(),
                    extendedServiceRequest.getIdentifierFirstRep().getValue(), null));
        }
        if (extendedServiceRequest.getSubject() != null) {
            extendedProcedure.setSubject(extendedServiceRequest.getSubject());
        }
//		if (extendedServiceRequest.getContext() != null) {
//			extendedProcedure.setContext(extendedServiceRequest.getContext());
//		}
        if (extendedServiceRequest.getPerformerFirstRep() != null) {
            extendedProcedure.setAsserter(extendedServiceRequest.getPerformerFirstRep());
        }
        if (extendedServiceRequest.getBladesOrSuturesList() != null
                && extendedServiceRequest.getBladesOrSuturesList().isEmpty()) {
            extendedProcedure.setBladesOrSuturesList(
                    createPreferenceKartListForProcedure(extendedServiceRequest.getBladesOrSuturesList()));
        }
        if (extendedServiceRequest.getInstrumentSetsList() != null
                && extendedServiceRequest.getInstrumentSetsList().isEmpty()) {
            extendedProcedure.setInstrumentSetsList(
                    createPreferenceKartListForProcedure(extendedServiceRequest.getInstrumentSetsList()));
        }
        if (extendedServiceRequest.getConsumablesList() != null
                && extendedServiceRequest.getConsumablesList().isEmpty()) {
            extendedProcedure.setConsumablesList(
                    createPreferenceKartListForProcedure(extendedServiceRequest.getConsumablesList()));
        }
        if (extendedServiceRequest.getImplantsList() != null && extendedServiceRequest.getImplantsList().isEmpty()) {
            extendedProcedure
                    .setImplantsList(createPreferenceKartListForProcedure(extendedServiceRequest.getImplantsList()));
        }
        if (extendedServiceRequest.getServicesList() != null && extendedServiceRequest.getServicesList().isEmpty()) {
            extendedProcedure
                    .setServicesList(createPreferenceKartListForProcedure(extendedServiceRequest.getServicesList()));
        }
        return extendedProcedure;
    }

    public static List<ExtendedProcedure.PreferenceKartItemsComponent> createPreferenceKartListForProcedure(
            List<ExtendedServiceRequest.PreferenceCardItems> preferenceKartItems) {
        List<ExtendedProcedure.PreferenceKartItemsComponent> preferenceKartItemsComponents = new ArrayList<>();
        preferenceKartItems.forEach(preferenceKartItem -> {
            preferenceKartItemsComponents.add(createPreferenceKartForProcedure(preferenceKartItem));
        });
        return preferenceKartItemsComponents;
    }

    public static ExtendedProcedure.PreferenceKartItemsComponent createPreferenceKartForProcedure(
            ExtendedServiceRequest.PreferenceCardItems preferenceKartItem) {
        ExtendedProcedure.PreferenceKartItemsComponent preferenceKartItemsComponent = new ExtendedProcedure.PreferenceKartItemsComponent();
        preferenceKartItemsComponent.setCode(preferenceKartItem.getCode());
        preferenceKartItemsComponent.setDesc(preferenceKartItem.getDesc());
        preferenceKartItemsComponent.setGroup(preferenceKartItem.getGroup());
        preferenceKartItemsComponent.setGroupName(preferenceKartItem.getGroupName());
        preferenceKartItemsComponent.setPriority(preferenceKartItem.getPriority());
        preferenceKartItemsComponent.setType(preferenceKartItem.getType());
        preferenceKartItemsComponent.setItemId(preferenceKartItem.getItemId());
        preferenceKartItemsComponent
                .setRequestedQuantity(new PositiveIntType(preferenceKartItem.getQuantity().getValue()));
        preferenceKartItemsComponent
                .setUtilisedQuantity(new PositiveIntType(preferenceKartItem.getQuantity().getValue()));
        preferenceKartItemsComponent.setUnUtilisedQuantity(new PositiveIntType(0));
        return preferenceKartItemsComponent;
    }

}
