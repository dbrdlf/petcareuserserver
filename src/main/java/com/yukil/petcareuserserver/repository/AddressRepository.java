package com.yukil.petcareuserserver.repository;

import com.yukil.petcareuserserver.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
