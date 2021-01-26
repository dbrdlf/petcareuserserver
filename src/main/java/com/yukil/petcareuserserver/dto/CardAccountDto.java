package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardAccountDto {
    private Long id;
    private String ownerName;
    private String cardNumber;
    private Vendor vendor;
    private Customer customer;
}
