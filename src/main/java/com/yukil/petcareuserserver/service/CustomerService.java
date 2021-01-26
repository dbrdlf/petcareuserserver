package com.yukil.petcareuserserver.service;

import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.entity.Customer;

public interface CustomerService {
    CustomerDto getCustomer(Long id);
    CustomerDto createCustomer(CustomerParam param);
    Customer updateCustomer(Customer customer);
    void deleteCustomer(Customer customer);

    AddressDto addAddress(AddressParam param, Long customerId);

    CardAccountDto addCard(CardAccountParam param);


    PetDto addPet(PetParam param);
}
