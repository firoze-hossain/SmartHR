package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.AuthenticationRequest;
import com.roze.smarthr.dto.AuthenticationResponse;
import com.roze.smarthr.dto.RegisterRequest;
import com.roze.smarthr.dto.RegisterResponse;
import com.roze.smarthr.entity.Role;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.DuplicateResourceException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.UserMapper;
import com.roze.smarthr.repository.RoleRepository;
import com.roze.smarthr.repository.UserRepository;
import com.roze.smarthr.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException("This email:\"" + request.getEmail() + "\" already associated with other user ");
        }
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new DuplicateResourceException("This username:\"" + request.getUsername() + "\" already associated with other user ");
        }
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.registerResponse(savedUser);

    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        return userMapper.toDto(user);
    }

    @Override
    public User createUser(User user, String roleName) {
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + user.getEmail());
        }
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            throw new DuplicateResourceException("Username already in use: " + user.getUsername());
        }

        Role candidateRole = roleRepository.findByTitle(roleName.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.setRoles(new ArrayList<>(List.of(candidateRole)));
        user.setEnabled(true);

        return userRepository.save(user);
    }
}