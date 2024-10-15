package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.PaymentDto;
import com.example.bake_boss_backend.entity.RetailerCommission;

public interface RetailerCommissionRepository extends JpaRepository<RetailerCommission, Long> {
    @Query("SELECT new com.example.bake_boss_backend.dto.PaymentDto(s.date, s.retailerName, s.note, s.amount) "
            + "FROM RetailerCommission s WHERE s.username = :username AND s.date = :date")
    List<PaymentDto> findRetailerCommissionsForToday(@Param("username") String username, @Param("date") LocalDate date);

     @Query("SELECT o FROM RetailerCommission o WHERE YEAR(o.date) = :year AND MONTH(o.date) = :month AND o.username = :username")
  List<RetailerCommission> findRetailerCommissionByMonth(@Param("year") int year, @Param("month") int month, @Param("username") String username);

  @Query("SELECT o FROM RetailerCommission o WHERE o.username = :username AND  o.date BETWEEN :startDate AND :endDate")
  List<RetailerCommission> findRetailerCommissionByDate(String username, LocalDate startDate, LocalDate endDate);
}
