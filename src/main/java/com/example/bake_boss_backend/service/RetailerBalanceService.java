package com.example.bake_boss_backend.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.RetailerBalanceDTO;
import com.example.bake_boss_backend.dto.RetailerDetailsDTO;
import com.example.bake_boss_backend.entity.ClosingSetup;
import com.example.bake_boss_backend.entity.RetailerInfo;
import com.example.bake_boss_backend.repository.ClosingSetupRepository;
import com.example.bake_boss_backend.repository.RetailerInfoRepository;
import com.example.bake_boss_backend.repository.RetailerPaymentRepository;

@Service
public class RetailerBalanceService {
    @Autowired
    private RetailerPaymentRepository retailerPaymentRepository;

    @Autowired
    private ClosingSetupRepository closingSetupRepository;

    @Autowired
    private RetailerInfoRepository retailerInfoRepository;


    public List<RetailerBalanceDTO> retailerBalance() {
        ClosingSetup lastClosingSetup = closingSetupRepository.findLastClosingSetup();

        if (lastClosingSetup != null) {
            LocalDate startDate = lastClosingSetup.getStartDate();
            LocalDate endDate = lastClosingSetup.getEndDate();

            return retailerPaymentRepository.findRetailerBalanceBetweenDates(startDate, endDate);
        }
        // Return an empty list if no ClosingSetup is found
        return List.of();

    }

    public List<RetailerBalanceDTO> datewiseRetailerBalance(LocalDate startDate, LocalDate endDate) {
       return retailerPaymentRepository.findRetailerBalanceBetweenDates(startDate, endDate);

    }

    public List<RetailerBalanceDTO> salesRetailerBalance(String salesPerson) {
        ClosingSetup lastClosingSetup = closingSetupRepository.findLastClosingSetup();

        if (lastClosingSetup != null) {
            LocalDate startDate = lastClosingSetup.getStartDate();
            LocalDate endDate = lastClosingSetup.getEndDate();

            return retailerPaymentRepository.findSalesRetailerBalanceBetweenDates(salesPerson, startDate, endDate);
        }
        // Return an empty list if no ClosingSetup is found
        return List.of();

    }

    public List<RetailerBalanceDTO> salesDatewiseRetailerBalance(String salesPerson, LocalDate startDate, LocalDate endDate) {
        return retailerPaymentRepository.findSalesRetailerBalanceBetweenDates(salesPerson, startDate, endDate);
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

     public RetailerInfo updateRetailerInfo(Long id, RetailerInfo updatedRetailerInfo) {
        Optional<RetailerInfo> existingRetailerOpt = retailerInfoRepository.findById(id);
        if (existingRetailerOpt.isPresent()) {
            RetailerInfo existingRetailer = existingRetailerOpt.get();
            existingRetailer.setRetailerName(updatedRetailerInfo.getRetailerName());
            existingRetailer.setRetailerCode(updatedRetailerInfo.getRetailerCode());
            existingRetailer.setThanaName(updatedRetailerInfo.getThanaName());
            existingRetailer.setZillaName(updatedRetailerInfo.getZillaName());
            existingRetailer.setAreaName(updatedRetailerInfo.getAreaName());
            existingRetailer.setMobileNumber(updatedRetailerInfo.getMobileNumber());
            existingRetailer.setSalesPerson(updatedRetailerInfo.getSalesPerson());
            existingRetailer.setStatus(updatedRetailerInfo.getStatus());

            return retailerInfoRepository.save(existingRetailer);
        } else {
            throw new RuntimeException("Retailer not found with ID: " + id);
        }
    }
}
