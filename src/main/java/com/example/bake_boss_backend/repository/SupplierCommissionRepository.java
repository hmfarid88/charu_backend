package com.example.bake_boss_backend.repository;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bake_boss_backend.dto.ReceiveDto;
import com.example.bake_boss_backend.entity.SupplierCommission;

public interface SupplierCommissionRepository extends JpaRepository<SupplierCommission, Long> {
    @Query("SELECT new com.example.bake_boss_backend.dto.ReceiveDto(r.date, r.supplierName, r.note, r.amount) " +
            "FROM SupplierCommission r WHERE r.username=:username AND r.date = :date")
    List<ReceiveDto> findCommissionReceivesForToday(@Param("username") String username, @Param("date") LocalDate date);
}
