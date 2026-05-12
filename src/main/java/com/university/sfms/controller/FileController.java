package com.university.sfms.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.university.sfms.service.CloudinaryService;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final CloudinaryService service;

    public FileController(CloudinaryService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','FACULTY','STUDENT')")
    public Map<String, String> upload(@RequestParam MultipartFile file, @RequestParam(defaultValue = "uploads") String folder) {
        return Map.of("secureUrl", service.upload(file, folder));
    }
}
