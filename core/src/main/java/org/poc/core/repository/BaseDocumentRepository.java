package org.poc.core.repository;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import org.poc.core.base.rest.util.HttpUtils;
import org.poc.core.constants.HttpHeaders;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;


public class BaseDocumentRepository {

	protected GridFsTemplate gridFsTemplate;
	Logger logger = LoggerFactory.getLogger(BaseDocumentRepository.class);


	@Autowired
	GridFsOperations gridFsOperations;
	
	public BaseDocumentRepository(GridFsTemplate gridFsTemplate) {
		this.gridFsTemplate = gridFsTemplate;
	}

	public String storeFile(InputStream inputStream, String filename, String contentType) {
		DBObject metaData = new BasicDBObject();
		metaData.put("user", HttpUtils.getHeader(HttpHeaders.USER_NAME));
		metaData.put("organization", HttpUtils.getHeader(HttpHeaders.ORG_CODE));
		metaData.put("unit", HttpUtils.getHeader(HttpHeaders.UNIT_CODE));
		metaData.put("version",Instant.now().getEpochSecond());
		try {
			metaData.put("size", inputStream.available());
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return storeFile(inputStream, filename, contentType, metaData);
	}

	public String storeFile(InputStream inputStream, String filename, String contentType, DBObject metaData) {
		return gridFsTemplate.store(inputStream, filename, contentType, metaData).toString();
	}

	public List<GridFSFile> findAllFiles() {
		return gridFsTemplate.find(new Query()).into(new ArrayList());
	}

	/**
	 * patterns can be <filename>* or *.<extension>
	 * @param pattern
	 * @return
	 */
	public List<GridFsResource> findAllResourcesByPattern(String pattern) {
		return Arrays.asList(gridFsTemplate.getResources(pattern));
	}

	/**
	 * <p>
	 * The GridFSFile API is quite simple as well:
	 * <ul>
	 * <li>getFilename – gets the filename of the file </li> 
	 * <li>getMetaData – gets the metadata for the given file</li> 
	 * <li>containsField – determines if the document contains a field with the given name</li> 
	 * <li>get – gets a field from the object by name </li> 
	 * <li>getId – gets the file's object ID </li>
	 * <li>keySet – gets the object's field names</li>
	 * </ul>
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	public GridFSFile findFileById(String id) {
		return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
	}
	//TODO: work for latest version file resource using meta version. query.with(new Sort(Sort. "version"))
	public GridFsResource getResource(String fileName) {
		return gridFsTemplate.getResource(fileName);
	}

	public void deleteFileById(String id) {
		gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
	}


	
}
