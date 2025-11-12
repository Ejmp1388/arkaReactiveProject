package com.msvc.customer.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyController {

    @GetMapping("/healthz")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
