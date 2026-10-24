package com.example.microbankingproject.repository;

import com.example.microbankingproject.entity.CardEntity;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    Optional<CardEntity> findByCardNumber(Long cardNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CardEntity c WHERE c.cardNumber = :cardNumber")
    Optional<CardEntity> findByCardNumberForUpdate(@Param("cardNumber") Long cardNumber);
}
