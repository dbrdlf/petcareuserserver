package com.yukil.petcareuserserver.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue
    private Long id;
    private String city;
    private String street;
    private Integer zipcode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public void saveCustomer(Customer customer){
        this.customer = customer;
        customer.getAddressList().add(this);
    }
}
