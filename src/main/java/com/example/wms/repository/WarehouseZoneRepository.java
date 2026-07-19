package com.example.wms.repository;

import com.example.wms.entity.WarehouseZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseZoneRepository extends JpaRepository<WarehouseZone, Long> {

    Optional<WarehouseZone> findByZoneName(String zoneName);
}
