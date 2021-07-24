package com.example.demo.service.serviceimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.UploadedFile;
import com.example.demo.repository.FileUploadRepository;
import com.example.demo.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService{

	private String uploadFolderPath = "/teste/";
	@Autowired
    private FileUploadRepository fileUploadRepository;
	
	@Override
	public void uploadToLocal(MultipartFile file) {
		try {
			byte[] data = file.getBytes();
			Path path = Paths.get(uploadFolderPath + file.getOriginalFilename());
			Files.write(path, data);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public UploadedFile uploadToDb(MultipartFile file) {
		UploadedFile uploadedFile = new UploadedFile();
		try {
            uploadedFile.setFileData(file.getBytes());
            uploadedFile.setFileType(file.getContentType());
            uploadedFile.setFileName(file.getOriginalFilename());
            this.uploadToLocal(file);
            UploadedFile uploadedFileToRet = fileUploadRepository.save(uploadedFile);
            return uploadedFileToRet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	@Override
	public UploadedFile downloadFile(Long fileId) {
		UploadedFile uploadedFileToRet = fileUploadRepository.getOne(fileId);
        return uploadedFileToRet;
	}
}
