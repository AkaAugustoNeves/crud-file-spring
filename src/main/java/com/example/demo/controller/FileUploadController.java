package com.example.demo.controller;

import java.io.IOException;
import java.util.zip.ZipException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.controller.dto.FileUploadResponse;
import com.example.demo.model.UploadedFile;
import com.example.demo.service.FileUploadService;

@RestController
@RequestMapping("upload")
public class FileUploadController {

	@Autowired
    private FileUploadService fileUploadService;
	
	@PostMapping("/local")
	public void uploadLocal(@RequestParam("file") MultipartFile multipartFile) throws IOException, ZipException {
		fileUploadService.uploadToLocal(multipartFile);
	}
	
	@PostMapping("/db")
    public FileUploadResponse uploadDb(@RequestParam("file")MultipartFile multipartFile)
    {
       UploadedFile uploadedFile = fileUploadService.uploadToDb(multipartFile);
       FileUploadResponse response = new FileUploadResponse();
       if(uploadedFile!=null){
           String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                   .path("upload/download/")
                   .path(uploadedFile.getFileId().toString())
                   .toUriString();
           response.setDownloadUri(downloadUri);
           response.setFileId(uploadedFile.getFileId());
           response.setFileType(uploadedFile.getFileType());
           response.setUploadStatus(true);
           response.setMessage("File Uploaded Successfully!");
           return response;

       }
       response.setMessage("Oops 1 something went wrong please re-upload.");
       return response;
    }
	@GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id)
    {
        UploadedFile uploadedFileToRet =  fileUploadService.downloadFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(uploadedFileToRet.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= "+uploadedFileToRet.getFileName())
                .body(new ByteArrayResource(uploadedFileToRet.getFileData()));
    }
	
	
	
}
