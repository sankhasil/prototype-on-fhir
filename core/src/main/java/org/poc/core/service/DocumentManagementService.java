package org.poc.core.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.poc.core.repository.BaseDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

public class DocumentManagementService {

	Logger logger = LoggerFactory.getLogger(DocumentManagementService.class);
	protected BaseDocumentRepository baseDocDao;

	public DocumentManagementService(GridFsTemplate gridFsTemplate) {
		this.baseDocDao = new BaseDocumentRepository(gridFsTemplate);
	}

	public void storeFile(MultipartFile file) {
		try {
			InputStream fileInputStream = file.getInputStream();
			baseDocDao.storeFile(fileInputStream, file.getName(), file.getContentType());
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public GridFsResource findFileById(String id) {
		return baseDocDao.getResource(baseDocDao.findFileById(id).getFilename());
	}

	public GridFsResource findFileByName(String name) {
		return baseDocDao.getResource(name);
	}

}
