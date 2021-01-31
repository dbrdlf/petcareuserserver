package com.yukil.petcareuserserver.service;

import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.entity.Address;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Customer getCustomer(Long id);

    Address getAddress(Long id);
    Customer createCustomer(CustomerParam param);
    Customer updateCustomer(Long id, CustomerParam param);
    Long deleteCustomer(Long id);

    CardAccount getCard(Long id);
    List<PetDto> addPet(List<PetParam> param, Long customerId);

    Address updateAddress(Long addressId, AddressParam addressParam);

    Long deleteAddress(Long addressId);

    CardAccount updateCard(Long cardId, CardAccountParam cardAccountParam);

    Long deleteCard(Long cardId);

    Pet updatePet(Long petId, PetParam petParam);

    Long deletePet(Long petId);

    Page<Customer> queryCustomers(Pageable pageable, CustomerSearchCondition condition);

    Pet getPet(Long id);
}
