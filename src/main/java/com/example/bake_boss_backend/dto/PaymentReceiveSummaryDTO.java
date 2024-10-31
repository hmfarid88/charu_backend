package com.example.bake_boss_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReceiveSummaryDTO {
    private LocalDate date;
    private String name;
    private Double totalPaymentAmount;
    private Double totalReceiveAmount;
    private Double balance;

}
