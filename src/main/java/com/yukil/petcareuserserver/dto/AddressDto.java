package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto extends RepresentationModel<AddressDto> {
    private Long id;
    private String city;
    private String street;
    private Integer zipcode;
}
