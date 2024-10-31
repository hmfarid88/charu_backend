package com.example.bake_boss_backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bake_boss_backend.dto.NetSumAmountDto;
import com.example.bake_boss_backend.dto.PaymentDto;
import com.example.bake_boss_backend.dto.PaymentReceiveSummaryDTO;
import com.example.bake_boss_backend.entity.ClosingSetup;
import com.example.bake_boss_backend.entity.OfficePayment;
import com.example.bake_boss_backend.repository.ClosingSetupRepository;
import com.example.bake_boss_backend.repository.ExpenseRepository;
import com.example.bake_boss_backend.repository.OfficePaymentRepository;
import com.example.bake_boss_backend.repository.RetailerCommissionRepository;
import com.example.bake_boss_backend.repository.RetailerPaymentRepository;
import com.example.bake_boss_backend.repository.SupplierPaymentRepository;

@Service
public class OfficePaymentService {
    @Autowired
    private RetailerPaymentRepository retailerPaymentRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private OfficePaymentRepository officePaymentRepository;

    @Autowired
    private SupplierPaymentRepository supplierPaymentRepository;

    @Autowired
    private RetailerCommissionRepository retailerCommissionRepository;

    @Autowired
    private ClosingSetupRepository closingSetupRepository;

    public NetSumAmountDto getNetSumAmountBeforeToday(String username, LocalDate date) {
        Double netSumAmount = retailerPaymentRepository.findNetSumAmountBeforeToday(username, date);
        return new NetSumAmountDto(netSumAmount);
    }

    public List<PaymentDto> getPaymentsForToday(String username, LocalDate date) {
        List<PaymentDto> userExpense = expenseRepository.findExpenseForToday(username, date);
        List<PaymentDto> userPayments = officePaymentRepository.findPaymentsForToday(username, date);
        List<PaymentDto> supplierPayments = supplierPaymentRepository.findSupplierPaymentsForToday(username, date);
        List<PaymentDto> retailerCommission = retailerCommissionRepository.findRetailerCommissionsForToday(username,
                date);
        List<PaymentDto> combinedPayments = new ArrayList<>();
        combinedPayments.addAll(userExpense);
        combinedPayments.addAll(userPayments);
        combinedPayments.addAll(supplierPayments);
        combinedPayments.addAll(retailerCommission);
        return combinedPayments;
    }

    public List<OfficePayment> getPaymentsForCurrentMonth(String username) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return officePaymentRepository.findPaymentsByMonth(year, month, username);
    }

    public List<OfficePayment> getDatewiseOfficePay(String username, LocalDate startDate, LocalDate endDate) {
        return officePaymentRepository.findPaymentsByDate(username, startDate, endDate);
    }

    public List<PaymentReceiveSummaryDTO> getDatewisePaymentAndReceiveSummary() {
        ClosingSetup lastClosingSetup = closingSetupRepository.findLastClosingSetup();

        if (lastClosingSetup != null) {
            LocalDate startDate = lastClosingSetup.getStartDate();
            LocalDate endDate = lastClosingSetup.getEndDate();

            // Get the payment and receive summary for the given date range
            return officePaymentRepository.findDatewisePaymentAndReceiveSummary(startDate, endDate);
        }

        // Return an empty list if no ClosingSetup is found
        return List.of();
    }
}
