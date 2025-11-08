package com.arka.proveedor.controller;

import com.arka.proveedor.service.CompraService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompraControllerTest {

    @Mock
    private CompraService compraService;

    @InjectMocks
    private CompraController compraController;

    @Test
    void registrarCompraTest() {
        when(compraService.registrarCompra(isNull())).thenReturn(Mono.empty());
        Mono<?> result = compraController.registrarCompra(null);
        verify(compraService).registrarCompra(null);
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void actualizarDetalleTest() {
        when(compraService.actualizarDetalle(eq("1"), isNull())).thenReturn(Mono.empty());
        Mono<?> result = compraController.actualizarDetalle("1", null);
        verify(compraService).actualizarDetalle("1", null);
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void agregarDetalleTest() {
        when(compraService.agregarDetalle(isNull())).thenReturn(Mono.empty());
        Mono<?> result = compraController.agregarDetalle(null);
        verify(compraService).agregarDetalle(null);
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void eliminarDetalleTest() {
        when(compraService.eliminarDetalle("1")).thenReturn(Mono.empty());
        Mono<?> result = compraController.eliminarDetalle("1");
        verify(compraService).eliminarDetalle("1");
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void healthCheckTest() {
        var resp = compraController.healthCheck();
        org.junit.jupiter.api.Assertions.assertEquals(200, resp.getStatusCodeValue());
    }
}
