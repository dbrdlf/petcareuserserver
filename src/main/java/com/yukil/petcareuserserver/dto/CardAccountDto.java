package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Vendor;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CardAccountDto extends RepresentationModel<CardAccountDto> {
    private Long id;
    private String ownerName;
    private String cardNumber;
    private Vendor vendor;
}
