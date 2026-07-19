/**
 * @Author: JavicGray
 * @Version: 1.0
 * @Description: Бизнес-логика управления складскими транзакциями (Приход/Расход) WMS
 */
package com.example.wms.service;

import com.example.wms.entity.Invoice;
import com.example.wms.entity.Stock;
import com.example.wms.entity.WarehouseZone;
import com.example.wms.entity.invoice.InvoiceItem;
import com.example.wms.entity.invoice.InvoiceStatus;
import com.example.wms.entity.invoice.InvoiceType;
import com.example.wms.exception.NotEnoughStockException;
import com.example.wms.repository.InvoiceRepository;
import com.example.wms.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final StockRepository stockRepository;
    private final InvoiceRepository invoiceRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAllWithProductsAndZones();
    }

    @Transactional
    public Invoice processInvoice(Long invoiceId, WarehouseZone zone) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Накладная не найдена"));

        if (invoice.getStatus() == InvoiceStatus.COMPLETED) {
            throw new IllegalStateException("Накладная уже проведена");
        }
        for (InvoiceItem item : invoice.getItems()) {
            if (invoice.getType() == InvoiceType.INCOMING) {
                Stock stock = stockRepository.findByProductIdAndWarehouseZoneId(item.getProduct().getId(), zone.getId())
                        .orElse(Stock.builder()
                                .product(item.getProduct())
                                .warehouseZone(zone)
                                .quantity(0)
                                .build());
                stock.setQuantity(stock.getQuantity() + item.getQuantity());
                stockRepository.save(stock);
            } else if (invoice.getType() == InvoiceType.OUTGOING) {
                Stock stock = stockRepository.findByProductIdAndWarehouseZoneId(item.getProduct().getId(), zone.getId())
                        .orElseThrow(() -> new NotEnoughStockException(
                                "Товар с ID " + item.getProduct().getId() +
                                        " отсутствует в зоне " + zone.getZoneName()));

                if (stock.getQuantity() < item.getQuantity()) {
                    throw new NotEnoughStockException("Недостаточно товара " + stock.getProduct().getName() +
                            " в зоне " + zone.getZoneName() + ". Требуется: " + item.getQuantity() +
                            ", в наличии: " + stock.getQuantity());
                }
                stock.setQuantity(stock.getQuantity() - item.getQuantity());
                stockRepository.save(stock);
            }
        }
        invoice.setStatus(InvoiceStatus.COMPLETED);
        return invoiceRepository.save(invoice);
    }
}
