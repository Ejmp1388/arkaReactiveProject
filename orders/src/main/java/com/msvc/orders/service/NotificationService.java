package com.msvc.orders.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class NotificationService {

    private final WebClient webClient;

    @Value("${notification.api.url}")
    private String notificationApiUrl; // URL del API Gateway

    public NotificationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<Void> sendEmail(String to, String subject, String body) {
        Map<String, Object> payload = Map.of(
                "to", to,
                "subject", subject,
                "body", body
        );

        return webClient.post()
                .uri(notificationApiUrl)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    System.err.println("Error enviando correo: " + e.getMessage());
                    return Mono.empty();
                });
    }
}

