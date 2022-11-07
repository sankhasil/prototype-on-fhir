/**
 *
 */
package org.poc.bloodbank.rest.model;

import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.entity.model.DonorVisit;

/**
 * @author Sankha
 *
 */
public class DonorVisitRequestBody {

    private Donor donor;
    private DonorVisit donorVisit;

    /**
     * @return the donor
     */
    public Donor getDonor() {
        return donor;
    }

    /**
     * @param donor the donor to set
     */
    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    /**
     * @return the donorVisit
     */
    public DonorVisit getDonorVisit() {
        return donorVisit;
    }

    /**
     * @param donorVisit the donorVisit to set
     */
    public void setDonorVisit(DonorVisit donorVisit) {
        this.donorVisit = donorVisit;
    }

}
