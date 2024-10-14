package com.example.bake_boss_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetailerDetailsDTO {
    private LocalDate date;
    private String productName;
    private Double productValue;
    private Double payment;
    private Double commission;
    private Double balance;
}