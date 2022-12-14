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

import org.poc.core.exceptions.BaseRuntimeException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;


public class ResourceSerializer extends JsonSerializer<IBaseResource>{
	@Autowired
	private FhirContext fhirContext;
	
	private static Logger logger = LoggerFactory.getLogger(ResourceSerializer.class);

	@Override
	public void serialize(IBaseResource resource, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
//		logger.info("In resource serialize");
		try {
			IParser jsonParser = fhirContext.newJsonParser();
			String encoded = jsonParser.encodeResourceToString(resource);
//			logger.info("ResourceString:" + encoded);
			
			generator.writeRawValue(encoded);
		} catch (DataFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BaseRuntimeException("ResourceSerializer: exception occurred while parsing "+
					resource.getClass(),e);
		}	
	}
}
