package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.PetType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class PetDto {
    private Long id;
    private String name;
    private PetType petType;
    private Integer age;
}
