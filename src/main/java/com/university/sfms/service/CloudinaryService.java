package com.university.sfms.service;

import com.cloudinary.Cloudinary;
import com.university.sfms.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(MultipartFile file, String folder) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), Map.of(
                    "folder", "sfms/" + folder,
                    "resource_type", "auto",
                    "type", "authenticated"
            ));
            Object secureUrl = result.get("secure_url");
            if (secureUrl == null) throw new ApiException(HttpStatus.BAD_GATEWAY, "Cloudinary did not return a secure URL");
            return secureUrl.toString();
        } catch (IOException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "File upload failed");
        }
    }
}
