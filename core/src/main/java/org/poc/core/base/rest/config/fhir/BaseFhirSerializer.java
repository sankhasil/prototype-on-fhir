package org.poc.core.base.rest.config.fhir;

import ca.uhn.fhir.context.FhirContext;

import org.poc.core.fhir.converter.ResourceDeserializer;
import org.poc.core.fhir.converter.ResourceSerializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.hl7.fhir.dstu3.model.DomainResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

public abstract class BaseFhirSerializer {

    @Bean
    @Autowired
    public FhirContext fhirContext(){
        FhirContext fhirContext = FhirContext.forDstu3();
        return fhirContext;
    }
    @Bean @Autowired
    public ResourceDeserializer resourceDeserializer(){
        return new ResourceDeserializer();
    }
    @Bean @Autowired
    public ResourceSerializer resourceSerializer(){
        return new ResourceSerializer();
    }

    @Bean @Autowired
    public ObjectMapper objectMapper(ResourceSerializer resourceSerializer, ResourceDeserializer resourceDeserializer) {
        ObjectMapper om = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(DomainResource.class, resourceSerializer);
//        module.addDeserializer(Patient.class, new JsonDeserializer<Patient>() {
//
//            @Override
//            public Patient deserialize(JsonParser parser, DeserializationContext context)
//                    throws IOException, JsonProcessingException {
//                return (Patient) resourceDeserializer.deserialize(parser, context);
//            }
//        });

        createDeserializers(module);
        //TODO- for some classes like extendedpatient generic is not working

        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
                                                          JsonDeserializer<?> deserializer) {
                if(DomainResource.class.isAssignableFrom(beanDesc.getBeanClass() )){
//                    System.out.println("===========================================");
                    return new JsonDeserializer<DomainResource>() {

                        @Override
                        public DomainResource deserialize(JsonParser parser, DeserializationContext context)
                                throws IOException, JsonProcessingException {
                            return (DomainResource) resourceDeserializer.deserialize(parser, context);
                        }
                    };
                }
                return super.modifyDeserializer(config, beanDesc, deserializer);
            }
        });

        om.registerModule(module);
        return om;
    }

    protected abstract void createDeserializers(SimpleModule module);

}
