package com.NextStepEdu.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryImageService {

    Map<String, Object> upload(MultipartFile file) throws IOException;
}
