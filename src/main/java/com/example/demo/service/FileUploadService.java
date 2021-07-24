package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.UploadedFile;

public interface FileUploadService {

	public void uploadToLocal(MultipartFile file);
	public UploadedFile uploadToDb(MultipartFile file);
    public UploadedFile downloadFile(Long fileId); 
	
	
}
