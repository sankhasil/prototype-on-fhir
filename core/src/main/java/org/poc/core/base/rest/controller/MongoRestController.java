package org.poc.core.base.rest.controller;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.poc.core.model.ResourceFailureRecovery;
import org.poc.core.service.BaseService;
import org.poc.core.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.JsonObject;

/**
 * 
 * @author Sankha Sil
 *
 * @param <T> parameterized Fhir Object
 */
public class MongoRestController<T> {

	protected Logger logger = LoggerFactory.getLogger(MongoRestController.class);
	protected IService<T> service;
	protected Class busClass;

	private MongoRestController() {
		this.service = new BaseService<>();
		setParameterisedBusinessClass();
	}

	@Autowired
	public MongoRestController(IService service) {
		this();
		this.service = service;
		logger = LoggerFactory.getLogger(service.getClass());
		if (service != null) {
			service.setBusinessClass(busClass);
		}
	}

	@CrossOrigin
	@GetMapping(value = { "", "/" }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public Iterable<?> findAll() {
		logger.info(">>>>>> Find All " + busClass.getSimpleName());
		return service.findAll();
	}

	@CrossOrigin
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, value = "/{id}")
	public T findById(@PathVariable("id") String id) {
		logger.info(">>>>>> Find " + busClass.getSimpleName() + ":" + id);
		T byId = service.findById(id);
		return byId;
	}

	@PostMapping(value = { "/", "" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public T create(@RequestBody T object) {

//		logger.info(">>>>> Creating  " + busClass.getSimpleName());
		return service.create(object);
	}

	@PutMapping(value = "/{id}")
	public T update(@PathVariable("id") String id, @RequestBody T object) {
		// todo - update should find perticular object then update the object
		logger.info(">>>>> Updating " + busClass.getSimpleName());
		return service.update(object, id);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Serializable id) {
		logger.info(">>>>> Deleting " + busClass.getSimpleName() + ":" + id);
		service.deleteById(id);
	}

	@PostMapping(value = "/search", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<T> search(@RequestBody JsonObject object) {
		return service.search(object);
	}

	@GetMapping(value = { "/resourceRecovery/{id}", "/ResourceRecovery/{id}" })
	public ResourceFailureRecovery getFailedDataById(@PathVariable("id") String id) {
		return service.findFailedDataById(id);
	}

	@GetMapping(value = { "/resourceRecovery", "/resourceRecovery/" })
	public List<ResourceFailureRecovery> getAllFailedData() {
		return service.findAllFailedData();
	}

	@PostMapping(value = "/resourceRecovery/search", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public List<ResourceFailureRecovery> searchFailedData(@RequestBody JsonObject object) {
		return service.searchFailedData(object);
	}

	@PostMapping(value = { "/resourceRecovery/{id}", "/ResourceRecovery/{id}" })
	public ResponseEntity<String> processFailedDataById(@PathVariable("id") String id) {
		service.processFailedDataById(id);
		return ResponseEntity.ok().build();
	}

	

	private void setParameterisedBusinessClass() {
		Type genericSuperclass = getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length > 0) {
				busClass = ((Class) (actualTypeArguments[0]));
			}
		}
	}

}
