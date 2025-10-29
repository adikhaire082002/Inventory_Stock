package com.aditya.inventory.serviceImpl;

import com.aditya.inventory.entity.Dealer;
import com.aditya.inventory.entity.FileData;
import com.aditya.inventory.entity.Product;
import com.aditya.inventory.repository.DealerRepo;
import com.aditya.inventory.repository.FileDataRepo;
import com.aditya.inventory.repository.ProductRepo;
import com.aditya.inventory.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    FileDataRepo fileDataRepo;

    @Autowired
    DealerRepo  dealerRepo;

    @Autowired
    ProductRepo  productRepo;

    @Override
    public FileData uploadImagetoFile(String path, MultipartFile file, Authentication authentication) throws IOException {


        return null;

    }

    @Override
    public List<FileData> uploadImages(Integer productId, String path, List<MultipartFile> files, Authentication authentication) throws IOException {
        List<FileData> fileDataList = new ArrayList<>();
        String email = authentication.getName();
        Dealer dealer = dealerRepo.findByEmail(email);
        String dealerId = dealer.getDealer_id();
        Product product = productRepo.findById(productId).get();

        for(MultipartFile file : files){


            String name = file.getOriginalFilename();
            String uniqueName= System.currentTimeMillis()+"_"+name;
            String folderPath = path + File.separator+ dealerId;

            File dir = new File(folderPath);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String finalPath=folderPath+File.separator+uniqueName;

            Files.copy(file.getInputStream(), Paths.get(finalPath));

            FileData fileData = new FileData();
            fileData.setName(uniqueName);
            fileData.setFilePath(finalPath);
            fileData.setType(file.getContentType());
            fileData.setProduct(product);
            FileData save = fileDataRepo.save(fileData);
            fileDataList.add(save);

        }

        product.setImages(fileDataList);
        productRepo.save(product);

        return fileDataList;
    }
}
