package com.roze.smarthr.service;


import com.roze.smarthr.dto.AuthenticationRequest;
import com.roze.smarthr.dto.AuthenticationResponse;
import com.roze.smarthr.dto.RegisterRequest;
import com.roze.smarthr.dto.RegisterResponse;
import com.roze.smarthr.entity.User;

public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    User createUser(User user, String roleName);
}