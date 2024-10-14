package com.example.bake_boss_backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.DetailsSupplierDTO;
import com.example.bake_boss_backend.entity.ProductStock;

public interface ProductStockrepository extends JpaRepository<ProductStock, Long> {
       
        Optional<ProductStock> findTopByProductNameAndUsernameOrderByProductIdDesc(String productName, String username);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.username=:username AND ps.productId IN " +
                        "(SELECT MAX(ps2.productId) FROM ProductStock ps2 GROUP BY ps2.productName)")
        List<ProductStock> findLatestProductStockForEachProductName(@Param("username") String username);

        Optional<ProductStock> findByProductId(Long productId);

        List<ProductStock> findByUsernameAndInvoiceNo(String username, String invoiceNo);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.status='sold' AND YEAR(ps.date) = :year AND MONTH(ps.date) = :month AND ps.username=:username")
        List<ProductStock> findProductByStatus(@Param("year") int year, @Param("month") int month, @Param("username") String username);

        @Query("SELECT ps FROM ProductStock ps WHERE ps.status='sold' AND  ps.username=:username AND ps.date BETWEEN :startDate AND :endDate")
        List<ProductStock> findDatewiseSoldProductByUsername(String username, LocalDate startDate, LocalDate endDate);
       
        @Query("SELECT ps FROM ProductStock ps WHERE ps.username=:username AND ps.status='damaged'")
        List<ProductStock> findDamagedProductByStatus(String username);

        @Query("SELECT ps FROM ProductStock ps WHERE  YEAR(ps.date) = :year AND MONTH(ps.date) = :month AND ps.username=:username")
        List<ProductStock> findProductByUsername(@Param("year") int year, @Param("month") int month, @Param("username") String username);

        @Query("SELECT ps FROM ProductStock ps WHERE  ps.username=:username AND ps.date BETWEEN :startDate AND :endDate")
        List<ProductStock> findDatewiseProductByUsername(String username, LocalDate startDate, LocalDate endDate);

        List<ProductStock> findByUsernameAndProductName(String username, String oldItemName);

        @Query("SELECT ps.supplier, SUM(ps.purchasePrice * ps.productQty) " +
        "FROM ProductStock ps WHERE ps.username = :username AND ps.status='stored' GROUP BY ps.supplier")
        List<Object[]> findTotalProductCostGroupedBySupplierAndUsername(String username);

        @Query("SELECT new com.example.bake_boss_backend.dto.DetailsSupplierDTO(m.date,  m.productName, SUM(m.productQty), SUM(m.purchasePrice * m.productQty)) "
        +
        "FROM ProductStock m " +
        "WHERE m.username = :username AND m.supplier = :supplier AND m.date BETWEEN :startDate AND :endDate"
        +
        " GROUP BY m.date,  m.productName")
        List<DetailsSupplierDTO> findProductsValueBySupplierAndUsername(String username, String supplier, LocalDate startDate, LocalDate endDate);

        
}
