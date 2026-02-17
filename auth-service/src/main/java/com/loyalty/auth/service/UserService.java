package com.loyalty.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loyalty.auth.dto.CreateUserRequest;
import com.loyalty.auth.entity.Role;
import com.loyalty.auth.entity.User;
import com.loyalty.auth.entity.UserRole;
import com.loyalty.auth.repository.RoleRepository;
import com.loyalty.auth.repository.UserRepository;
import com.loyalty.auth.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        for (Long roleId : request.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow();
            userRoleRepository.save(new UserRole(savedUser, role)
            );
        }

        return savedUser;
    }
}
