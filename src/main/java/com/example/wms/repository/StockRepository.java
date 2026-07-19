package com.example.wms.repository;

import com.example.wms.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s JOIN FETCH s.product JOIN FETCH s.warehouseZone")
    List<Stock> findAllWithProductsAndZones();

    Optional<Stock> findByProductIdAndWarehouseZoneId(Long productId, Long zoneId);
}
