package com.example.bake_boss_backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_supplier", columnList = "supplier"),
        @Index(name = "idx_product_name", columnList = "productName"),
        @Index(name = "idx_username", columnList = "username")
})
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private Long orderId;
    private LocalDate date;
    private String supplier;
    private String productName;
    private Double purchasePrice;
    private Double costPrice;
    private Double dpRate;
    private Double productQty;
    private Double remainingQty;
    private String status;
    private String customer;
    private String username;
    private String invoiceNo;
    private String truckNo;
    private String note;

}
