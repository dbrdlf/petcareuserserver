package com.yukil.petcareuserserver.hateoas;

import com.yukil.petcareuserserver.controller.CustomerController;
import com.yukil.petcareuserserver.dto.AddressDto;
import com.yukil.petcareuserserver.entity.Address;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AddressDtoAssembler extends RepresentationModelAssemblerSupport<Address, AddressDto> {
    public AddressDtoAssembler() {
        super(CustomerController.class, AddressDto.class);
    }

    @Override
    public AddressDto toModel(Address entity) {
        AddressDto addressDto = AddressDto.builder()
                .id(entity.getId())
                .city(entity.getCity())
                .street(entity.getStreet())
                .zipcode(entity.getZipcode())
                .build();
        addressDto.add(linkTo(methodOn(CustomerController.class).getAddress(entity.getId())).withSelfRel());
        addressDto.add(linkTo(CustomerController.class).withRel("query-customers"));

        return addressDto;
    }
}
