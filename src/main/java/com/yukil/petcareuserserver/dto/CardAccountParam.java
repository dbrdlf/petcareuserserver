package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
public class CardAccountParam {
    private String ownerName;
    private String cardNumber;
    private Vendor vendor;
    private CustomerParam customerParam;
}
