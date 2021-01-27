package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Vendor;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CardAccountDto {
    private Long id;
    private String ownerName;
    private String cardNumber;
    private Vendor vendor;
}
