/**
 *
 */
package org.poc.bloodbank.Async;

import com.google.gson.JsonObject;
import org.poc.bloodbank.entity.model.Donor;
import org.poc.bloodbank.entity.model.DonorVisit;
import org.poc.bloodbank.enums.DonorSearchEnum;
import org.poc.bloodbank.service.DonorVisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sankha
 *
 */
@EnableAsync
@Component
public class ParallelExecution {

    Logger parallelExecutionLogger = LoggerFactory.getLogger(ParallelExecution.class);
    private DonorVisitService donorVisitService;

    public DonorVisitService getDonorVisitService() {
        return donorVisitService;
    }

    public void setDonorVisitService(DonorVisitService donorVisitService) {
        this.donorVisitService = donorVisitService;
    }


    @Async
    public void updateEncounterDetails(Donor donorResponse) {
        JsonObject searchObject = new JsonObject();
        searchObject.addProperty(DonorSearchEnum.DONORNO.getValue(), donorResponse.getDonorno().getValueAsString());
        List<DonorVisit> donorVisitFound = donorVisitService.search(searchObject);
        if (donorVisitFound.size() > 0) {
            donorVisitFound.stream().forEach(donorVisit -> {
                donorVisitService.addDonorDetails(donorVisit, donorResponse);
                donorVisitService.update(donorVisit,
                        org.poc.core.base.util.GeneralFhirUtility.trimResourceId(donorVisit.getId()));

            });
        }

    }
}
