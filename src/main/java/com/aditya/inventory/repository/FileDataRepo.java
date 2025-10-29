package com.aditya.inventory.repository;

import com.aditya.inventory.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDataRepo extends JpaRepository<FileData, Long> {
}
