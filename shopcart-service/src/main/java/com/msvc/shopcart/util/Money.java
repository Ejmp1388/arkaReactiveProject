package com.msvc.shopcart.util;

public class Money {
    private long cents;
    private String currency;

    public static Money ofCents(long cents, String currency) {
        return new Money(cents, currency);
    }

    public Money() { }
    public Money(long cents, String currency) {
        this.cents = cents;
        this.currency = currency;
    }

    public long getCents() { return cents; }
    public void setCents(long cents) { this.cents = cents; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}

