package com.yukil.petcareuserserver.hateoas;

import com.yukil.petcareuserserver.controller.CustomerController;
import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.entity.Address;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Pet;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerDtoAssembler extends RepresentationModelAssemblerSupport<Customer, CustomerDto> {

    public CustomerDtoAssembler() {
        super(CustomerController.class, CustomerDto.class);
    }

    @Override
    public CustomerDto toModel(Customer entity) {
        CustomerDto customerDto = instantiateModel(entity);
        customerDto.add(linkTo(methodOn(CustomerController.class).getCustomer(entity.getId())).withSelfRel());

        customerDto = CustomerDto.builder()
                .address(toAddressDto(entity.getAddress()))
                .id(entity.getId())
                .name(entity.getName())
                .birthday(entity.getBirthday())
                .email(entity.getEmail())
                .pets(toPetDto(entity.getPetList()))
                .cardAccounts(toCardDto(entity.getCardAccountList()))
                .phoneNumber(entity.getPhoneNumber())
                .age(getAge(entity.getBirthday()))
                .build();
//        customerDto.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerDto.add(linkTo(CustomerController.class).slash(entity.getId()).withSelfRel());
        return customerDto;
    }



    @Override
    public CollectionModel<CustomerDto> toCollectionModel(Iterable<? extends Customer> customers) {
        CollectionModel<CustomerDto> customerDtos = super.toCollectionModel(customers);
        customerDtos.add(linkTo(methodOn(CustomerController.class).queryCustomers(Pageable.unpaged(), new CustomerSearchCondition())).withSelfRel());
        return customerDtos;
    }

    private AddressDto toAddressDto(Address address) {
        if (address == null) {
            return new AddressDto();
        }

        return AddressDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .street(address.getStreet())
                .zipcode(address.getZipcode())

                .build()
                .add(linkTo(methodOn(CustomerController.class)
                        .getAddress(address.getId())
                ).withSelfRel())
                ;
    }

    private List<CardAccountDto> toCardDto(List<CardAccount> cardAccountList) {
        if (cardAccountList.isEmpty()) {
            return Collections.emptyList();
        }

        return cardAccountList.stream().map(cardAccount -> CardAccountDto.builder()
                .id(cardAccount.getId())
                .cardNumber(cardAccount.getCardNumber())
                .ownerName(cardAccount.getOwnerName())
                .vendor(cardAccount.getVendor())
                .build()
                .add(linkTo(
                        methodOn(CustomerController.class)
                                .getCard(cardAccount.getId()))
                        .withSelfRel()
                )).collect(Collectors.toList());
    }

    private List<PetDto> toPetDto(List<Pet> petList) {
        if (petList.isEmpty()) {
            return Collections.emptyList();
        }

        return petList.stream().map(pet -> PetDto.builder()
                .id(pet.getId())
                .age(getAge(pet.getBirthday()))
                .birthday(pet.getBirthday())
                .name(pet.getName())
                .petType(pet.getPetType())
                .build()
                .add(linkTo(
                        methodOn(CustomerController.class)
                                .getPet(pet.getId()))
                        .withSelfRel()
                )).collect(Collectors.toList());
    }

    private Integer getAge(LocalDate birthday) {
        if (birthday == null) {
            return 0;
        }
        Period period = birthday.until(LocalDate.now());
        return period.getYears();
    }


}
