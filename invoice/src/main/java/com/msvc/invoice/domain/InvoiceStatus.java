package com.msvc.invoice.domain;

public final class InvoiceStatus {
    private InvoiceStatus() {}
    public static final String CREATED = "CREATED";
    public static final String SENT = "SENT";       // enviada al cliente
    public static final String PAID = "PAID";       // si manejas pagos aquí
    public static final String CANCELLED = "CANCELLED";
}
