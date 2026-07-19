/**
 * @Author: JavicGray
 * @Version: 1.0
 * @Description: Бизнес-логика управления складскими транзакциями (Приход/Расход) WMS
 */
package com.example.wms.controller;

import com.example.wms.entity.Invoice;
import com.example.wms.entity.Product;
import com.example.wms.entity.Stock;
import com.example.wms.entity.WarehouseZone;
import com.example.wms.entity.invoice.InvoiceItem;
import com.example.wms.repository.InvoiceRepository;
import com.example.wms.repository.ProductRepository;
import com.example.wms.repository.WarehouseZoneRepository;
import com.example.wms.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final ProductRepository productRepository;
    private final WarehouseZoneRepository warehouseZoneRepository;
    private final InvoiceRepository invoiceRepository;

    // 1. Создать товар (POST http://localhost:8080/api/warehouse/products)
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    // 2. Создать зону склада (POST http://localhost:8080/api/warehouse/zones)
    @PostMapping("/zones")
    public ResponseEntity<WarehouseZone> createZone(@RequestBody WarehouseZone zone) {
        return ResponseEntity.ok(warehouseZoneRepository.save(zone));
    }

    // 3. Создать пустую накладную (POST http://localhost:8080/api/warehouse/invoices)
    @PostMapping("/invoices")
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        if (invoice.getItems() != null) {
            for (InvoiceItem item : invoice.getItems()) {
                item.setInvoice(invoice);
            }
        }
        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }

    // Получить все остатки
    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(warehouseService.getAllStocks());
    }

    // Провести накладную
    @PostMapping("/process-invoice/{invoiceId}")
    public ResponseEntity<Invoice> processInvoice(
            @PathVariable Long invoiceId,
            @RequestBody WarehouseZone zone) {
        Invoice processedInvoice = warehouseService.processInvoice(invoiceId, zone);
        return ResponseEntity.ok(processedInvoice);
    }
}
