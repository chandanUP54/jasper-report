package com.example.jasper_demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ReportData {
    @Id
    private Long id;
    private String name;
    private String category;

    // Constructors, Getters, Setters
}