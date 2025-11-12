package com.msvc.invoice.mapper;



import com.msvc.invoice.domain.Invoice;
import com.msvc.invoice.domain.InvoiceItem;
import com.msvc.invoice.dto.InvoiceItemResponse;
import com.msvc.invoice.dto.InvoiceResponse;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceMapper {

    public static InvoiceItemResponse toItemResponse(InvoiceItem it) {
        InvoiceItemResponse dto = new InvoiceItemResponse();
        dto.setId(it.getId());
        dto.setProductId(it.getProductId());
        dto.setProductName(it.getProductName());
        dto.setUnitPrice(it.getUnitPrice());
        dto.setQuantity(it.getQuantity());
        dto.setLineTotal(it.getLineTotal());
        return dto;
    }

    public static InvoiceResponse toInvoiceResponse(Invoice inv, List<InvoiceItem> items) {
        InvoiceResponse dto = new InvoiceResponse();
        dto.setId(inv.getId());
        dto.setOrderId(inv.getOrderId());
        dto.setCustomerId(inv.getCustomerId());
        dto.setInvoiceNumber(inv.getInvoiceNumber());
        dto.setStatus(inv.getStatus());
        dto.setCurrency(inv.getCurrency());
        dto.setSubtotal(inv.getSubtotal());
        dto.setTaxes(inv.getTaxes());
        dto.setTotal(inv.getTotal());
        dto.setCreatedAt(inv.getCreatedAt());
        dto.setUpdatedAt(inv.getUpdatedAt());
        dto.setItems(items.stream().map(InvoiceMapper::toItemResponse).collect(Collectors.toList()));
        return dto;
    }
}
