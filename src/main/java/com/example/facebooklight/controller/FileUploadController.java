package com.example.facebooklight.controller;

import com.example.facebooklight.model.ResponseData;
import com.example.facebooklight.repo.ImagesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@RestController()
public class FileUploadController {

    @Autowired
    ImagesRepo imagesRepo;

    @PostMapping(value = "/fileUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<String>> fileUpload(@RequestParam("file") MultipartFile file) {
        File converFile = new File("D:/work/"+file.getOriginalFilename());
        try {
            converFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(converFile);
            fout.write(file.getBytes());

            return new ResponseEntity<>(new ResponseData<>("/images/" + converFile.getName(), "success", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
