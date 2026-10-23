package com.example.microbankingproject.repository;

import com.example.microbankingproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByPhone(String phone);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.phone = :phone")
    UserEntity findByPhoneWithRoles(@Param("phone") String phone);
}
