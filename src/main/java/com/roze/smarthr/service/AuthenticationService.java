package com.roze.smarthr.service;


import com.roze.smarthr.dto.AuthenticationRequest;
import com.roze.smarthr.dto.AuthenticationResponse;
import com.roze.smarthr.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}