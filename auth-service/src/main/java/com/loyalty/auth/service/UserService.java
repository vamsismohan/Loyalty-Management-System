package com.loyalty.auth.service;

import java.time.LocalDateTime;

import javax.management.RuntimeErrorException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loyalty.auth.dto.CreateUserRequest;
import com.loyalty.auth.entity.Role;
import com.loyalty.auth.entity.User;
import com.loyalty.auth.entity.UserRole;
import com.loyalty.auth.exception.RoleNotFoundException;
import com.loyalty.auth.exception.UserAlreadyExistsException;
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

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(request.getUsername());
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setStatus(request.getStatus());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        for (Long roleId : request.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RoleNotFoundException(roleId));
            userRoleRepository.save(new UserRole(savedUser, role)
            );
        }

        return savedUser;
    }
}
