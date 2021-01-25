package com.yukil.petcareuserserver.repository;

import com.yukil.petcareuserserver.entity.CardAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {
}
