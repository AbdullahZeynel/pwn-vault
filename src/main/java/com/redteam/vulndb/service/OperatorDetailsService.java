package com.redteam.vulndb.service;

import com.redteam.vulndb.entity.Operator;
import com.redteam.vulndb.repository.OperatorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation for Spring Security authentication.
 */
@Service
public class OperatorDetailsService implements UserDetailsService {

    private final OperatorRepository operatorRepository;

    public OperatorDetailsService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Operator operator = operatorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Operator bulunamadı: " + username));

        return User.builder()
                .username(operator.getUsername())
                .password(operator.getPassword())
                .roles("OPERATOR")
                .build();
    }
}
