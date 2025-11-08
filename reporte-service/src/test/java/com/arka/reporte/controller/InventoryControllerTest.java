package com.arka.reporte.controller;

import com.arka.reporte.dto.LowStockInventoryDTO;
import com.arka.reporte.service.InventoryService;
import com.arka.reporte.util.CsvExporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @Mock
    private CsvExporter csvExporter;

    @InjectMocks
    private InventoryController controller;

    @Test
    void getLowStockItems_devuelveListaVacia_cuandoNoHayItems() {
        when(inventoryService.getLowStockItems()).thenReturn(Flux.empty());

        List<LowStockInventoryDTO> result = controller.getLowStockItems()
                .collectList()
                .block();

        assertEquals(0, result != null ? result.size() : 0);
    }

    @Test
    void getLowStockItems_devuelveItems_cuandoHayDatos() {
        LowStockInventoryDTO dto = mock(LowStockInventoryDTO.class);
        when(inventoryService.getLowStockItems()).thenReturn(Flux.just(dto));

        List<LowStockInventoryDTO> result = controller.getLowStockItems()
                .collectList()
                .block();

        assertEquals(1, result != null ? result.size() : 0);
    }

    @Test
    void exportLowStockCsv_devuelveCsvConCabecerasYContenido() {
        when(inventoryService.getLowStockItems()).thenReturn(Flux.empty());
        when(csvExporter.generateInventoryCsv(anyList())).thenReturn("col1,col2\n");

        ResponseEntity<Resource> resp = controller.exportLowStockCsv().block();
        // verificar status
        assertEquals(200, resp != null ? resp.getStatusCodeValue() : 0);
        // verificar headers
        assertEquals("attachment; filename=low_stock_inventory.csv",
                resp != null ? resp.getHeaders().getFirst("Content-Disposition") : null);
        assertEquals("text/csv",
                resp != null && resp.getHeaders().getContentType() != null
                        ? resp.getHeaders().getContentType().toString()
                        : null);
        // verificar contenido
        Resource resource = resp != null ? resp.getBody() : null;
        byte[] bytes = (resource instanceof ByteArrayResource)
                ? ((ByteArrayResource) resource).getByteArray()
                : new byte[0];
        assertEquals("col1,col2\n", new String(bytes, StandardCharsets.UTF_8));
    }


    @Test
    void healthCheck_deberiaResponderOk() {
        ResponseEntity<Void> resp = controller.healthCheck();
        assertEquals(200, resp.getStatusCodeValue());
    }
}
