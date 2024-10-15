package com.example.bake_boss_backend.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bake_boss_backend.dto.NetSumAmountDto;
import com.example.bake_boss_backend.dto.PaymentDto;
import com.example.bake_boss_backend.dto.ReceiveDto;
import com.example.bake_boss_backend.entity.EmployeePayment;
import com.example.bake_boss_backend.entity.Expense;
import com.example.bake_boss_backend.entity.OfficeReceive;
import com.example.bake_boss_backend.entity.RetailerCommission;
import com.example.bake_boss_backend.entity.OfficePayment;
import com.example.bake_boss_backend.entity.RetailerPayment;
import com.example.bake_boss_backend.entity.SupplierCommission;
import com.example.bake_boss_backend.entity.SupplierPayment;
import com.example.bake_boss_backend.repository.EmployeePaymentRepository;
import com.example.bake_boss_backend.repository.ExpenseRepository;
import com.example.bake_boss_backend.repository.OfficeReceiveRepository;
import com.example.bake_boss_backend.repository.RetailerCommissionRepository;
import com.example.bake_boss_backend.repository.OfficePaymentRepository;
import com.example.bake_boss_backend.repository.RetailerPaymentRepository;
import com.example.bake_boss_backend.repository.SupplierCommissionRepository;
import com.example.bake_boss_backend.repository.SupplierPaymentRepository;
import com.example.bake_boss_backend.service.ExpenseService;
import com.example.bake_boss_backend.service.OfficePaymentService;
import com.example.bake_boss_backend.service.ReceiveService;
import com.example.bake_boss_backend.service.RetailerPaymentService;
import com.example.bake_boss_backend.service.SupplierPaymentService;

@RestController
@RequestMapping("/paymentApi")
public class TransactionController {

    @Autowired
    private OfficePaymentService officePaymentService;

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private RetailerPaymentService retailerPaymentService;

    private final OfficeReceiveRepository officeReceiveRepository;
    private final OfficePaymentRepository officePaymentRepository;
    private final RetailerPaymentRepository retailerPaymentRepository;
    private final SupplierPaymentRepository supplierPaymentRepository;
    private final ExpenseRepository expenseRepository;
    private final EmployeePaymentRepository employeePaymentRepository;
    private final SupplierCommissionRepository supplierCommissionRepository;
    private final RetailerCommissionRepository retailerCommissionRepository;

    TransactionController(OfficeReceiveRepository officeReceiveRepository,
            OfficePaymentRepository paymentRecordRepository,
            RetailerPaymentRepository retailerPaymentRepository,
            SupplierPaymentRepository supplierPaymentRepository,
            ExpenseRepository expenseRepository,
            EmployeePaymentRepository employeePaymentRepository,
            SupplierCommissionRepository supplierCommissionRepository,
            RetailerCommissionRepository retailerCommissionRepository) {
        this.officeReceiveRepository = officeReceiveRepository;
        this.officePaymentRepository = paymentRecordRepository;
        this.retailerPaymentRepository = retailerPaymentRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
        this.expenseRepository = expenseRepository;
        this.employeePaymentRepository = employeePaymentRepository;
        this.supplierCommissionRepository=supplierCommissionRepository;
        this.retailerCommissionRepository=retailerCommissionRepository;
    }

    @PostMapping("/officeReceive")
    public OfficeReceive newItem(@RequestBody OfficeReceive officeReceive) {
        return officeReceiveRepository.save(officeReceive);
    }

    @PostMapping("/officePayment")
    public OfficePayment newItem(@RequestBody OfficePayment paymentRecord) {
        return officePaymentRepository.save(paymentRecord);
    }

    @PostMapping("/expenseRecord")
    public Expense expenseItem(@RequestBody Expense expenseRecord) {
        return expenseRepository.save(expenseRecord);
    }

    @PostMapping("/retailerPayment")
    public RetailerPayment newItem(@RequestBody RetailerPayment retailerPayment) {
        return retailerPaymentRepository.save(retailerPayment);
    }

    @PostMapping("/supplierCommission")
    public SupplierCommission newItem(@RequestBody SupplierCommission supplierCommission) {
        return supplierCommissionRepository.save(supplierCommission);
    }

    @PostMapping("/retailerCommission")
    public RetailerCommission newItem(@RequestBody RetailerCommission retailerCommission) {
        return retailerCommissionRepository.save(retailerCommission);
    }

    @PostMapping("/supplierPayment")
    public SupplierPayment newItem(@RequestBody SupplierPayment supplierPayment) {
        return supplierPaymentRepository.save(supplierPayment);
    }

    @PostMapping("/employeePayment")
    public EmployeePayment newItem(@RequestBody EmployeePayment employeePayment) {
        return employeePaymentRepository.save(employeePayment);
    }

    @GetMapping("/net-sum-before-today")
    public NetSumAmountDto getNetSumAmountBeforeToday(@RequestParam String username, LocalDate date) {
        return officePaymentService.getNetSumAmountBeforeToday(username, date);
    }

    @GetMapping("/payments/today")
    public List<PaymentDto> getPaymentsForToday(@RequestParam String username, LocalDate date) {
        return officePaymentService.getPaymentsForToday(username, date);
    }

    @GetMapping("/receives/today")
    public List<ReceiveDto> getReceivesForToday(@RequestParam String username, LocalDate date) {
        return receiveService.findReceivesForToday(username, date);
    }

    @GetMapping("/getExpense")
    public List<Expense> getCurrentMonthExpenses(@RequestParam String username) {
        return expenseService.getCurrentMonthExpenses(username);
    }

    @GetMapping("/getDatewiseExpense")
    public List<Expense> getDatewiseExpenses(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return expenseService.getDatewiseExpenses(username, startDate, endDate);
    }

    @GetMapping("/getOfficePay")
    public List<OfficePayment> getPaymentsForCurrentMonth(@RequestParam String username) {
        return officePaymentService.getPaymentsForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseOfficePay")
    public List<OfficePayment> getDatewiseOfficePay(@RequestParam String username, LocalDate startDate,
            LocalDate endDate) {
        return officePaymentService.getDatewiseOfficePay(username, startDate, endDate);
    }

    @GetMapping("/getSupplierPay")
    public List<SupplierPayment> getsupplierForCurrentMonth(@RequestParam String username) {
        return supplierPaymentService.getSupplierForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseSupplierPayment")
    public List<SupplierPayment> getDatewiseSupplierPayment(@RequestParam String username, LocalDate startDate,
            LocalDate endDate) {
        return supplierPaymentService.getDatewiseSupplierPayment(username, startDate, endDate);
    }

    @GetMapping("/getOfficeReceive")
    public List<OfficeReceive> getReceiveForCurrentMonth(@RequestParam String username) {
        return receiveService.getReceivesForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseOfficeReceive")
    public List<OfficeReceive> getDatewiseReceive(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return receiveService.getDatewiseOfficeReceive(username, startDate, endDate);
    }

    @GetMapping("/getRetailerPayment")
    public List<RetailerPayment> getRetailerPayForCurrentMonth(@RequestParam String username) {
        return retailerPaymentService.getRetailerPayForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseRetailerPayment")
    public List<RetailerPayment> getDatewiseRetailerPayForCurrentMonth(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return retailerPaymentService.getDatewiseRetailerPay(username, startDate, endDate);
    }

    @GetMapping("/getEmployeePayment")
    public List<EmployeePayment> getEmployeePayForCurrentMonth(@RequestParam String username) {
        return retailerPaymentService.getEmployeePayForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseEmployeePayment")
    public List<EmployeePayment> getDatewiseEmployeePayment(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return retailerPaymentService.getDatewiseEmployeePay(username, startDate, endDate);
    }

    @GetMapping("/getRetailerCommission")
    public List<RetailerCommission> getRetailerCommissionForCurrentMonth(@RequestParam String username) {
        return retailerPaymentService.getRetailerCommissionForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseRetailerCommission")
    public List<RetailerCommission> getDatewiseRetailerCommission(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return retailerPaymentService.getDatewiseRetailerCommission(username, startDate, endDate);
    }

    @GetMapping("/getSupplierCommission")
    public List<SupplierCommission> getSupplierCommissionForCurrentMonth(@RequestParam String username) {
        return supplierPaymentService.getSupplierCommissionForCurrentMonth(username);
    }

    @GetMapping("/getDatewiseSupplierCommission")
    public List<SupplierCommission> getDatewiseSupplierCommission(@RequestParam String username, LocalDate startDate, LocalDate endDate) {
        return supplierPaymentService.getDatewiseSupplierCommission(username, startDate, endDate);
    }
}
