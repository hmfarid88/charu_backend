package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.entity.RetailerCommission;
import com.example.bake_boss_backend.entity.SupplierCommission;
import com.example.bake_boss_backend.entity.SupplierPayment;
import com.example.bake_boss_backend.repository.SupplierCommissionRepository;
import com.example.bake_boss_backend.repository.SupplierPaymentRepository;

@Service
public class SupplierPaymentService {
    @Autowired
    private SupplierPaymentRepository supplierPaymentRepository;

    @Autowired
    private SupplierCommissionRepository supplierCommissionRepository;

    public List<SupplierPayment> getSupplierForCurrentMonth(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return supplierPaymentRepository.findPaymentsByMonth(year, month, username);
    }

    public List<SupplierPayment> getDatewiseSupplierPayment(String username, LocalDate startDate, LocalDate endDate) {
        return supplierPaymentRepository.findPaymentsByDate(username, startDate, endDate);
    }

    public List<SupplierCommission> getSupplierCommissionForCurrentMonth(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return supplierCommissionRepository.findSupplierCommissionByMonth(year, month, username);
    }

    public List<SupplierCommission> getDatewiseSupplierCommission(String username, LocalDate startDate, LocalDate endDate) {
        return supplierCommissionRepository.findSupplierCommissionByDate(username, startDate, endDate);
    }
}
