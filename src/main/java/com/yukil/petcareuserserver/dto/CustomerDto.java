package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Address;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Pet;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private Integer age;
    private Address address;
    private List<CardAccount> cardAccountList = new ArrayList<>();
    private List<Pet> petList = new ArrayList<>();
}
