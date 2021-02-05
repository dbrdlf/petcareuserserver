package com.yukil.petcareuserserver.service;

import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.entity.Address;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface CustomerService {
    CustomerDto getCustomer(Long id);

    AddressDto getAddress(Long id);
    CustomerDto createCustomer(CustomerParam param);
    CustomerDto updateCustomer(Long id, CustomerParam param);
    Long deleteCustomer(Long id);

    CardAccountDto getCard(Long id);
    List<PetDto> addPet(List<PetParam> param, Long customerId);

    AddressDto updateAddress(Long addressId, AddressParam addressParam);

    Long deleteAddress(Long addressId);

    CardAccountDto updateCard(Long cardId, CardAccountParam cardAccountParam);

    Long deleteCard(Long cardId);

    PetDto updatePet(Long petId, PetParam petParam);

    Long deletePet(Long petId);

    PagedModel<CustomerDto> queryCustomers(Pageable pageable, CustomerSearchCondition condition);

    PetDto getPet(Long id);

    CollectionModel<PetDto> queryPet(Long id);
}
