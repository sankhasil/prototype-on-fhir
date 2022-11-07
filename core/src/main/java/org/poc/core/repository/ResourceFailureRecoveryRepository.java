/**
 * 
 */
package org.poc.core.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.poc.core.exceptions.BaseRuntimeException;
import org.poc.core.model.ResourceFailureRecovery;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Sankha
 *
 */
public class ResourceFailureRecoveryRepository {
	private static final String FAILURE_RECOVERY_FAILED_FILE_LOCATION = "failureRecovery/failed.dat";
	private static final String FAILURE_RECOVERY_FOLDER = "failureRecovery";
	protected MongoTemplate mongoTemplate;
	protected List<ResourceFailureRecovery> listOfEntries;
	/**
	 * 
	 */
	
	public ResourceFailureRecoveryRepository() {
		if(Files.notExists(Paths.get(FAILURE_RECOVERY_FOLDER),LinkOption.NOFOLLOW_LINKS)) {
			try {
				Files.createDirectory(Paths.get(FAILURE_RECOVERY_FOLDER));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		loadDataToFailureRecoveryList();
	}

	/**
	 * 
	 */
	private void loadDataToFailureRecoveryList() {
		if (Files.exists(Paths.get(FAILURE_RECOVERY_FOLDER))) {
			try {
				String pwd = System.getProperty("user.dir");
				Path saveFilePath = Paths.get(pwd, FAILURE_RECOVERY_FAILED_FILE_LOCATION);
				FileInputStream fileInputStream = new FileInputStream(saveFilePath.toFile());
				ObjectInputStream objectStream = new ObjectInputStream(fileInputStream);
				listOfEntries = (List<ResourceFailureRecovery>) objectStream.readObject();
				objectStream.close();
				fileInputStream.close();
			}catch(Exception e) {
				
			}finally {
				
			}
		}
	}
	
	private void saveToDataFile(ResourceFailureRecovery object)  {
		if (Files.exists(Paths.get(FAILURE_RECOVERY_FOLDER))) {
			try {
				String pwd = System.getProperty("user.dir");
				Path saveFilePath = Paths.get(pwd, FAILURE_RECOVERY_FAILED_FILE_LOCATION);
				FileOutputStream fileOutputStream = new FileOutputStream(saveFilePath.toFile());
				ObjectOutputStream objectStream = new ObjectOutputStream(fileOutputStream);
				if(listOfEntries == null || listOfEntries.isEmpty())
					listOfEntries = new ArrayList<ResourceFailureRecovery>();
				if(listOfEntries.contains(object))
					listOfEntries.remove(object);
				listOfEntries.add(object);
				objectStream.writeObject(listOfEntries);
				objectStream.close();
				fileOutputStream.close();
			} catch (Exception e) {
				throw new RuntimeException("Data File save problem " + e.getMessage(), e);
			}
		}
	}
	public ResourceFailureRecoveryRepository(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	public List<ResourceFailureRecovery> findAll() {
		if(mongoTemplate !=null)
			return mongoTemplate.findAll(ResourceFailureRecovery.class);
		else {
			loadDataToFailureRecoveryList();
			return listOfEntries;
		}
	}

	public ResourceFailureRecovery findById(String id) {
		if(mongoTemplate !=null)
		return  mongoTemplate.findById(id, ResourceFailureRecovery.class);
		else {
			loadDataToFailureRecoveryList();
			return listOfEntries.stream().filter(resource -> resource.getId().equals(id)).findFirst().orElse(null);
		}
	}

	public ResourceFailureRecovery save(ResourceFailureRecovery object) {
		if(mongoTemplate != null)
			mongoTemplate.save(object);
		else
			saveToDataFile(object);
		return object;
	}
	public List<ResourceFailureRecovery> search(JsonObject object) {
		Set<Map.Entry<String, JsonElement>> entries = object.entrySet();
		if(mongoTemplate != null) {
		Query query = new Query();
		for (Map.Entry<String, JsonElement> entry : entries) {
			if (entry.getValue() != null) {
				switch (entry.getKey().toLowerCase()) {
				case "id":
				case "resourceName" :
				case "action":
					query.addCriteria(Criteria.where(entry.getKey().toLowerCase()).regex(Pattern.compile(entry.getValue().getAsString(),Pattern.CASE_INSENSITIVE)));
				default:
					break;
				}
			}
		}
		if(object.has("fromDate") && object.get("fromDate")!=null && object.has("toDate") && object.get("toDate")!=null) {
			query.addCriteria(Criteria.where("failureTimeStamp").gte(object.get("fromDate").getAsLong()).and("failureTimeStamp").lte(object.get("toDate").getAsLong()));
			
		}
		List<ResourceFailureRecovery> results = mongoTemplate.find(query, ResourceFailureRecovery.class);
		return results; }
		else {
			loadDataToFailureRecoveryList();
			List<ResourceFailureRecovery> results = listOfEntries.stream().filter(resource -> {
				boolean predicate = true;
				for (Map.Entry<String, JsonElement> entry : entries) {
					if (entry.getValue() != null) {
						switch (entry.getKey().toLowerCase()) {
						case "id":
							predicate = predicate && resource.getId().equals(entry.getValue());
							break;
						case "resourceName" :
							predicate = predicate && resource.getResourceName().equals(entry.getValue());
							break;
						case "action":
							predicate = predicate && resource.getAction().equals(entry.getValue());
							break;
						default:
							break;
						}
					}
				}
				if(object.has("fromDate") && object.get("fromDate")!=null && object.has("toDate") && object.get("toDate")!=null) {
					predicate = predicate && resource.getFailureTimeStamp().longValue() >= object.get("fromDate").getAsLong() && resource.getFailureTimeStamp().longValue() <=  object.get("toDate").getAsLong();
				}
				return predicate;
			}).collect(Collectors.toList());
			return results;
			
		}
	}
	
	public void delete(ResourceFailureRecovery obj) {
		if(obj !=null) {
			if(mongoTemplate!=null)
				mongoTemplate.remove(obj);
			else {
				if (Files.exists(Paths.get(FAILURE_RECOVERY_FOLDER))) {
					try {
						String pwd = System.getProperty("user.dir");
						Path saveFilePath = Paths.get(pwd, FAILURE_RECOVERY_FAILED_FILE_LOCATION);
						FileOutputStream fileOutputStream = new FileOutputStream(saveFilePath.toFile());
						ObjectOutputStream objectStream = new ObjectOutputStream(fileOutputStream);
						listOfEntries.remove(obj);
						objectStream.writeObject(listOfEntries);
						objectStream.close();
						fileOutputStream.close();
					}catch (Exception e) {
						throw new BaseRuntimeException(e.getMessage(), e,HttpStatus.INTERNAL_SERVER_ERROR);
					}
					}
			}
		}else {
			throw new BaseRuntimeException("Object can't be null", null,HttpStatus.BAD_REQUEST);
		}
	}
	
	public void deleteById(String id) {
		delete(findById(id));
	}
}
