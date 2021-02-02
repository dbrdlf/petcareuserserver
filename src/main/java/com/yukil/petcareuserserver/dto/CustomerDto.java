package com.yukil.petcareuserserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yukil.petcareuserserver.entity.Address;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Pet;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "customers", itemRelation = "customer")
public class CustomerDto extends RepresentationModel<CustomerDto> {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDate birthday;
    private Integer age;
    private AddressDto address;
    private List<CardAccountDto> cardAccounts = new ArrayList<>();
    private List<PetDto> pets = new ArrayList<>();

}
