package org.poc.bloodbank.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.poc.bloodbank.entity.model.*;
import org.poc.core.base.rest.config.fhir.BaseFhirSerializer;
import org.poc.core.base.rest.config.mongo.MongoDBBaseConfig;
import org.poc.core.fhir.converter.AbstractMongoConverter;
import org.poc.core.fhir.converter.ResourceDeserializer;
import org.poc.core.fhir.converter.ResourceSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.JsonObject;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class FhirConfig extends MongoDBBaseConfig {

    @Bean
    @Autowired
    public FhirContext fhirContext() {
        FhirContext fhirContext = FhirContext.forDstu3();
        return fhirContext;
    }

    @Bean
    @Autowired
    public FhirContext fhirContextForR4() {
        FhirContext fhirContext = FhirContext.forR4();
        return fhirContext;
    }

    @Bean
    @Autowired
    public ResourceDeserializer resourceDeserializer() {
        return new ResourceDeserializer();
    }

    @Bean
    @Autowired
    public ResourceSerializer resourceSerializer() {
        return new ResourceSerializer();
    }

    @Bean
    @Autowired
    public BaseFhirSerializer baseFhrSerializer() {
        return new BaseFhirSerializer() {

            @Override
            protected void createDeserializers(SimpleModule module) {
                module.addDeserializer(Donor.class, new JsonDeserializer<Donor>() {

                    @Override
                    public Donor deserialize(JsonParser parser, DeserializationContext context)
                            throws IOException, JsonProcessingException {
                        return (Donor) resourceDeserializer().deserialize(parser, context);
                    }
                });

            }
        };
    }

    @Bean
    @Autowired
    public ObjectMapper objectMapper(ResourceSerializer resourceSerializer, ResourceDeserializer resourceDeserializer) {
        ObjectMapper om = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Patient.class, resourceSerializer);
        module.addDeserializer(Patient.class, new JsonDeserializer<Patient>() {

            @Override
            public Patient deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException, JsonProcessingException {
                return (Patient) resourceDeserializer.deserialize(parser, context);
            }
        });
        module.addSerializer(Donor.class, new JsonSerializer<Donor>() {
            @Override
            public void serialize(Donor value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                IParser jsonParser = fhirContextForR4().newJsonParser();
                String encoded = jsonParser.encodeResourceToString(value);
                gen.writeRawValue(encoded);
            }
        });
        module.addDeserializer(Donor.class, new JsonDeserializer<Donor>() {

            @Override
            public Donor deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException, JsonProcessingException {
                return (Donor) fhirContext().newJsonParser().parseResource(Donor.class,
                        parser.getCodec().readTree(parser).toString());
            }
        });
        module.addSerializer(DonorVisit.class, new JsonSerializer<DonorVisit>() {
            @Override
            public void serialize(DonorVisit value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                IParser jsonParser = fhirContextForR4().newJsonParser();
                String encoded = jsonParser.encodeResourceToString(value);
                gen.writeRawValue(encoded);
            }
        });
        module.addDeserializer(DonorVisit.class, new JsonDeserializer<DonorVisit>() {

            @Override
            public DonorVisit deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException, JsonProcessingException {
                return (DonorVisit) fhirContext().newJsonParser().parseResource(DonorVisit.class,
                        parser.getCodec().readTree(parser).toString());
            }
        });
        module.addSerializer(ExtendedAppointment.class, resourceSerializer);
        module.addDeserializer(ExtendedAppointment.class, new JsonDeserializer<ExtendedAppointment>() {

            @Override
            public ExtendedAppointment deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException, JsonProcessingException {
                return (ExtendedAppointment) fhirContext().newJsonParser().parseResource(ExtendedAppointment.class,
                        parser.getCodec().readTree(parser).toString());
            }
        });
        module.addSerializer(ExtendedSpecimen.class, resourceSerializer);
        module.addDeserializer(ExtendedSpecimen.class, new JsonDeserializer<ExtendedSpecimen>() {

            @Override
            public ExtendedSpecimen deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException, JsonProcessingException {
                return (ExtendedSpecimen) resourceDeserializer.deserialize(parser, context);
            }
        });
        module.addDeserializer(JsonObject.class, new JsonDeserializer<JsonObject>() {

            @Override
            public JsonObject deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException, JsonProcessingException {
                return new com.google.gson.JsonParser().parse(parser.getCodec().readTree(parser).toString())
                        .getAsJsonObject();
            }
        });
        module.addSerializer(ExtendedProcedure.class, resourceSerializer);
        module.addDeserializer(ExtendedProcedure.class, new JsonDeserializer<ExtendedProcedure>() {
            @Override
            public ExtendedProcedure deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException, JsonProcessingException {
                return (ExtendedProcedure) fhirContextForR4().newJsonParser().parseResource(ExtendedProcedure.class,
                        parser.getCodec().readTree(parser).toString());
            }

        });
        /*
         * module.addSerializer(AppointmentConfigRequestBody.class, resourceSerializer);
         * module.addDeserializer(Reference.class, new JsonDeserializer<Reference>() {
         *
         * @Override public Reference deserialize(JsonParser parser,
         * DeserializationContext context) throws IOException, JsonProcessingException {
         * return (Reference)
         * fhirContext().newJsonParser().parseResource(Reference.class,parser.getCodec()
         * .readTree(parser).toString()); }
         *
         * }); module.addSerializer(Period.class, resourceSerializer);
         * module.addDeserializer(Period.class, new JsonDeserializer<Period>() {
         *
         * @Override public Period deserialize(JsonParser parser, DeserializationContext
         * context) throws IOException, JsonProcessingException { return (Period)
         * fhirContext().newJsonParser().parseResource(Period.class,parser.getCodec().
         * readTree(parser).toString()); }
         *
         * });
         */
        module.addSerializer(ExtendedServiceRequest.class, new JsonSerializer<ExtendedServiceRequest>() {
            @Override
            public void serialize(ExtendedServiceRequest value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                IParser jsonParser = fhirContextForR4().newJsonParser();
                String encoded = jsonParser.encodeResourceToString(value);
                gen.writeRawValue(encoded);
            }
        });
        module.addDeserializer(ExtendedServiceRequest.class, new JsonDeserializer<ExtendedServiceRequest>() {
            @Override
            public ExtendedServiceRequest deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException, JsonProcessingException {
                return (ExtendedServiceRequest) fhirContextForR4().newJsonParser()
                        .parseResource(ExtendedServiceRequest.class, parser.getCodec().readTree(parser).toString());
            }

        });
        om.registerModule(module);
        return om;
    }

    @Override
    protected List<AbstractMongoConverter> createDocumentToDomainConverters() {
        List<AbstractMongoConverter> converterList = new ArrayList<>();
        AbstractMongoConverter<Document, Donor> donorConverter = new AbstractMongoConverter<Document, Donor>() {
            public Donor convert(Document document) {
                return super.convert(document);
            }
        };
        AbstractMongoConverter<Document, DonorVisit> donorVisitConverter = new AbstractMongoConverter<Document, DonorVisit>() {
            public DonorVisit convert(Document document) {
                return super.convert(document);
            }
        };
        AbstractMongoConverter<Document, ExtendedAppointment> extendedAppointmentConverter = new AbstractMongoConverter<Document, ExtendedAppointment>() {
            public ExtendedAppointment convert(Document document) {
                return super.convert(document);
            }
        };
        AbstractMongoConverter<Document, Reference> referenceConverter = new AbstractMongoConverter<Document, Reference>() {
            public Reference convert(Document document) {
                return super.convert(document);
            }
        };
        AbstractMongoConverter<Document, Period> periodConverter = new AbstractMongoConverter<Document, Period>() {
            public Period convert(Document document) {
                return super.convert(document);
            }
        };
        AbstractMongoConverter<Document, ExtendedServiceRequest> serviceRequestConverter = new AbstractMongoConverter<Document, ExtendedServiceRequest>() {
            public ExtendedServiceRequest convert(Document document) {
                return super.convert(document);
            }
        };

        AbstractMongoConverter<ExtendedServiceRequest, Document> serviceRequestConverterToDocument = new AbstractMongoConverter<ExtendedServiceRequest, Document>() {
            @Override
            public Document convert(ExtendedServiceRequest t1) {
                return super.convert(t1);
            }
        };
        AbstractMongoConverter<Donor, Document> donorConverterToDocument = new AbstractMongoConverter<Donor, Document>() {
            @Override
            public Document convert(Donor t1) {
                return super.convert(t1);
            }
        };
        AbstractMongoConverter<DonorVisit, Document> donorVisitConverterToDocument = new AbstractMongoConverter<DonorVisit, Document>() {
            @Override
            public Document convert(DonorVisit t1) {
                return super.convert(t1);
            }
        };
        AbstractMongoConverter<ExtendedProcedure, Document> procedureConverterToDocument = new AbstractMongoConverter<ExtendedProcedure, Document>() {
            @Override
            public Document convert(ExtendedProcedure t1) {
                return super.convert(t1);
            }
        };
        converterList.add(donorVisitConverter);
        converterList.add(donorConverter);
        converterList.add(donorConverterToDocument);
        converterList.add(donorVisitConverterToDocument);
        converterList.add(extendedAppointmentConverter);
        converterList.add(periodConverter);
        converterList.add(referenceConverter);
        converterList.add(serviceRequestConverter);
        converterList.add(serviceRequestConverterToDocument);
        converterList.add(procedureConverterToDocument);
        return converterList;
    }



    @Autowired
    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(
            InMemorySwaggerResourcesProvider defaultResourcesProvider) {
        return () -> {
            SwaggerResource wsResource = new SwaggerResource();
            wsResource.setName("BloodBank API");
            wsResource.setSwaggerVersion("2.0");
            wsResource.setLocation("/swagger.yaml");

            List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
            resources.add(wsResource);
            return resources;
        };
    }

    @Bean
    public Docket opdApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("BloodBank API").select()
                .apis(RequestHandlerSelectors.any()).paths(PathSelectors.none()).build();
    }

//	private Predicate<String> opdPaths() {
//		return Predicates.or(PathSelectors.regex("/extendedPatient.*"),PathSelectors.regex("/extendedaccount.*"));
//	}
//	
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder().title("SDGT OPD API")
//				.description("OPD API for reference")
//				.version("1.0").build();
//	}

}
