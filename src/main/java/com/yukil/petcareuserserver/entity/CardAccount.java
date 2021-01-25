package com.yukil.petcareuserserver.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class CardAccount {
    @Id
    @GeneratedValue
    private Long id;
    private String ownerName;
    private String cardNumber;
    @Enumerated(EnumType.STRING)
    private Vendor vendor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public void saveCustomer(Customer customer){
        this.customer = customer;
        customer.getCardAccountList().add(this);
    }
}
