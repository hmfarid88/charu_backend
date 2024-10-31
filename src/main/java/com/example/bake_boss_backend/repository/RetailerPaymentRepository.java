package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.ReceiveDto;
import com.example.bake_boss_backend.dto.RetailerBalanceDTO;
import com.example.bake_boss_backend.dto.RetailerDetailsDTO;
import com.example.bake_boss_backend.entity.RetailerPayment;

public interface RetailerPaymentRepository extends JpaRepository<RetailerPayment, Long> {
        @Query(value = "SELECT ( " +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM retailer_payment WHERE username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_commission WHERE username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM office_receive WHERE username = :username AND DATE(date) < :date) "
                        +
                        ") - ( " +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM expense WHERE username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM office_payment WHERE username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM retailer_commission WHERE username = :username AND DATE(date) < :date) + "
                        +
                        "  (SELECT COALESCE(SUM(amount), 0) FROM supplier_payment WHERE username = :username AND DATE(date) < :date) "
                        +
                        ") AS total_amount", nativeQuery = true)
        Double findNetSumAmountBeforeToday(@Param("username") String username, @Param("date") LocalDate date);

        @Query("SELECT new com.example.bake_boss_backend.dto.ReceiveDto(rp.date, rp.retailerName, rp.note, rp.amount) "
                        +
                        "FROM RetailerPayment rp " +
                        "WHERE rp.username = :username AND rp.date = :date")
        List<ReceiveDto> findRetailerPaymentsForToday(@Param("username") String username,
                        @Param("date") LocalDate date);

        @Query("SELECT o FROM RetailerPayment o WHERE YEAR(o.date) = :year AND MONTH(o.date) = :month AND o.username = :username")
        List<RetailerPayment> findRetailerPayByMonth(@Param("year") int year, @Param("month") int month,
                        @Param("username") String username);

        @Query("SELECT ps FROM RetailerPayment ps WHERE  ps.username=:username AND ps.date BETWEEN :startDate AND :endDate")
        List<RetailerPayment> findDatewiseRetailerPaymentByUsername(String username, LocalDate startDate,
                        LocalDate endDate);

                        @Query("SELECT new com.example.bake_boss_backend.dto.RetailerBalanceDTO(" +
                        "r.retailerName, r.retailerCode, r.salesPerson, " +
                        "COALESCE(SUM(ps.productQty), 0) as totalProductQty, " +
                        "COALESCE(SUM(ps.productQty * ps.dpRate), 0) as totalProductValue, " +
                        "COALESCE(SUM(rp.amount), 0) as totalPayment, " +
                        "COALESCE(SUM(rc.amount), 0) as totalCommission) " +
                        "FROM RetailerInfo r " +
                        "LEFT JOIN ProductStock ps ON ps.customer = r.retailerName " +
                        "AND ps.date BETWEEN :startDate AND :endDate " +
                        "LEFT JOIN RetailerPayment rp ON rp.retailerName = r.retailerName " +
                        "AND rp.date BETWEEN :startDate AND :endDate " +
                        "LEFT JOIN RetailerCommission rc ON rc.retailerName = r.retailerName " +
                        "AND rc.date BETWEEN :startDate AND :endDate " +
                        "WHERE r.status = 'active' " +
                        "GROUP BY r.retailerName, r.retailerCode, r.salesPerson")
                 List<RetailerBalanceDTO> findRetailerBalanceBetweenDates(
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
                 
                        @Query("SELECT new com.example.bake_boss_backend.dto.RetailerBalanceDTO(" +
                        "r.retailerName, r.retailerCode, r.salesPerson, " +
                        "COALESCE(SUM(ps.productQty), 0) as totalProductQty, " +
                        "COALESCE(SUM(ps.productQty * ps.dpRate), 0) as totalProductValue, " +
                        "COALESCE(SUM(rp.amount), 0) as totalPayment, " +
                        "COALESCE(SUM(rc.amount), 0) as totalCommission) " +
                        "FROM RetailerInfo r " +
                        "LEFT JOIN ProductStock ps ON ps.customer = r.retailerName " +
                        "AND ps.date BETWEEN :startDate AND :endDate " +
                        "LEFT JOIN RetailerPayment rp ON rp.retailerName = r.retailerName " +
                        "AND rp.date BETWEEN :startDate AND :endDate " +
                        "LEFT JOIN RetailerCommission rc ON rc.retailerName = r.retailerName " +
                        "AND rc.date BETWEEN :startDate AND :endDate " +
                        "WHERE r.status = 'active' AND r.salesPerson = :salesPerson " +
                        "GROUP BY r.retailerName, r.retailerCode, r.salesPerson")
                 List<RetailerBalanceDTO> findSalesRetailerBalanceBetweenDates(
                        @Param("salesPerson") String salesPerson,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
                 

        
        // @Query("SELECT new com.example.bake_boss_backend.dto.RetailerBalanceDTO(ps.customer, " +
        //                 "SUM(ps.dpRate * ps.productQty) - (COALESCE(SUM(rp.amount), 0) + COALESCE(SUM(rc.amount), 0))) "
        //                 +
        //                 "FROM ProductStock ps " +
        //                 "LEFT JOIN RetailerPayment rp ON ps.customer = rp.retailerName " +
        //                 "LEFT JOIN RetailerCommission rc ON ps.customer = rc.retailerName " +
        //                 "JOIN RetailerInfo ri ON ps.customer = ri.retailerName " +
        //                 "WHERE ri.salesPerson = :salesPerson AND ps.status = 'sold' " +
        //                 "GROUP BY ps.customer")
        // List<RetailerBalanceDTO> findRetailerBalanceBySalesPerson(@Param("salesPerson") String salesPerson);

        @Query("SELECT new com.example.bake_boss_backend.dto.RetailerDetailsDTO( " +
                        "COALESCE(ps.date, rp.date, rc.date), ps.productName, " +
                        "COALESCE(SUM(ps.dpRate * ps.productQty), 0), " +
                        "COALESCE(SUM(rp.amount), 0), " +
                        "COALESCE(SUM(rc.amount), 0), " +
                        "COALESCE(SUM(ps.dpRate * ps.productQty), 0) - " +
                        "(COALESCE(SUM(rp.amount), 0) + COALESCE(SUM(rc.amount), 0)) ) " +
                        "FROM ProductStock ps " +
                        "LEFT JOIN RetailerPayment rp ON ps.customer = rp.retailerName AND ps.username = rp.username AND rp.date BETWEEN :startDate AND :endDate "
                        +
                        "LEFT JOIN RetailerCommission rc ON ps.customer = rc.retailerName AND ps.username = rc.username AND rc.date BETWEEN :startDate AND :endDate "
                        +
                        "WHERE ps.status = 'sold' AND ps.customer = :retailerName AND ps.username = :username AND (ps.date BETWEEN :startDate AND :endDate OR rp.date BETWEEN :startDate AND :endDate OR rc.date BETWEEN :startDate AND :endDate) "
                        +
                        "GROUP BY COALESCE(ps.date, rp.date, rc.date), ps.productName")
        List<RetailerDetailsDTO> findDatewiseDetailsByRetailerAndUsername(@Param("retailerName") String retailerName,
                        @Param("username") String username,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT new com.example.bake_boss_backend.dto.RetailerDetailsDTO( " +
                        "COALESCE(ps.date, rp.date, rc.date), ps.productName, " +
                        "COALESCE(SUM(ps.dpRate * ps.productQty), 0), " +
                        "COALESCE(SUM(rp.amount), 0), " +
                        "COALESCE(SUM(rc.amount), 0), " +
                        "COALESCE(SUM(ps.dpRate * ps.productQty), 0) - " +
                        "(COALESCE(SUM(rp.amount), 0) + COALESCE(SUM(rc.amount), 0)) ) " +
                        "FROM ProductStock ps " +
                        "LEFT JOIN RetailerPayment rp ON ps.customer = rp.retailerName AND rp.date BETWEEN :startDate AND :endDate "
                        +
                        "LEFT JOIN RetailerCommission rc ON ps.customer = rc.retailerName AND rc.date BETWEEN :startDate AND :endDate "
                        +
                        "JOIN RetailerInfo ri ON ps.customer = ri.retailerName " +
                        "WHERE ri.salesPerson = :salesPerson AND ps.status = 'sold' " +
                        "AND ps.customer = :retailerName AND (ps.date BETWEEN :startDate AND :endDate " +
                        "OR rp.date BETWEEN :startDate AND :endDate OR rc.date BETWEEN :startDate AND :endDate) " +
                        "GROUP BY ps.date, ps.productName")
        List<RetailerDetailsDTO> findDatewiseDetailsByRetailerAndSalesPerson(
                        @Param("retailerName") String retailerName,
                        @Param("salesPerson") String salesPerson,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(rp.amount) FROM RetailerPayment rp " +
                        "WHERE rp.retailerName IN " +
                        "(SELECT ri.retailerName FROM RetailerInfo ri WHERE ri.salesPerson = :employeeName) " +
                        "AND EXTRACT(YEAR FROM rp.date) = :year " +
                        "AND EXTRACT(MONTH FROM rp.date) = :month")
        Double getPaymentValueForEmployee(@Param("employeeName") String employeeName, @Param("year") int year,
                        @Param("month") int month);

        @Query("SELECT SUM(rp.amount) FROM RetailerPayment rp WHERE rp.retailerName IN :retailerNames AND YEAR(rp.date) = :year AND MONTH(rp.date) = :month")
        Double getPaymentValueForRetailers(@Param("retailerNames") List<String> retailerNames, @Param("year") int year,
                        @Param("month") int month);

}
