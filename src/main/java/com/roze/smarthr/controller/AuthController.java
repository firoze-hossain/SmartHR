package com.roze.smarthr.controller;

import com.roze.smarthr.dto.AuthenticationRequest;
import com.roze.smarthr.dto.AuthenticationResponse;
import com.roze.smarthr.dto.RegisterRequest;
import com.roze.smarthr.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    //@PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}