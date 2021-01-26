package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDto {
    private Long id;
    private Customer customer;
    private String name;
    private PetType petType;
    private Integer age;
}
