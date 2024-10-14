package com.example.bake_boss_backend.repository;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.entity.EmployeePayment;

public interface EmployeePaymentRepository extends JpaRepository<EmployeePayment, Long>{

     @Query("SELECT e FROM EmployeePayment e WHERE MONTH(e.date) = :month AND YEAR(e.date) = :year AND e.username = :username")
    List<EmployeePayment> findByMonthYearAndUsername(@Param("month") int month, @Param("year") int year, @Param("username") String username);

    @Query("SELECT e FROM EmployeePayment e WHERE e.username = :username AND  e.date BETWEEN :startDate AND :endDate")
    List<EmployeePayment> findDatewiseEmployeePayment(String username, LocalDate startDate, LocalDate endDate);
}
