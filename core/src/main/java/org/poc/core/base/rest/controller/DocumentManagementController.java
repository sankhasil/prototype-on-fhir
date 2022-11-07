/**
 * 
 */
package org.poc.core.base.rest.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.poc.core.service.DocumentManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Sankha
 *
 */
@CrossOrigin
@RequestMapping(value={"/dm","/documentManagement","/documentmanagement"})
public class DocumentManagementController {
	Logger logger = LoggerFactory.getLogger(DocumentManagementController.class);
	DocumentManagementService service;
	
	public DocumentManagementController(DocumentManagementService service) {
		this.service = service;
	}
	@PostMapping(value = { "/uploadFile", "/UploadFile", "/uploadfile" })
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		service.storeFile(file);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = { "/bulkUploadFile", "/BulkUploadFile", "/bulkuploadfile" })
	public ResponseEntity<String> uploadBulkFiles(@RequestParam("fileList") List<MultipartFile> fileList) {
		if (fileList != null && !fileList.isEmpty()) {
			fileList.forEach(file -> service.storeFile(file));
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping(value = { "/getFile/{id}", "/GetFile/{id}", "/getfile/{id}" })
	public ResponseEntity<Resource> getFileById(@PathVariable("id") String id) {
		GridFsResource file = service.findFileById(id);
	try {
		Resource responseFile = new InputStreamResource(file.getInputStream());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.contentType(MediaType.parseMediaType(file.getContentType())).contentLength(file.contentLength()).body(responseFile);
	} catch (IllegalStateException | IOException e) {
		logger.error(ExceptionUtils.getStackTrace(e));
	}
	return ResponseEntity.badRequest().build();
		
	}
	@GetMapping(value = { "/searchFile", "/SearchFile", "/searchfile" })
	public ResponseEntity<Resource> searchFile(@RequestParam("name") String name) { //TODO: enhance this method for other possibilities 
		GridFsResource file = service.findFileByName(name);
	try {
		Resource responseFile = new InputStreamResource(file.getInputStream());
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.contentType(MediaType.parseMediaType(file.getContentType())).contentLength(file.contentLength()).body(responseFile);
	} catch (IllegalStateException | IOException e) {
		logger.error(ExceptionUtils.getStackTrace(e));
	}
	return ResponseEntity.badRequest().build();
		
	}

}
