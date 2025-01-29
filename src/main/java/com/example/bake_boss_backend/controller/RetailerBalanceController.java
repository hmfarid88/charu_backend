package com.example.bake_boss_backend.controller;

import java.util.List;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.RetailerBalanceDTO;
import com.example.bake_boss_backend.dto.RetailerDetailsDTO;
import com.example.bake_boss_backend.service.RetailerBalanceService;

@RestController
@RequestMapping("/retailer")
public class RetailerBalanceController {

    @Autowired
    private RetailerBalanceService retailerBalanceService;

    @GetMapping("/retailerBalance")
    public List<RetailerBalanceDTO> retailerBalance() {
        return retailerBalanceService.retailerBalance();
    }

    @GetMapping("/datewiseRetailerBalance")
    public List<RetailerBalanceDTO> datewiseRetailerBalance(LocalDate startDate, LocalDate endDate) {
        return retailerBalanceService.datewiseRetailerBalance(startDate, endDate);
    }
  
    @GetMapping("/salesRetailerBalance")
    public List<RetailerBalanceDTO> salesRetailerBalance(@RequestParam String salesPerson) {
        return retailerBalanceService.salesRetailerBalance(salesPerson);
    }

    @GetMapping("/salesDatewiseRetailerBalance")
    public List<RetailerBalanceDTO> salesDatewiseRetailerBalance(String salesPerson, LocalDate startDate, LocalDate endDate) {
        return retailerBalanceService.salesDatewiseRetailerBalance(salesPerson, startDate, endDate);
    }

    @GetMapping("/retailer-details")
    public List<RetailerDetailsDTO> getDetailsByRetailerAndUsername(
            @RequestParam String retailerName,
            @RequestParam String username){
        return retailerBalanceService.getDatewiseDetailsByRetailerAndUsername(retailerName, username);
    }

    @GetMapping("/datewise-retailer-details")
    public List<RetailerDetailsDTO> getDatewiseDetailsByRetailerAndUsername(LocalDate startDate, LocalDate endDate,
            @RequestParam String retailerName,
            @RequestParam String username){
        return retailerBalanceService.getDatewiseRetailerDetailsByRetailerAndUsername(retailerName, username, startDate, endDate);
    }

    @GetMapping("/sales-retailer-details")
    public List<RetailerDetailsDTO> getDetailsByRetailerAndSalesPerson(
            @RequestParam String retailerName, @RequestParam String salesPerson) {
        return retailerBalanceService.getDetailsByRetailerAndSalesPerson(retailerName, salesPerson);
    }

    @GetMapping("/sales-datewise-retailer-details")
    public List<RetailerDetailsDTO> getDatewiseDetailsByRetailerAndSalesPerson(LocalDate startDate, LocalDate endDate,
            @RequestParam String retailerName,
            @RequestParam String salesPerson){
        return retailerBalanceService.getSalesDatewiseDetailsByRetailerAndSalesPerson(salesPerson, retailerName, startDate, endDate);
    }
}
