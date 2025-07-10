package com.roze.smarthr.service;


import com.roze.smarthr.dto.AuthenticationRequest;
import com.roze.smarthr.dto.AuthenticationResponse;
import com.roze.smarthr.dto.RegisterRequest;
import com.roze.smarthr.dto.RegisterResponse;

public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}