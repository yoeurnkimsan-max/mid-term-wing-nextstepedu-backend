package com.NextStepEdu.services.impl;

import com.NextStepEdu.services.CloudinaryImageService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> upload(MultipartFile file) throws IOException {

        try {
            Map<String, Object> data = (Map<String, Object>) (Map<?, ?>) this.cloudinary.uploader()
                    .upload(file.getBytes(), Map.of());
            return data;
        } catch (IOException e) {
            throw new RemoteException("Image upload failed");
        }
    }
}
