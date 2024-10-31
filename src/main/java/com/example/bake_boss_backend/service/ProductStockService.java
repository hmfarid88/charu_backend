package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.ClosingSetup;
import com.example.bake_boss_backend.entity.OrderInfo;
import com.example.bake_boss_backend.entity.ProductStock;
import com.example.bake_boss_backend.repository.ClosingSetupRepository;
import com.example.bake_boss_backend.repository.OrderInfoRepository;
import com.example.bake_boss_backend.repository.ProductStockrepository;

@Service
public class ProductStockService {
    @Autowired
    private ProductStockrepository productStockRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private ClosingSetupRepository closingSetupRepository;

    public List<ProductStock> getProductDistForCurrentMonth(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return productStockRepository.findProductByStatus(year, month, username);
    }

    public List<ProductStock> getDatewiseSoldProductStock(String username, LocalDate startDate, LocalDate endDate) {
        return productStockRepository.findDatewiseSoldProductByUsername(username, startDate, endDate);
    }

    public List<ProductStock> getAllProductStock(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return productStockRepository.findProductByUsername(year, month, username);
    }

    public List<ProductStock> getDatewiseProductStock(String username, LocalDate startDate, LocalDate endDate) {
        return productStockRepository.findDatewiseProductByUsername(username, startDate, endDate);
    }

    public List<Object[]> getExistingOrder() {
        return orderInfoRepository.findAllList();
    }

    public List<OrderInfo> getExistingSingleOrder(Long orderId) {
        return orderInfoRepository.findByOrderId(orderId);
    }

    public ClosingSetup saveOrUpdateClosingSetup(LocalDate startDate, LocalDate endDate) {
        Optional<ClosingSetup> existingSetup = closingSetupRepository.findByStartDateAndEndDate(startDate, endDate);

        ClosingSetup closingSetup;
        if (existingSetup.isPresent()) {
            // Update the existing record
            closingSetup = existingSetup.get();
            closingSetup.setStartDate(startDate);
            closingSetup.setEndDate(endDate);
        } else {
            // Create a new record
            closingSetup = new ClosingSetup(0, startDate, endDate);
        }

        // Save the updated or new record
        return closingSetupRepository.save(closingSetup);
    }

    public Double getTotalSoldProductQtyToday() {
        return productStockRepository.findTotalSoldProductQtyToday();
    }
}
