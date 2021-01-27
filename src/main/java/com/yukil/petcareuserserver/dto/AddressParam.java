package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressParam {
    private String city;
    private String street;
    private Integer zipcode;
}
