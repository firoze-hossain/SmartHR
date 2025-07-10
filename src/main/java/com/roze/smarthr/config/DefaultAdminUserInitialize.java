package com.roze.smarthr.config;

import com.roze.smarthr.entity.Role;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.repository.RoleRepository;
import com.roze.smarthr.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultAdminUserInitialize {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @PostConstruct
    @Transactional
    public void initUsers() {
        initRoles();
        createUserIfNotExist("firoze", "firoze.hossain@outlook.com", "firoze28", true, "ADMIN");
    }

    private void createUserIfNotExist(String username, String email, String password, Boolean enabled, String roleName) {
        if (userRepository.findByEmail(email).isEmpty()) {
            Role role = roleRepository.findByTitle(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.setEnabled(enabled);
            user.setRoles(List.of(role));
            userRepository.save(user);
        }
    }

    @Transactional
    public void initRoles() {
        createRoleIfNotExist("ADMIN");
        createRoleIfNotExist("HR");
        createRoleIfNotExist("EMPLOYEE");
    }

    private void createRoleIfNotExist(String roleName) {
        if (roleRepository.findByTitle(roleName).isEmpty()) {
            Role role = new Role();
            role.setTitle(roleName);
            roleRepository.save(role);
        }
    }
}
