package com.example.OzonHelper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class MaxWebHookController {

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String body,
                                                @RequestHeader(value = "X-Max-Bot-Api-Secret", required = false) String secret) {
        System.out.println("Получен webhook: " + body);

        return ResponseEntity.ok("OK");
    }
}
