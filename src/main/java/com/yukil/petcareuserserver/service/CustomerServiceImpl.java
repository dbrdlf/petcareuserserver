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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (!optionalCustomer.isPresent()) {
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
    @Transactional
    public AddressDto addAddress(AddressParam param, Long customerId) {
        Address address = modelMapper.map(param, Address.class);
        Address createdAddress = addressRepository.save(address);

        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            return null;
        }
        Customer customer = optionalCustomer.get();

        customer.setAddress(createdAddress);
//        customerRepository.save(customer);
        AddressDto addressDto = modelMapper.map(createdAddress, AddressDto.class);
        return addressDto;
    }
    @Override
    @Transactional
    public List<CardAccountDto> addCard(List<CardAccountParam> param, Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            return null;
        }
        Customer customer = optionalCustomer.get();
        List<CardAccount> cardAccountList = param.stream().map(p -> {
            CardAccount cardAccount = modelMapper.map(p, CardAccount.class);
            cardAccount.setCustomer(customer);
            return cardAccount;
        }).collect(Collectors.toList());
        List<CardAccount> createdCardAccount = cardAccountRepository.saveAll(cardAccountList);
        return createdCardAccount.stream().map(c -> modelMapper.map(c, CardAccountDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<PetDto> addPet(List<PetParam> param, Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            return null;
        }
        Customer customer = optionalCustomer.get();
        List<Pet> petList = param.stream().map(p ->{
            Pet pet = modelMapper.map(p, Pet.class);
            pet.setCustomer(customer);
            return pet;
        }).collect(Collectors.toList());
        List<Pet> createdPet = petRepository.saveAll(petList);

        return createdPet.stream().map(p -> modelMapper.map(p, PetDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto changeAddress(Long addressId, AddressParam addressParam) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (!optionalAddress.isPresent()) {
            return null;
        }

        Address address = optionalAddress.get();
        address.setCity(addressParam.getCity());
        address.setStreet(addressParam.getStreet());
        address.setZipcode(addressParam.getZipcode());
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public Long deleteAddress(Long addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (!optionalAddress.isPresent()) {
            return null;
        }
        addressRepository.delete(optionalAddress.get());
        return addressId;
    }
}
