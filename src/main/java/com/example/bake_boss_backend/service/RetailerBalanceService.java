package com.example.bake_boss_backend.service;

import java.util.List;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.RetailerBalanceDTO;
import com.example.bake_boss_backend.dto.RetailerDetailsDTO;
import com.example.bake_boss_backend.repository.RetailerPaymentRepository;

@Service
public class RetailerBalanceService {
    @Autowired
    private RetailerPaymentRepository retailerPaymentRepository;

    public List<RetailerBalanceDTO> retailerBalance(String username) {
        return retailerPaymentRepository.findRetailerBalanceByUsername(username);
    }

    public List<RetailerBalanceDTO> salesRetailerBalance(String salesPerson) {
        return retailerPaymentRepository.findRetailerBalanceBySalesPerson(salesPerson);
    }

    public List<RetailerDetailsDTO> getDatewiseDetailsByRetailerAndUsername(
            String retailerName, String username, LocalDate startDate, LocalDate endDate) {
        return retailerPaymentRepository.findDatewiseDetailsByRetailerAndUsername(
                retailerName, username, startDate, endDate);
    }

    public List<RetailerDetailsDTO> getDatewiseDetailsByRetailerAndSalesPerson(
            String retailerName, String salesPerson, LocalDate startDate, LocalDate endDate) {
        return retailerPaymentRepository.findDatewiseDetailsByRetailerAndSalesPerson(
                retailerName, salesPerson, startDate, endDate);
    }
}
