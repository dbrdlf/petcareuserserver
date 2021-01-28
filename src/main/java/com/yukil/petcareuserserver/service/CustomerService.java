package com.yukil.petcareuserserver.service;

import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    CustomerDto getCustomer(Long id);
    CustomerDto createCustomer(CustomerParam param);
    Customer updateCustomer(Customer customer);
    void deleteCustomer(Customer customer);

    AddressDto addAddress(AddressParam param, Long customerId);

    List<CardAccountDto> addCard(List<CardAccountParam> param, Long customerId);


    List<PetDto> addPet(List<PetParam> param, Long customerId);

    AddressDto changeAddress(Long addressId, AddressParam addressParam);

    Long deleteAddress(Long addressId);

    CardAccountDto changeCard(Long cardId, CardAccountParam cardAccountParam);

    Long deleteCard(Long cardId);

    PetDto changePet(Long petId, PetParam petParam);

    Long deletePet(Long petId);

    Page<CustomerDto> queryCustomers(Pageable pageable, CustomerParam customerParam);
}
