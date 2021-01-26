package com.yukil.petcareuserserver.repository;

import com.yukil.petcareuserserver.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
