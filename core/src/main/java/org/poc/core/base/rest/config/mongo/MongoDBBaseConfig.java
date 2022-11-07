package org.poc.core.base.rest.config.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Patient;
import org.poc.core.fhir.converter.AbstractMongoConverter;
import org.poc.core.fhir.converter.ResourceDeserializer;
import org.poc.core.fhir.converter.ResourceSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;


public abstract class MongoDBBaseConfig extends AbstractMongoClientConfiguration {

    public ObjectMapper objectMapper;
    @Autowired
    @Bean
     public ObjectMapper objectMapper() {
        objectMapper = new ObjectMapper();
        return objectMapper;
    }
    @Autowired
    private Environment environment;

    @Value("${spring.data.mongodb.database:test}")
    private String databaseName;

    @Value("${spring.data.mongodb.username:root}")
    private String userName;
    @Value("${spring.data.mongodb.password:root}")
    private String password;
    @Value("${spring.data.mongodb.authenticationDatabase:admin}")
    private String authenticationDatabase;
    @Value("${spring.data.mongodb.host:localhost}")
    private String host;
    @Value("${spring.data.mongodb.port:27017}")
    private int port;


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getAuthenticationDatabase() {
        return authenticationDatabase;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {

        MongoCredential mongoCredential = MongoCredential.createCredential(userName, authenticationDatabase, password.toCharArray());
        String cons = String.format("mongodb://%s:%d/?replicaSet=rs0", host, port);

        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        MongoClientSettings mongoClientSettings = builder
                .applicationName(databaseName)
                .credential(mongoCredential)
                .applyToSslSettings(sslb ->
                        sslb.enabled(true)
                                .invalidHostNameAllowed(true))
                .applyConnectionString(new ConnectionString(cons))
                .build();


        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        return mongoClient;
    }


    @Bean
    public MongoCustomConversions customConversions() {
        final List<Converter> converters = new ArrayList<>();

        for (AbstractMongoConverter con : createDocumentToDomainConverters()) {
            con.setObjectMapper(objectMapper);
            converters.add(con);
        }

        converters.add(new AbstractMongoConverter<DomainResource, Document>() {
            @Override
            public Document convert(DomainResource patient) {
                return super.convertToDocument(objectMapper, patient);
            }
        });

//        LOG.info("Register converters: " + converters);
        return new MongoCustomConversions(converters);
    }

    protected <T extends DomainResource> void createMongoConverter(List<Converter> converters, Class<T> dr,
                                                                   ObjectMapper objectMapper) {

        AbstractMongoConverter<Document, T> mongoConverter = new AbstractMongoConverter<>();
        mongoConverter.setObjectMapper(objectMapper);
        mongoConverter.setDomainClass(dr);
        AbstractMongoConverter<Document, Patient> mongoConverterz = new AbstractMongoConverter<Document, Patient>() {
            @Override
            public Patient convert(Document document) {
                return super.convert(document);
            }
        };
        mongoConverterz.setObjectMapper(objectMapper);
        converters.add(mongoConverterz);
    }

// TODO: get list of domain objects using reflections

    protected List<Class<? extends DomainResource>> getDomainObjects() {

        ArrayList<Class<? extends DomainResource>> domainResources = new ArrayList();
        domainResources.add(Patient.class);
        return domainResources;
    }

    protected abstract List<AbstractMongoConverter> createDocumentToDomainConverters();

}