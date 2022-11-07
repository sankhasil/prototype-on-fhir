/*
 * Copyright 2015 Vizuri, a business division of AEM Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.poc.core.fhir.converter;

import java.io.IOException;

import org.bson.Document;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

public  class AbstractMongoConverter<T1,T2> implements Converter<T1, T2>  {
	private static Logger logger = LoggerFactory.getLogger(AbstractMongoConverter.class);

	@Autowired
	private ObjectMapper objectMapper;

	private Class domainClass;

	public AbstractMongoConverter() {
		Class  returnClassType = (Class<T2>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
		this.domainClass = returnClassType;
	}

	public void setDomainClass(Class domainClass) {
		this.domainClass = domainClass;
	}

	public IBaseResource convertToResource(ObjectMapper objectMapper, Document dbObject, Class<?> type) {
//		logger.info("Converting dbObject to Resource:"+ dbObject);
		String json = dbObject.toJson();
//		logger.info("JSON:" + json);

		IBaseResource resource = null;

		try {
			resource = (IBaseResource)objectMapper.readValue(json, type);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resource;
	}

	public Document convertToDocument(ObjectMapper objectMapper, IBaseResource resource) {
//		logger.info("Converting Resource to DBObject:" + resource);

		String json = "";
		try {
			json = objectMapper.writeValueAsString(resource);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		logger.info("ResourceJSON:" + json);

		Document o = new Document();

		Document dbObj = o.parse(json);
		dbObj.put("_id", resource.getIdElement().getIdPart());

		return dbObj;
	}

	@Override
	public T2 convert(T1 t1) {
		if(t1 instanceof IBaseResource){
			return (T2) convertToDocument(objectMapper,(IBaseResource) t1);
		}else if(t1 instanceof Document){

			return (T2) convertToResource(objectMapper,(Document) t1,this.domainClass);
		}
		return null;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
}

