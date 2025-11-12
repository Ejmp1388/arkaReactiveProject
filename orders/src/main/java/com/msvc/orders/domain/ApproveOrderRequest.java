package com.msvc.orders.domain;

import jakarta.validation.constraints.Email;

public record ApproveOrderRequest(
        @Email String to,      // si no se envía, usa order.getCustomerEmail()
        String message         // opcional; si no se envía, se arma uno por defecto
) {}
