package com.yukil.petcareuserserver.entity;

import lombok.*;
import org.springframework.jmx.export.annotation.ManagedAttribute;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pet {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String name;
    @Enumerated(EnumType.STRING)
    private PetType petType;
    private LocalDate birthday;

    public void saveCustomer(Customer customer){
        this.customer = customer;
        customer.getPetList().add(this);
    }
}
