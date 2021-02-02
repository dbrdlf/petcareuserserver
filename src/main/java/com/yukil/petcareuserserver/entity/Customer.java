package com.yukil.petcareuserserver.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private LocalDate birthday;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CardAccount> cardAccountList = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Pet> petList = new ArrayList<>();

    public void changeAddress(Address address) {
        address.setCustomer(this);
        this.setAddress(address);
    }


    public void addCardAccount(CardAccount cardAccount) {
        cardAccount.setCustomer(this);
        cardAccountList.add(cardAccount);
    }

    public void addPet(Pet pet) {
        pet.setCustomer(this);
        petList.add(pet);
    }


}
