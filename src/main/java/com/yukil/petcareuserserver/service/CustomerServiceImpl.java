package com.yukil.petcareuserserver.service;

import com.yukil.petcareuserserver.dto.CustomerDto;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public CustomerDto getCustomer(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            return null;
        }
        Customer customer = optionalCustomer.get();
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        return customerDto;
    }

    @Override
    public Long createCustomer() {
        return null;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return null;
    }

    @Override
    public void deleteCustomer(Customer customer) {

    }
}
