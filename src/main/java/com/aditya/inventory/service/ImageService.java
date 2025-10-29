package com.aditya.inventory.service;

import com.aditya.inventory.entity.FileData;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    FileData uploadImagetoFile(String path, MultipartFile file, Authentication authentication)throws IOException;

    List<FileData> uploadImages(Integer productId,String path, List<MultipartFile> file, Authentication authentication)throws IOException;

}
