package com.arka.reporte.ventas.controller;

import com.arka.reporte.ventas.dto.ReporteSemanal;
import com.arka.reporte.ventas.service.ReporteService;
import com.arka.reporte.ventas.util.CsvExporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReporteControllerTest {
    @Mock
    private ReporteService reporteService;

    @Mock
    private CsvExporter csvExporter;

    @Test
    void obtenerReporte_deberiaEmitirElementosYCompletar() {
        ReporteSemanal mockReporte = org.mockito.Mockito.mock(ReporteSemanal.class);
        when(reporteService.generarReporte(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Flux.just(mockReporte));

        ReporteController controller = new ReporteController(reporteService, csvExporter);

        StepVerifier.create(controller.obtenerReporte())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void exportarCSV_deberiaDevolverResourceConHeaders() {
        ReporteSemanal r1 = org.mockito.Mockito.mock(ReporteSemanal.class);
        ReporteSemanal r2 = org.mockito.Mockito.mock(ReporteSemanal.class);

        when(reporteService.generarReporte(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Flux.just(r1, r2));

        byte[] csvBytes = "col1,col2\n1,2\n".getBytes();
        when(csvExporter.exportarReporteSemanal(ArgumentMatchers.anyList()))
                .thenReturn(new ByteArrayResource(csvBytes));

        ReporteController controller = new ReporteController(reporteService, csvExporter);

        ResponseEntity<Resource> response = controller.exportarCSV().block();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getFirst("Content-Disposition").contains("reporte_semanal.csv"));
        assertEquals("text/csv", response.getHeaders().getContentType().toString());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ByteArrayResource);
        try {
            ByteArrayResource body = (ByteArrayResource) response.getBody();
            assertEquals(csvBytes.length, body.contentLength());
            assertArrayEquals(csvBytes, body.getByteArray());
        } catch (Exception e) {
            fail("No se pudo leer el body del resource: " + e.getMessage());
        }
    }

    @Test
    void healthCheck_deberiaResponderOk() {
        ReporteController controller = new ReporteController(reporteService, csvExporter);
        ResponseEntity<Void> resp = controller.healthCheck();
        assertEquals(200, resp.getStatusCodeValue());
    }
}
