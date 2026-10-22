package com.example.microbankingproject.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserDetailsService extends UserDetailsService {

    UserDetails loadUserByUsername(String identifier);
}
