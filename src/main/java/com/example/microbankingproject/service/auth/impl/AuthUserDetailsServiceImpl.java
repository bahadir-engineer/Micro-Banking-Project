package com.example.microbankingproject.service.auth.impl;

import com.example.microbankingproject.entity.UserEntity;
import com.example.microbankingproject.exception.ApplicationException;
import com.example.microbankingproject.exception.CommonErrors;
import com.example.microbankingproject.repository.UserRepository;
import com.example.microbankingproject.service.auth.AuthUserDetailsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthUserDetailsServiceImpl implements AuthUserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) {
        UserEntity userEntity = userRepository.findByPhoneWithRoles(identifier);
        if (userEntity != null) {
            return (UserDetails) userEntity;
        }
        throw new ApplicationException(CommonErrors.ACCOUNT_NOT_FOUND);
    }
}
