package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.AuthenticationResponse;
import com.roze.smarthr.dto.RegisterRequest;
import com.roze.smarthr.dto.RegisterResponse;
import com.roze.smarthr.entity.Role;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.RoleRepository;
import com.roze.smarthr.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    public User toEntity(RegisterRequest request) {
        List<Role> roleEntities = request.getRoles().stream()
                .map(title -> roleRepository.findByTitle(title)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + title)))
                .toList();

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleEntities)
                .build();
        return user;
    }

    public AuthenticationResponse toDto(User user) {
        String jwtToken = jwtService.generateToken(user);
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
        return response;
    }

    public RegisterResponse registerResponse(User user) {
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setEnabled(user.getEnabled());
        response.setRoles(user.getRoles().stream().map(Role::getTitle).toList());
        response.setUsername(user.getUsername());
        return response;
    }
}