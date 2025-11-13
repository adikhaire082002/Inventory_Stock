package com.aditya.inventory.controller;


import com.aditya.inventory.dto.BaseResponse;
import com.aditya.inventory.dto.BaseResponseDto;
import com.aditya.inventory.entity.Dealer;
import com.aditya.inventory.entity.FileData;
import com.aditya.inventory.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Value("${project.image}")
    private String path;

    @PreAuthorize("hasRole('Dealer')")
    @PostMapping("/upload")
    public ResponseEntity<BaseResponseDto> uploadImage(@RequestParam("file") List<MultipartFile> file, Integer product_Id, Authentication authentication)throws IOException {
        List<FileData> fileData = imageService.uploadImages(product_Id, path, file, authentication);
        BaseResponseDto response= new BaseResponseDto(HttpStatus.OK,"Images uploaded successfully",fileData,new Date());
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/images/")
    public void downloadlmage(@RequestParam String imageName, @RequestParam String dealerId , HttpServletResponse response) throws IOException {
        Path path1 = Paths.get(path,dealerId, imageName);
        response.setContentType(Files.probeContentType(path1));
        Files.copy(path1, response.getOutputStream());

    }
}
