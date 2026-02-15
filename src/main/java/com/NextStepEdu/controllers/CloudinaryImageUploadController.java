package com.NextStepEdu.controllers;

import com.NextStepEdu.services.CloudinaryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloud/upload")
public class CloudinaryImageUploadController {
    @Autowired

    private CloudinaryImageService  cloudinaryImageService;

    @PostMapping
    public ResponseEntity<Map> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
         Map data =   this.cloudinaryImageService.upload(file);
         return new ResponseEntity<>(data, HttpStatus.OK);
        }

}
