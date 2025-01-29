package com.example.bake_boss_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.SupplierBalanceDTO;
import com.example.bake_boss_backend.dto.SupplierDetailsDTO;
import com.example.bake_boss_backend.service.SupplierBalanceService;

@RestController
@RequestMapping("/supplierBalance")
public class SupplierBalanceController {
     @Autowired
    private SupplierBalanceService supplierBalanceService;

    @GetMapping("/supplier/balance")
    public List<SupplierBalanceDTO> getSuppliersBalanceByUsername(@RequestParam String username) {
        return supplierBalanceService.calculateSuppliersBalanceByUsername(username);
    }

    @GetMapping("/supplier-details")
    public List<SupplierDetailsDTO> getDatewiseDetailsBySupplierAndUsername(@RequestParam String username, @RequestParam String supplierName){
        return supplierBalanceService.getDetailsBySupplierAndUsername(username, supplierName);
    }
}
