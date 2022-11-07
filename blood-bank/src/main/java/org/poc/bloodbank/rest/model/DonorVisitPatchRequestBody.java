/**
 * 
 */
package org.poc.bloodbank.rest.model;

import java.util.List;

/**
 * @author Sankha
 *
 */
public class DonorVisitPatchRequestBody {
	
	List<Measurement> screeningList;
	List<ScreenTest> screeningTestList;
	BagDetails bloodBagDetails;
	List<Measurement> examinationDetailList;
	/**
	 * @return the screeningList
	 */
	public List<Measurement> getScreeningList() {
		return screeningList;
	}
	/**
	 * @param screeningList the screeningList to set
	 */
	public void setScreeningList(List<Measurement> screeningList) {
		this.screeningList = screeningList;
	}
	/**
	 * @return the screeningTestList
	 */
	public List<ScreenTest> getScreeningTestList() {
		return screeningTestList;
	}
	/**
	 * @param screeningTestList the screeningTestList to set
	 */
	public void setScreeningTestList(List<ScreenTest> screeningTestList) {
		this.screeningTestList = screeningTestList;
	}
	/**
	 * @return the bloodBagDetails
	 */
	public BagDetails getBloodBagDetails() {
		return bloodBagDetails;
	}
	/**
	 * @param bloodBagDetails the bloodBagDetails to set
	 */
	public void setBloodBagDetails(BagDetails bloodBagDetails) {
		this.bloodBagDetails = bloodBagDetails;
	}
	/**
	 * @return the examinationDetailList
	 */
	public List<Measurement> getExaminationDetailList() {
		return examinationDetailList;
	}
	/**
	 * @param examinationDetailList the examinationDetailList to set
	 */
	public void setExaminationDetailList(List<Measurement> examinationDetailList) {
		this.examinationDetailList = examinationDetailList;
	}
	

}
