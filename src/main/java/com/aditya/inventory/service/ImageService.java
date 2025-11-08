package com.aditya.inventory.service;

import com.aditya.inventory.entity.FileData;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ImageService {

    FileData uploadImagetoFile(String path, MultipartFile file, Authentication authentication)throws IOException;

    List<FileData> uploadImages(Integer productId,String path, List<MultipartFile> file, Authentication authentication)throws IOException;

    InputStream getImage(String path,String filename) throws FileNotFoundException;

}
