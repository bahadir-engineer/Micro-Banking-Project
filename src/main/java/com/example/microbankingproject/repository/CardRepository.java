package com.example.microbankingproject.repository;

import com.example.microbankingproject.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
}
