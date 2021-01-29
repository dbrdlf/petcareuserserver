package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Address;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Pet;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "email")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerParam {
    private String email;
    private String name;
    private String phoneNumber;
    private String password;
    private Integer age;
    private AddressParam addressParam;
    @Builder.Default
    private List<CardAccountParam> cardAccountList = new ArrayList<>();
    @Builder.Default
    private List<PetParam> petList = new ArrayList<>();
}
