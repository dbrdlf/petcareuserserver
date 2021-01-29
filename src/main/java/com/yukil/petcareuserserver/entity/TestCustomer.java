package com.yukil.petcareuserserver.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@Entity
public class TestCustomer {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private Integer age;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CardAccount> cardAccountList = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Pet> petList = new ArrayList<>();


    public void addCardAccount(CardAccount cardAccount) {
        cardAccountList.add(cardAccount);
    }

    public void addPet(Pet pet) {
        petList.add(pet);
    }
}
