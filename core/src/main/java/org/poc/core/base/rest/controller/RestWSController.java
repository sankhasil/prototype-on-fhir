package org.poc.core.base.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.poc.core.exceptions.DataException;
import org.poc.core.model.BaseEntity;
import org.poc.core.model.ResourceFailureRecovery;
import org.poc.core.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

//@RestController
//@RequestMapping("/ws/v1")
public class RestWSController<T> {


    private static Logger log = LoggerFactory.getLogger(RestController.class);
    protected IService<T> service;//will be setter injected
    protected Class busClass;
    protected static ObjectMapper om = new ObjectMapper();

    public RestWSController() {
        setParameterisedBusinessClass();

    }

    public RestWSController(IService service) {
        this();
        this.service = service;
    }

    @RequestMapping("/ping")
    protected String testRest(String txt) {
        log.info("pinged ..");
        return "rest is working";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody String requestBody) {
        log.info(getClass().getSimpleName() + ">>> " + RestController.class.getSimpleName()
                + " Rest controller Create method >> POST method ");
        //from the postquery ..create the  object

        T ob = getEntity(requestBody);// here that object should not contain the ID

        T resultObject = service.create(ob);
        log.info(getClass().getSimpleName() + ">>> " + RestController.class.getSimpleName()
                + " Rest controller Create method >> POST method  Object created ");


        return new ResponseEntity(resultObject, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<T> update(@RequestBody String requestBody,
            @PathVariable("id") Optional<String> id) {

        log.info(" Rest controller Update method >> PUT method ");
        T t = getEntity(requestBody);
        if (t == null) {
            throw new RuntimeException("put body cannot be empty");
        }
        if(t instanceof BaseEntity) {
            BaseEntity ob = (BaseEntity)t;

            if (id.isPresent() && !Objects.equals(ob.getId(), id.get())) {//conflict
                throw new RuntimeException("id conflicts with url id and json payload");
            }
            if (!id.isPresent() && Objects.isNull(ob.getId())) {
                log.info(" Rest controller update method >> PUT method service creation is called");
                T resultObject = service.create(t);
                return new ResponseEntity(resultObject, HttpStatus.OK);
            }
            if (Objects.isNull(ob.getId()) || id.isPresent()) {
                ob.setId(id.get());
            }

            T resultObject = service.update(t,id.get());
            log.info(" Rest controller update method >> PUT method service update is called");
            return new ResponseEntity(resultObject, HttpStatus.OK);

        }
        return null;//todo ???
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<T> patch(@RequestBody String requestBody,
            @PathVariable("id") String id) {

        T ob = getEntity(requestBody);
        if (ob == null) {
            throw new RuntimeException("put body cannot be empty");
        }
        if (Objects.isNull(id)) {//conflict
            throw new RuntimeException("id conflicts with url id and json payload");
        }
        if (!Objects.isNull(id) && ob instanceof BaseEntity && !Objects.equals(((BaseEntity)ob).getId(), id)) {//conflict
            throw new RuntimeException("id conflicts with url id and json payload");
        }
        T resultObject = service.patch(ob,id);
        return new ResponseEntity(ob, HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) {
        log.info(" Rest controller update method >> delete method ");
        service.deleteById(id);
        log.info(" Rest controller update method >> deleteById method successful");
        return new ResponseEntity(HttpStatus.OK);

    }

//    @Autowired
    public void setService(IService service) {
        this.service = service;
    }

    @RequestMapping("")
    public List<T> getAll() {
        log.info(" Rest controller getAll method  ");
        return service.findAll();
    }

    @RequestMapping("/page/{pageNo}")
    public List<T> getAll(@PathVariable("pageNo") int pageNo, HttpServletRequest request) {
        log.info(" Rest controller getAll method paged ");
        return service.goToPage(pageNo);//limit
    }

    @RequestMapping("/{id}")
    public T get(@PathVariable("id") String id) {
        log.info(" Rest controller get method  "+id);
        return (T) service.findById(id);
    }

    @RequestMapping("/search")
    public List<T> search(HttpServletRequest request) {
        String column = request.getParameter("column");
        String value = request.getParameter("value");

        String operator = request.getParameter("operator");
        //operator = is by default
        if (operator == null || operator.trim().length() == 0) {
            operator = "=";
        }
        System.out.println(column + "  --  " + value + "  --  " + operator + "  --  ");
        if (StringUtils.isNotEmpty(column) && StringUtils.isNotEmpty(value)) {
            List<T> list = service.search(column, value);
            log.info(" Rest controller search method  "+column + "  "+ value);
            return list;
        }
        return null;
    }
	@GetMapping(value = { "/resourceRecovery/{id}", "/ResourceRecovery/{id}" })
	public ResourceFailureRecovery getFailedDataById(@PathVariable("id") String id) {
		return service.findFailedDataById(id);
	}
	 
	@GetMapping(value = { "/resourceRecovery", "/resourceRecovery/" })
	public List<ResourceFailureRecovery> getAllFailedData() {
		return service.findAllFailedData();
	}
	 
	@PostMapping(value = "/resourceRecovery/search", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<ResourceFailureRecovery> searchFailedData(@RequestBody JsonObject object) {
		return service.searchFailedData(object);
	}
	
	@PostMapping(value = { "/resourceRecovery/{id}","/ResourceRecovery/{id}"})
	public ResponseEntity<String> processFailedDataById(@PathVariable("id") String id){
		service.processFailedDataById(id);
		return ResponseEntity.ok().build();
	}
    @RequestMapping("/searchpage")
    public Page<T> searchPage(HttpServletRequest request) {
        String column = request.getParameter("column");
        String value = request.getParameter("value");
        int page = getValue(request.getParameter("page"),0);
        int size = getValue(request.getParameter("size"),50);

        String operator = request.getParameter("operator");
        //operator = is by default
        if (operator == null || operator.trim().length() == 0) {

            operator = "=";
        }
        if (StringUtils.isNotEmpty(column) && StringUtils.isNotEmpty(value)) {
            Page<T> shops = service.searchPageable(column, value, PageRequest.of(page,size));
            return shops;
        }
        return null;
    }

    protected   T getEntity(String payloadJson) {
      return (T) getValue(payloadJson,busClass);
    }

    protected static   <T> T getValue(String payloadJson,Class<T> busClass) {
        T ob = null;
        try {  //read JSON like DOM Parser
            JsonNode rootNode = om.readTree(payloadJson);
            JsonNode entityNode = rootNode.path("entity");
            if(entityNode!=null && entityNode.isMissingNode()){

                ob = om.readValue(rootNode.toString(), busClass);

            }else {
                ob = om.readValue(entityNode.toString(), busClass);

            }
            return ob;

        } catch (IOException ie) {
            throw new DataException("JSON Parsing Error  "+payloadJson,ie);
        }
//            catch (Exception e) {

//        //translate the exception to meaningful json payload /pojo representation with
//        // nessary parameter which can ease debugging / support / user friendly
//        if (e instanceof DataException) {
//
//        } else if (e instanceof ParsingException) {
//
//        }
//        throw new DataException(postQuery, e);// error handler which will hanlde this

//    }


    }

    public static <T> T getT(String payloadJson,String path,Class busClass){
        T ob = null;
        try {  //read JSON like DOM Parser
            JsonNode rootNode = om.readTree(payloadJson);
            JsonNode entityNode = rootNode.path(path);
            return  (T) om.readValue(entityNode.toString(),busClass);
        } catch (IOException ie) {
            throw new DataException("JSON Parsing Error  "+payloadJson,ie);
        }
    }

    public static Integer getValue(Object obj, int defaultValueIfNullOrEmpty) {
        if (obj == null) {
            return defaultValueIfNullOrEmpty;
        } else {

            try {
                return Integer.valueOf(obj.toString());

            } catch (Exception e) {
                throw e;
            }

        }
//    return defaultValueIfNullOrEmpty;
    }

    
    private void setParameterisedBusinessClass() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments =((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                busClass = ((Class) (actualTypeArguments[0]));
            }
        }
    }

}
