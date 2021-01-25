package com.yukil.petcareuserserver.entity;

import lombok.*;

import javax.persistence.*;
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
    private Integer age;

    @OneToMany(mappedBy = "customer")
    @Builder.Default
    private List<Address> addressList = new ArrayList<>();
    @OneToMany(mappedBy = "customer")
    @Builder.Default
    private List<CardAccount> cardAccountList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    @Builder.Default
    private List<Pet> petList = new ArrayList<>();



}
