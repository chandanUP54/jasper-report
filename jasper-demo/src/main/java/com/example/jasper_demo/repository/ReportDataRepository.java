package com.example.jasper_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jasper_demo.entity.ReportData;

@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, Long> {
    List<ReportData> findByCategory(String category);
}
