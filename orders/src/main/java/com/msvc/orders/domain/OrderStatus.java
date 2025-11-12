package com.msvc.orders.domain;


public final class OrderStatus {
    private OrderStatus() {}
    public static final String NEW = "NEW";
    public static final String PENDING = "PENDING";
    public static final String APPROVED = "APPROVED";
    public static final String CANCELLED = "CANCELLED";
}

