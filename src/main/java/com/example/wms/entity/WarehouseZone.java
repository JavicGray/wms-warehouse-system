package com.example.wms.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "warehouse_zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zone_name", nullable = false, length = 100)
    private String zoneName;
}
