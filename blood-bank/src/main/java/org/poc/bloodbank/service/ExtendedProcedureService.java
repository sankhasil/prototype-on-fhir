package org.poc.bloodbank.service;

import org.poc.bloodbank.entity.model.ExtendedProcedure;
import org.poc.bloodbank.entity.model.ExtendedServiceRequest;
import org.poc.bloodbank.repository.ExtendedProcedureRepository;
import org.poc.bloodbank.util.BloodBankUtils;
import org.poc.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ExtendedProcedureService extends BaseService<ExtendedProcedure> {

    ExtendedProcedureRepository extendedProcedureRepository;

    @Autowired
    public ExtendedProcedureService(ExtendedProcedureRepository extendedProcedureRepository,
                                    MongoTemplate mongoTemplate) {
        super(extendedProcedureRepository, mongoTemplate);
        this.extendedProcedureRepository = extendedProcedureRepository;
    }

    public ExtendedProcedure createProcedureForServiceRequest(ExtendedServiceRequest extendedServiceRequest) {
        return create(BloodBankUtils.craeteProcedureFromServiceRequest(extendedServiceRequest));
    }

    public List<ExtendedProcedure> getProcedureForServiceRequest(String id) {
        return extendedProcedureRepository.getByBasedOn_Identifier_Value(id);
    }
}
