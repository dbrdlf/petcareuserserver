package com.yukil.petcareuserserver.service;

import com.yukil.petcareuserserver.dto.CustomerDto;
import com.yukil.petcareuserserver.entity.Customer;

public interface CustomerService {
    CustomerDto getCustomer(Long id);
    Long createCustomer();
    Customer updateCustomer(Customer customer);
    void deleteCustomer(Customer customer);

}
