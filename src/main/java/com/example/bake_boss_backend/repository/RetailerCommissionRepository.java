package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.RetailerDetailsDTO;
import com.example.bake_boss_backend.entity.RetailerCommission;

public interface RetailerCommissionRepository extends JpaRepository<RetailerCommission, Long> {

     @Query("SELECT o FROM RetailerCommission o WHERE YEAR(o.date) = :year AND MONTH(o.date) = :month AND o.username = :username")
  List<RetailerCommission> findRetailerCommissionByMonth(@Param("year") int year, @Param("month") int month, @Param("username") String username);

  @Query("SELECT o FROM RetailerCommission o WHERE o.username = :username AND  o.date BETWEEN :startDate AND :endDate")
  List<RetailerCommission> findRetailerCommissionByDate(String username, LocalDate startDate, LocalDate endDate);

   @Query("SELECT new com.example.bake_boss_backend.dto.RetailerDetailsDTO(rp.date, rp.note, 'No', 0.0, 0.0, 0.0, SUM(rp.amount)) FROM RetailerCommission rp WHERE rp.username = :username AND  rp.retailerName = :retailerName AND rp.date BETWEEN :startDate AND :endDate GROUP BY rp.date, rp.note")
  List<RetailerDetailsDTO> findCommissionDetailsByUsernameAndRetailerName(String username, String retailerName, LocalDate startDate, LocalDate endDate);

  @Query("SELECT new com.example.bake_boss_backend.dto.RetailerDetailsDTO(rp.date, rp.note, 'No', 0.0, 0.0, 0.0, SUM(rp.amount)) FROM RetailerCommission rp JOIN RetailerInfo ri ON rp.retailerName = ri.retailerName WHERE ri.salesPerson = :salesPerson AND rp.retailerName = :retailerName AND rp.date BETWEEN :startDate AND :endDate GROUP BY rp.date, rp.note")
  List<RetailerDetailsDTO> findCommissionDetailsBySalesPersonAndRetailerName(String salesPerson, String retailerName, LocalDate startDate, LocalDate endDate);
}
