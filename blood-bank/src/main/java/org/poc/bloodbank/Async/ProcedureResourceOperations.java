package org.poc.bloodbank.Async;


import org.poc.bloodbank.entity.model.ExtendedProcedure;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;
import org.poc.bloodbank.service.ExtendedProcedureService;
import org.poc.bloodbank.util.BloodBankUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class ProcedureResourceOperations {

    @Autowired
    ExtendedProcedureService extendedProcedureService;

    @Async
    public ExtendedProcedure createProcedureAsyncFromServiceRequest(ExtendedServiceRequest extendedServiceRequest) {
        return extendedProcedureService.create(BloodBankUtils.craeteProcedureFromServiceRequest(extendedServiceRequest));
    }
}
