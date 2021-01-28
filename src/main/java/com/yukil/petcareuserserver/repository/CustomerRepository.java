package com.yukil.petcareuserserver.repository;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.repository.custom.CustomerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
}
