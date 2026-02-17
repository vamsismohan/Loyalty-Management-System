package com.loyalty.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.loyalty.auth.entity.Role;
import com.loyalty.auth.entity.RolePrivilege;
import com.loyalty.auth.entity.User;
import com.loyalty.auth.entity.UserRole;
import com.loyalty.auth.repository.RolePrivilegeRepository;
import com.loyalty.auth.repository.UserRepository;
import com.loyalty.auth.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        List<UserRole> userRoles =
                userRoleRepository.findByUserId(user.getId());

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserRole ur : userRoles) {

            Role role = ur.getRole();
            authorities.add(
                    new SimpleGrantedAuthority(role.getRoleName())
            );

            List<RolePrivilege> rps =
                    rolePrivilegeRepository.findByRoleId(role.getId());

            for (RolePrivilege rp : rps) {
                authorities.add(
                        new SimpleGrantedAuthority(
                                rp.getPrivilege().getPrivilegeName()
                        )
                );
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
