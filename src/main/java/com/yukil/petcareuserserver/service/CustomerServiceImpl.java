package com.yukil.petcareuserserver.service;

import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.entity.Address;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Pet;
import com.yukil.petcareuserserver.repository.AddressRepository;
import com.yukil.petcareuserserver.repository.CardAccountRepository;
import com.yukil.petcareuserserver.repository.CustomerRepository;
import com.yukil.petcareuserserver.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final CardAccountRepository cardAccountRepository;
    private final PetRepository petRepository;

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
    public CustomerDto createCustomer(CustomerParam param) {
        Customer customer = modelMapper.map(param, Customer.class);

        Customer createdCustomer = customerRepository.save(customer);
        return modelMapper.map(createdCustomer, CustomerDto.class);

    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return null;
    }

    @Override
    public void deleteCustomer(Customer customer) {

    }

    @Override
    public AddressDto addAddress(AddressParam param, Long customerId) {
        Address address = modelMapper.map(param, Address.class);
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            return null;
        }
        Customer customer = optionalCustomer.get();
        Address createdAddress = addressRepository.save(address);
        customer.setAddress(createdAddress);
        createdAddress.setCustomer(customerRepository.save(customer));
        return modelMapper.map(createdAddress, AddressDto.class);
    }
    @Override
    public CardAccountDto addCard(CardAccountParam param) {
        CardAccount cardAccount = modelMapper.map(param, CardAccount.class);
        CardAccount createdCardAccount = cardAccountRepository.save(cardAccount);
        return modelMapper.map(createdCardAccount, CardAccountDto.class);
    }

    @Override
    public PetDto addPet(PetParam param) {
        Pet pet = modelMapper.map(param, Pet.class);
        Pet createdPet = petRepository.save(pet);
        return modelMapper.map(createdPet, PetDto.class);
    }
}
