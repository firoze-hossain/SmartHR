package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.AuthenticationRequest;
import com.roze.smarthr.dto.AuthenticationResponse;
import com.roze.smarthr.dto.RegisterRequest;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.DuplicateResourceException;
import com.roze.smarthr.mapper.UserMapper;
import com.roze.smarthr.repository.UserRepository;
import com.roze.smarthr.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException("This email:\"" + request.getEmail() + "\" already associated with other user ");
        }
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new DuplicateResourceException("This username:\"" + request.getUsername() + "\" already associated with other user ");
        }
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);

    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        return userMapper.toDto(user);
    }
}