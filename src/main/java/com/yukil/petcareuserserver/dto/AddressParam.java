package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
public class AddressParam {
    private String city;
    private String street;
    private Integer zipcode;
}
