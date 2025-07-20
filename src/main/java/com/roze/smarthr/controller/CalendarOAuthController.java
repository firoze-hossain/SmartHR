package com.roze.smarthr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("calendar")
public class CalendarOAuthController {

    @GetMapping("/oauth2callback")
    public ResponseEntity<String> oauth2Callback(@RequestParam("code") String code) {
        return ResponseEntity.ok("Authorization successful! You can close this window.");
    }
}