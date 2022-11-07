/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.poc.core.service;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.hl7.fhir.dstu3.model.Property;
import org.poc.core.constants.HttpHeaders;
import org.poc.core.exceptions.BaseRuntimeException;
import org.poc.core.exceptions.DataException;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import org.poc.core.base.rest.util.HttpUtils;
import org.poc.core.base.rest.util.HttpUtilsR4;
import org.poc.core.base.util.GeneralFhirUtility;
import org.poc.core.model.AbstractBaseEntity;
import org.poc.core.model.BaseEntity;
import org.poc.core.model.ResourceFailureRecovery;
import org.poc.core.repository.BaseRepository;
import org.poc.core.repository.ResourceFailureRecoveryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import ca.uhn.fhir.context.FhirContext;

/**
 * @author jawath,sankha
 */
public class BaseService<T> implements IService<T> {

	Logger logger = LoggerFactory.getLogger(BaseService.class);
	protected CrudRepository<T, Serializable> dao;// id parameter should be generic id extents serializable	

	protected Class busClass;
	protected BaseRepository baseDao;
	protected KafkaTemplate<String, String> kafkaTemplate;
	protected AdminClient adminClient;
	protected ResourceFailureRecoveryRepository failureRecoveryRepository;
	/**
	 * @param kafkaTemplate the kafkaTemplate to set
	 */
	public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	/**
	 * @param adminClient the adminClient to set
	 */
	public void setAdminClient(AdminClient adminClient) {
		this.adminClient = adminClient;
	}

	//TODO: write elastic search template code.
	public BaseService() {
//		logger.info("---------initialising @ BaseService----" + getClass().getSimpleName());
	}

	public BaseService(CrudRepository dao) {
		this.dao = dao;
		
	}
	public BaseService(CrudRepository dao,KafkaTemplate<String,String> kafkaTemplate,AdminClient adminClient) {
		this.dao = dao;
		this.adminClient = adminClient;
		this.kafkaTemplate = kafkaTemplate;
		if(kafkaTemplate != null && StringUtils.isBlank(this.kafkaTemplate.getDefaultTopic())){
			this.kafkaTemplate.setDefaultTopic("test");
		}
		this.failureRecoveryRepository = new ResourceFailureRecoveryRepository();
		
	}

	public BaseService(CrudRepository dao, MongoTemplate mongoTemplate) {
		this.dao = dao;
		this.baseDao = new BaseRepository<>(mongoTemplate);
		
	}

	public BaseService(CrudRepository dao, MongoTemplate mongoTemplate,KafkaTemplate<String,String> kafkaTemplate,AdminClient adminClient) {
		this.dao = dao;
		this.baseDao = new BaseRepository<>(mongoTemplate);
		this.adminClient = adminClient;
		this.kafkaTemplate = kafkaTemplate;
		if(kafkaTemplate != null && StringUtils.isBlank(this.kafkaTemplate.getDefaultTopic())){
			this.kafkaTemplate.setDefaultTopic("test");
		}
		this.failureRecoveryRepository = new ResourceFailureRecoveryRepository(mongoTemplate);
	}
	

	@Override
	public List<ResourceFailureRecovery> findAllFailedData(){
		return	failureRecoveryRepository.findAll();
	}
	
	@Override
	public ResourceFailureRecovery findFailedDataById(String id){
		if(StringUtils.isNotBlank(id))
		return	failureRecoveryRepository.findById(id);
		else
			throw new BaseRuntimeException("id is blank or null", null, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public List<ResourceFailureRecovery> searchFailedData(JsonObject search) {
		return failureRecoveryRepository.search(search);
	}
	
	@Override
	@Deprecated
	public void processFailedDataById(String id) {
		if(StringUtils.isNotBlank(id)) {
		T objectToProcess = findById(id);
		boolean result = sendDataToKafka(objectToProcess);
		if(result)
			failureRecoveryRepository.deleteById(id);
		}else {
			throw new BaseRuntimeException("id should not be blank", null,HttpStatus.BAD_REQUEST);
		}
	}
	public List<T> findAll() {

		List<T> l = new ArrayList<>();
		Iterable<T> all = dao.findAll();
		all.forEach(e -> l.add(e));
		return l;
	}

	public List<T> search(String column, Object value) {
		// FIXME : Write the search code
		// Should call the another search method with params
		return null;
	}

	public List<T> goToPage(int pageNo) {
		List<T> list = goToPage(pageNo, 100);
		return list;
	}

	public List<T> goToPage(int pageNo, int size) {
		// FIXME: Write the query for specific page.
//        List<T> list =dao.goToPage(pageNo,size);
		return null;
	}

	public Page<T> searchPageable(String column, Object value, Pageable pageable) {
//        Page<T> page= dao.searchPageable(column, value, pageable);
		return null;
	}

	public List<T> search(String column, Object value, Object value2) {
		// FIXME : Write the search code
		return null;
	}

	public Page<T> searchPageable(String column, Object value, Object value2, Pageable pageable) {
		return null;
	}

	public Page<T> search(JsonObject object, Pageable pageable) {
//		//TODO -fix this -  move this to mongo repository generic repository level
//		Query query = new Query().with(pageable);
//		Set<Map.Entry<String, JsonElement>> entries = object.entrySet();
//		for(Map.Entry<String, JsonElement> entry:entries){
//			if(entry.getValue()!=null){
//				query.addCriteria(Criteria.where(entry.getKey()).regex(entry.getValue().toString()));
//				break;//TODO - fix me - should be possible to provide multiple options
//				//TODO - should be refectored !!!!!
//			}
//		}
//		List<T> results = template.find(query,busClass);
//		return PageableExecutionUtils.getPage(
//				results,
//				pageable,
//				() -> template.count(query, busClass)); //FIXME move code to repo
		return null;
	}

	public List<T> search(JsonObject object) {
		return baseDao.search(object);
	}

	public T findById(String id) {
		if (baseDao != null)
			return (T) baseDao.findById(id);
		else {

			Optional<T> byId = dao.findById(id);
			if (byId.isPresent())
				return byId.get();
			return null;
		}
	}

	public void setDao(CrudRepository dao) {
		this.dao = dao;
	}

	/**
	 * @param baseDao the baseDao to set
	 */
	public void setBaseDao(BaseRepository baseDao) {
		this.baseDao = baseDao;
	}
	

	public boolean sendDataToKafka(T object) {
		return sendDataToKafka(object,kafkaTemplate.getDefaultTopic());
	}
	
	public boolean sendDataToKafka(T object, String topic) {
		StringBuilder objectInString = new StringBuilder();
		ResourceFailureRecovery kafkaSendFailed = null;
		ResourceFailureRecovery[] arrayOfFailedResources = {kafkaSendFailed};
		
		if (object instanceof BaseResource) {
			BaseResource baseResource = (BaseResource) object; 
			objectInString.append(FhirContext.forDstu3().newJsonParser().encodeResourceToString(baseResource));
			kafkaSendFailed = new ResourceFailureRecovery(GeneralFhirUtility.trimResourceId(baseResource.getId()), object.getClass().getName(), "Kafka-Send", "", Instant.now().getEpochSecond());
			kafkaSendFailed.setFhir(true);
			kafkaSendFailed.setResourceType(baseResource.getIdElement().getResourceType());
		} else if (object instanceof org.hl7.fhir.r4.model.BaseResource) {
			org.hl7.fhir.r4.model.BaseResource baseResource = (org.hl7.fhir.r4.model.BaseResource) object;
			objectInString.append(FhirContext.forR4().newJsonParser()
					.encodeResourceToString(baseResource));
			kafkaSendFailed = new ResourceFailureRecovery(GeneralFhirUtility.trimResourceId(baseResource.getId()), object.getClass().getName(), "Kafka-Send", "", Instant.now().getEpochSecond());
			kafkaSendFailed.setFhir(true);
			kafkaSendFailed.setResourceType(baseResource.getIdElement().getResourceType());
		} else if (object instanceof AbstractBaseEntity) {
			kafkaSendFailed = new ResourceFailureRecovery(((AbstractBaseEntity) object).getId(), object.getClass().getName(), "Kafka-Send", "", Instant.now().getEpochSecond());
			try {
				objectInString.append(new ObjectMapper().writeValueAsString(object));
			} catch (JsonProcessingException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		if (StringUtils.isNotBlank(topic)) {
			try  {
				adminClient.listTopics(new ListTopicsOptions().timeoutMs(5000)).listings().get(); //FIXME: admin client can be autowired directly in base service. Check for kafkatemplate also. 
				
		        } catch (ExecutionException | InterruptedException ex) {
		            logger.error("Kafka is not available, timed out after {} ms", 5000);
		            kafkaSendFailed = setUpFailureObjectWithReason(object, kafkaSendFailed,"Exception in Kafka Send "+ex.getMessage());
					failureRecoveryRepository.save(kafkaSendFailed); //FIXME for postgre based data set. MongoTemplate wont be avaible. It should pickup the postgre concept . 
		            return false;
		        }
			try {
			ListenableFuture<SendResult<String, String>> kafkaSendResult =  kafkaTemplate.send(topic, objectInString.toString());
			kafkaSendResult.addCallback(success ->{
				logger.info("Sent message: {}",success);
			},failure -> {
				logger.error("Message Sending Failure: {}",failure.getMessage());
				ResourceFailureRecovery failedResource = setUpFailureObjectWithReason(object, arrayOfFailedResources[0],"Exception in Kafka Send "+failure.getMessage());
				failureRecoveryRepository.save(failedResource);
				kafkaTemplate.flush();
			});
			return kafkaSendResult.isDone();
			}catch (Exception e) {
				kafkaSendFailed = setUpFailureObjectWithReason(object, kafkaSendFailed,"Exception in Kafka Send "+e.getMessage());
				failureRecoveryRepository.save(kafkaSendFailed);
			}
			
		}else {
			kafkaSendFailed = setUpFailureObjectWithReason(object, kafkaSendFailed,"Topic is Blank");
			failureRecoveryRepository.save(kafkaSendFailed);
		}
		return false;
	}

	/**
	 * @param object
	 * @param kafkaSendFailed
	 * @return
	 */
	private ResourceFailureRecovery setUpFailureObjectWithReason(T object, ResourceFailureRecovery kafkaSendFailed, String reason) {
		if(kafkaSendFailed!=null) {
			kafkaSendFailed.setReason(reason);
		}else {
			kafkaSendFailed = new ResourceFailureRecovery(UUID.randomUUID().toString(), object.getClass().getName(), "Kafka-Send", "Resource Failure Object never Initialized", Instant.now().getEpochSecond());
		}
		return kafkaSendFailed;
	}
	public <S extends T> S save(S object) {
		ResourceFailureRecovery saveObjectFailed = null;
		try {
			boolean fhirUpdateFlag = true, abstractEntityUpdateFlag = true;
			StringBuilder objectInString = new StringBuilder();
			validateEntity(object);
			if (object instanceof IBaseResource && ((IBaseResource) object).getIdElement().isEmpty()) {
				((IBaseResource) object).setId(UUID.randomUUID().toString());
				fhirUpdateFlag = false;
			}
			
			setCurrentEpochDateToObject(object);
			if (object instanceof BaseResource || object instanceof AbstractBaseEntity) {
				HttpUtils.setHeadersToPayloadForSave(object);
				
				if(object instanceof BaseResource) {
					objectInString.append(FhirContext.forDstu3().newJsonParser().encodeResourceToString((BaseResource)object));
					saveObjectFailed = new ResourceFailureRecovery(GeneralFhirUtility.trimResourceId(((BaseResource) object).getId()), object.getClass().getName(), "Resource-Update", "", Instant.now().getEpochSecond());
				}
				else if(object instanceof AbstractBaseEntity) {
					objectInString.append(new ObjectMapper().writeValueAsString(object));
					saveObjectFailed = new ResourceFailureRecovery(GeneralFhirUtility.trimResourceId(((AbstractBaseEntity) object).getId()), object.getClass().getName(), "Resource-Update", "", Instant.now().getEpochSecond());
				}
				
				if(object instanceof AbstractBaseEntity && (((AbstractBaseEntity)object).getObjectId() == null || StringUtils.isBlank(((AbstractBaseEntity)object).getId()))){
					((AbstractBaseEntity)object).setId(UUID.randomUUID());
					abstractEntityUpdateFlag = false;
				}
				
			} else if (object instanceof org.hl7.fhir.r4.model.BaseResource) {
				HttpUtilsR4.setHeadersToPayloadForSave(object);
				objectInString.append(FhirContext.forR4().newJsonParser().encodeResourceToString((org.hl7.fhir.r4.model.BaseResource)object));
				saveObjectFailed = new ResourceFailureRecovery(GeneralFhirUtility.trimResourceId(((org.hl7.fhir.r4.model.BaseResource) object).getId()), object.getClass().getName(), "Resource-Update", "", Instant.now().getEpochSecond());
			}
			S objectSaved = dao.save(object);
			if(kafkaTemplate!=null && fhirUpdateFlag && abstractEntityUpdateFlag && objectInString.length() > 0) {
//				kafkaTemplate.sendDefault(objectInString.toString());
			}
			return objectSaved;
		} catch (Exception e) {
			saveObjectFailed = setUpFailureObjectWithReason(object, saveObjectFailed,"Exception in Resource Save "+e.getMessage());
			failureRecoveryRepository.save(saveObjectFailed);
			throw new DataException("persistance error from save", e);
		}
	}

	private <S extends T> void setCurrentEpochDateToObject(S object) {
		if (object instanceof BaseResource) {
			((BaseResource) object).setProperty("updatedDate",new DecimalType(Instant.now().getEpochSecond()));
			if (((BaseResource) object).getNamedProperty("createdDate") == null ) {
				Property dateProp = ((BaseResource) object).getNamedProperty("createdDate");
//				FIXME: work on meta information about created date.
			}
//			((BaseResource) object).setUpdatedDate(new DecimalType(Instant.now().getEpochSecond()));
//			if (((BaseResource) object).getCreatedDate() == null || (((BaseResource) object).getCreatedDate() != null
//					&& ((BaseResource) object).getCreatedDate().isEmpty())) {
//				((BaseResource) object).setCreatedDate(new DecimalType(Instant.now().getEpochSecond()));
//			}
		} else if (object instanceof org.hl7.fhir.r4.model.BaseResource) {
//			TODO: Implement for R4 models
//			if (((org.hl7.fhir.r4.model.BaseResource) object).getCreatedDate() == null
//					|| (((org.hl7.fhir.r4.model.BaseResource) object).getCreatedDate() != null
//							&& ((BaseResource) object).getCreatedDate().isEmpty())) {
//				((org.hl7.fhir.r4.model.BaseResource) object)
//						.setCreatedDate(new org.hl7.fhir.r4.model.DecimalType(Instant.now().getEpochSecond()));
//			}
		}

	}

	public <S extends T> List<S> saveAll(List<S> object) {
		try {
			List<S> persisted = new ArrayList<>();
			for (S subObject : object) {
				validateEntity(subObject);
				if (object instanceof IBaseResource && ((IBaseResource) object).getIdElement().isEmpty()) {
					((IBaseResource) object).setId(UUID.randomUUID().toString());
				}
				setCurrentEpochDateToObject(subObject);
				if (object instanceof BaseResource) {
					HttpUtils.setHeadersToPayloadForSave(object);
				} else if (object instanceof org.hl7.fhir.r4.model.BaseResource) {
					HttpUtilsR4.setHeadersToPayloadForSave(object);
				}
				persisted.add(dao.save(subObject));

			}
			return persisted;

		} catch (Exception e) {
			throw new DataException("Persistance error from save", e);
		}
	}

	public T create(T object) {
		if (object instanceof IBaseResource) {
			try {
				validateEntity(object);
				if (object instanceof IBaseResource) {
					((IBaseResource) object).setId(UUID.randomUUID().toString());
					if (object instanceof BaseResource) {
						HttpUtils.setHeadersToPayloadForSave(object);
//						((BaseResource) object)
//								.setCreatedBy(new StringType(HttpUtils.getHeader(HttpHeaders.USER_NAME)));
//						((BaseResource) object).setCreatedDate(new DecimalType(Instant.now().getEpochSecond()));
					} else if (object instanceof org.hl7.fhir.r4.model.BaseResource) {
						HttpUtilsR4.setHeadersToPayloadForSave(object);
//						((org.hl7.fhir.r4.model.BaseResource) object).setCreatedBy(
//								new org.hl7.fhir.r4.model.StringType(HttpUtils.getHeader(HttpHeaders.USER_NAME)));
//						((org.hl7.fhir.r4.model.BaseResource) object)
//								.setCreatedDate(new org.hl7.fhir.r4.model.DecimalType(Instant.now().getEpochSecond()));
					}
				}
				return dao.save(object);
			} catch (Exception e) {
				throw new DataException("Persistance error from create", e);
			}

		} else if (object instanceof BaseEntity) {
			final BaseEntity base = (BaseEntity) object;
			if (base.getId() != null) {
				throw new DataException(
						"Creation object's payload" + " should not contain id " + "  entity error this is save ");

			}
//            else if (base != null) {
//                throw new DataException("its looks like entity aleady eixists");
//            }
			return save(object);
		}else if (object instanceof AbstractBaseEntity) {
			final AbstractBaseEntity abstractBase = (AbstractBaseEntity)object;
			if(abstractBase.getId() != null)
				throw new DataException(
						"Creation object's payload" + " should not contain id " + "  entity error this is save ");
			if (abstractBase.getCreatedBy() == null || abstractBase.getCreatedBy() != null
					&& StringUtils.isBlank(abstractBase.getCreatedBy()))
				abstractBase.setCreatedBy(HttpUtils.getHeader(HttpHeaders.USER_NAME));
				abstractBase.setCreatedAt(Instant.now().getEpochSecond());
			return save(object);
		}
		throw new DataException("Its looks like entity is not a base Entity");

	}

	public T update(T objectTopatch, String id) {
		StringBuilder objectInString = new StringBuilder();
		T object = null;
		if (baseDao != null) {
			if (objectTopatch instanceof BaseResource) {
//				((BaseResource) objectTopatch).setUpdatedBy(new StringType(HttpUtils.getHeader(HttpHeaders.USER_NAME)));
//				((BaseResource) objectTopatch).setUpdatedDate(new DecimalType(Instant.now().getEpochSecond()));
				objectInString.append(FhirContext.forDstu3().newJsonParser().encodeResourceToString((BaseResource)objectTopatch));
			} else if (objectTopatch instanceof org.hl7.fhir.r4.model.BaseResource) {
//				((org.hl7.fhir.r4.model.BaseResource) objectTopatch)
//						.setUpdatedBy(new org.hl7.fhir.r4.model.StringType(HttpUtils.getHeader(HttpHeaders.USER_NAME)));
//				((org.hl7.fhir.r4.model.BaseResource) objectTopatch)
//						.setUpdatedDate(new org.hl7.fhir.r4.model.DecimalType(Instant.now().getEpochSecond()));
				objectInString.append(FhirContext.forR4().newJsonParser().encodeResourceToString((org.hl7.fhir.r4.model.BaseResource)objectTopatch));
			}
			object = (T) baseDao.update(objectTopatch, id);
		} else {
			if (objectTopatch == null)
				throw new DataException("there should exist  an object already");
			if (StringUtils.isNotBlank(id) && dao.existsById(id)) {
				if (objectTopatch instanceof BaseEntity) {
					((BaseEntity) objectTopatch).setId(id);
				} else if (objectTopatch instanceof IBaseResource) {
					((IBaseResource) objectTopatch).setId(id);
				}else if (objectTopatch instanceof AbstractBaseEntity) {
					((AbstractBaseEntity) objectTopatch).setId(id);
					((AbstractBaseEntity) objectTopatch).setUpdatedBy(HttpUtils.getHeader(HttpHeaders.USER_NAME));
					((AbstractBaseEntity) objectTopatch).setUpdatedAt(Instant.now().getEpochSecond());
				}
				object = save(objectTopatch);
				try {
					objectInString.append(new ObjectMapper().writeValueAsString(object));
				} catch (JsonProcessingException e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				logger.info("Successfully updated object " + objectTopatch);
			}
		}
		if(kafkaTemplate != null && objectInString.length() > 0) {
//			kafkaTemplate.sendDefault(objectInString.toString());
		}
		return object;
	}

	public T patch(T objectTopatch, String id) {

		if (objectTopatch == null)
			throw new DataException("There should exist  an object already");
		if (StringUtils.isNotBlank(id) && dao.existsById(id) && objectTopatch instanceof IBaseResource) {
			((IBaseResource) objectTopatch).setId(id);
			T ob = save(objectTopatch);
			logger.info("Successfully updated object " + objectTopatch);
			return ob;
		}
		return null;
	}

	public void deleteById(Serializable objId) {
		if (!dao.existsById(objId))
			throw new DataException("There should exist  an object already");
		dao.deleteById(objId);
	}

	public void delete(T objId) {
		if (objId == null)
			throw new DataException("There should exist  an object already");
		dao.delete(objId);
	}

	public void validateEntity(T object) {
		// http://docs.jboss.org/hibernate/validator/4.1/reference/en-US/html/programmaticapi.html#example-constraint-mapping
		// this explains the dynamic validation config machanism
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void setBusinessClass(Class cls) {
		this.busClass = cls;
		if (this.baseDao != null)
			this.baseDao.setClazz(cls);
	}
	
}
