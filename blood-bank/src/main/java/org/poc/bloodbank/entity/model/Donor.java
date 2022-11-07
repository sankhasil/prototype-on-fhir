/**
 *
 */
package org.poc.bloodbank.entity.model;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

import java.util.List;

/**
 * @author Sankha Sil
 *
 */

@ResourceDef(name = "Donor", profile = "http://hl7.org/fhir/Profile/Donor")
public class Donor extends Patient {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JsonBackReference
    @Child(name = "race", type = {StringType.class}, order = 17, min = 0, max = 1, modifier = false, summary = true)
    private StringType race;
    @JsonBackReference
    @Child(name = "donorCategory", type = {
            StringType.class}, order = 18, min = 0, max = 1, modifier = false, summary = true)
    private StringType donorCategory;
    @JsonBackReference
    @Child(name = "occupation", type = {
            StringType.class}, order = 19, min = 0, max = 1, modifier = false, summary = true)
    private StringType occupation;
    @JsonBackReference
    @Child(name = "bloodGroup", type = {StringType.class}, order = 20, min = 0, max = 1, modifier = false, summary = true)
    private StringType bloodGroup;

    @JsonBackReference
    @Child(name = "donorAutoSequenceNo", type = {
            DecimalType.class}, order = 31, min = 0, max = 1, modifier = false, summary = true)
    private DecimalType donorAutoSequenceNo;

    public StringType getDonorCategory() {
        return donorCategory;
    }

    public void setDonorCategory(StringType donorCategory) {
        this.donorCategory = donorCategory;
    }

    public DecimalType getDonorAutoSequenceNo() {
        return donorAutoSequenceNo;
    }

    public void setDonorAutoSequenceNo(DecimalType donorAutoSequenceNo) {
        this.donorAutoSequenceNo = donorAutoSequenceNo;
    }

    public StringType getRace() {
        return race;
    }

    @JsonBackReference
    @Child(name = "religion", type = {
            StringType.class}, order = 22, min = 0, max = 1, modifier = false, summary = true)
    private StringType religion;

    public StringType getReligion() {
        return religion;
    }

    public void setReligion(StringType religion) {
        this.religion = religion;
    }

    @JsonBackReference
    @Child(name = "donorno", type = {
            StringType.class}, order = 23, min = 0, max = 1, modifier = false, summary = true)
    private StringType donorno;

    public StringType getDonorno() {
        return donorno;
    }

    public void setDonorno(StringType donorno) {
        this.donorno = donorno;
    }

    @JsonBackReference
    @Child(name = "registrationDate", type = {
            DecimalType.class}, order = 24, min = 0, max = 1, modifier = false, summary = true)
    private DecimalType registrationDate;

    public DecimalType getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(DecimalType registrationDate) {
        this.registrationDate = registrationDate;
    }

    @JsonBackReference
    @Child(name = "nationality", type = {
            StringType.class}, order = 21, min = 0, max = 1, modifier = false, summary = true)
    private StringType nationality;
    @JsonBackReference
    @Child(name = "fullName", type = {
            StringType.class}, order = 25, min = 0, max = 1, modifier = false, summary = true)
    private StringType fullName;

    public StringType getFullName() {
        return fullName;
    }

    public void setFullName(StringType fullName) {
        this.fullName = fullName;
    }


    public void setNationality(StringType nationality) {
        this.nationality = nationality;
    }

    public StringType getNationality() {
        return nationality;
    }

    public void setRace(StringType race) {
        this.race = race;
    }

    public StringType getOccupation() {
        return occupation;
    }

    public void setOccupation(StringType occupation) {
        this.occupation = occupation;
    }


    public StringType getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(StringType bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setnNationality(StringType nationality) {
        this.nationality = nationality;
    }


    @JsonBackReference
    @Child(name = "oldReference", type = {
            CodeableConcept.class}, order = 33, max = Child.MAX_UNLIMITED, modifier = false, summary = true)
    private List<CodeableConcept> oldReference;

    public List<CodeableConcept> getOldReference() {
        return oldReference;
    }

    public void setOldReference(List<CodeableConcept> oldReference) {
        this.oldReference = oldReference;
    }


}
