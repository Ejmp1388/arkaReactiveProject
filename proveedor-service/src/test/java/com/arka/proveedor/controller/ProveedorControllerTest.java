package com.arka.proveedor.controller;

import com.arka.proveedor.service.ProveedorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProveedorControllerTest {
    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private ProveedorController proveedorController;

    @Test
    void saveProveedor() {
        when(proveedorService.saveProveedor(isNull())).thenReturn(Mono.empty());
        Mono<?> result = proveedorController.saveProveedor(null);
        verify(proveedorService).saveProveedor(null);
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void getProveedorByIdTest() {
        when(proveedorService.getProveedorById("1")).thenReturn(Mono.empty());
        Mono<?> result = proveedorController.getProveedorById("1");
        verify(proveedorService).getProveedorById("1");
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void getProveedoresAllTest() {
        when(proveedorService.getProveedoresAll()).thenReturn(Flux.empty());
        Flux<?> result = proveedorController.getProveedoresAll();
        verify(proveedorService).getProveedoresAll();
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void deleteByIdTest() {
        when(proveedorService.deleteById("1")).thenReturn(Mono.empty());
        Mono<Void> result = proveedorController.deleteById("1");
        verify(proveedorService).deleteById("1");
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void updateByIdTest() {
        when(proveedorService.updateById(eq("1"), isNull())).thenReturn(Mono.empty());
        Mono<?> result = proveedorController.updateById("1", null);
        verify(proveedorService).updateById("1", null);
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void healthCheckTest() {
        var resp = proveedorController.healthCheck();
        assertEquals(200, resp.getStatusCodeValue());
    }
}

