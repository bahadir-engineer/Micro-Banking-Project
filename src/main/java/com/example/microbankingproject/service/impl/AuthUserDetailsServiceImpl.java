package com.example.microbankingproject.service.impl;

import com.example.investhome.entity.AgentEntity;
import com.example.investhome.entity.UserEntity;
import com.example.investhome.exception.ApplicationException;
import com.example.investhome.exception.CommonErrors;
import com.example.investhome.repository.AgentRepository;
import com.example.investhome.repository.UserRepository;
import com.example.investhome.service.auth.AuthUserDetailsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthUserDetailsServiceImpl implements AuthUserDetailsService {

    AgentRepository agentRepository;
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) {
        if (identifier.startsWith("+")) {
            UserEntity userEntity = userRepository.findByPhoneWithRoles(identifier);
            if (userEntity != null) {
                return userEntity;
            }

            AgentEntity agentEntity = agentRepository.findByPhoneWithRoles(identifier);
            if (agentEntity != null) {
                return agentEntity;
            }
        } else {
            UserEntity userEntity = userRepository.findByEmailWithRoles(identifier);
            if (userEntity != null) {
                return userEntity;
            }

            AgentEntity agentEntity = agentRepository.findByEmailWithRoles(identifier);
            if (agentEntity != null) {
                return agentEntity;
            }
        }
        throw new ApplicationException(CommonErrors.ACCOUNT_NOT_FOUND);
    }
}
