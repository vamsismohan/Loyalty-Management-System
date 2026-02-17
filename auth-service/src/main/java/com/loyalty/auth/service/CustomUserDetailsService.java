package com.loyalty.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.loyalty.auth.entity.Privilege;
import com.loyalty.auth.entity.Role;
import com.loyalty.auth.entity.RolePrivilege;
import com.loyalty.auth.entity.User;
import com.loyalty.auth.entity.UserRole;
import com.loyalty.auth.repository.PrivilegeRepository;
import com.loyalty.auth.repository.RolePrivilegeRepository;
import com.loyalty.auth.repository.RoleRepository;
import com.loyalty.auth.repository.UserRepository;
import com.loyalty.auth.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final PrivilegeRepository privilegeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserRole ur : userRoles) {
            Role role = roleRepository.findById(ur.getRoleId()).orElseThrow();
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

            List<RolePrivilege> rolePrivileges =
                    rolePrivilegeRepository.findByRoleId(role.getId());

            for (RolePrivilege rp : rolePrivileges) {
                Privilege privilege =
                        privilegeRepository.findById(rp.getPrivilegeId()).orElseThrow();
                authorities.add(
                        new SimpleGrantedAuthority(privilege.getPrivilegeName()));
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
