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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final CardAccountRepository cardAccountRepository;
    private final PetRepository petRepository;

    @Override
    public Customer getCustomer(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            return null;
        }
        return  optionalCustomer.get();
    }

    @Override
    public Address getAddress(Long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (!optionalAddress.isPresent()) {
            return null;
        }
        return optionalAddress.get();
    }

    @Override
    public Customer createCustomer(CustomerParam param) {
        Address address = modelMapper.map(param.getAddressParam(), Address.class);
        addressRepository.save(address);
        Customer customer = Customer.builder()
                .name(param.getName())
                .email(param.getEmail())
                .birthday(param.getBirthday())
                .address(address)
                .phoneNumber(param.getPhoneNumber())
                .build();
        customerRepository.save(customer);


        List<CardAccount> cardAccounts = param.getCardAccountList().stream().map(c -> {
            CardAccount cardAccount = CardAccount.builder()
                    .customer(customer)
                    .cardNumber(c.getCardNumber())
                    .ownerName(c.getOwnerName())
                    .vendor(c.getVendor())
                    .build();
            customer.addCardAccount(cardAccount);
            return cardAccount;
        }).collect(Collectors.toList());
        cardAccountRepository.saveAll(cardAccounts);

        List<Pet> pets = param.getPetList().stream().map(p -> {
            Pet pet = Pet.builder()
                    .customer(customer)
                    .birthday(p.getBirthday())
                    .name(p.getName())
                    .build();
            customer.addPet(pet);
            return pet;
        }).collect(Collectors.toList());
        petRepository.saveAll(pets);

        return customer;
    }

    @Override
    public Customer updateCustomer(Long id, CustomerParam param) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            return null;
        }
        Customer customer = optionalCustomer.get();
        customer.setBirthday(param.getBirthday());
        customer.setName(param.getName());
        customer.setEmail(param.getEmail());
        customer.setPassword(param.getPassword());
        customer.setPhoneNumber(param.getPhoneNumber());
        return customer;
    }

    @Override
    public Long deleteCustomer(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            return null;
        }
        addressRepository.delete(optionalCustomer.get().getAddress());
        customerRepository.delete(optionalCustomer.get());
        return id;
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
    public Address updateAddress(Long addressId, AddressParam addressParam) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (!optionalAddress.isPresent()) {
            return null;
        }
        Address address = optionalAddress.get();
        address.setCity(addressParam.getCity());
        address.setStreet(addressParam.getStreet());
        address.setZipcode(addressParam.getZipcode());
        return address;
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

    @Override
    public CardAccount getCard(Long id) {
        Optional<CardAccount> optionalCardAccount = cardAccountRepository.findById(id);
        if (!optionalCardAccount.isPresent()) {
            return null;
        }

        return optionalCardAccount.get();
    }

    @Override
    public Pet getPet(Long id) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        if (!optionalPet.isPresent()) {
            return null;
        }
        return optionalPet.get();
    }

    @Override
    public Long deleteCard(Long cardId) {
        Optional<CardAccount> optionalCardAccount = cardAccountRepository.findById(cardId);
        if (!optionalCardAccount.isPresent()) {
            return null;
        }
        cardAccountRepository.delete(optionalCardAccount.get());
        return cardId;
    }

    @Override
    public Pet updatePet(Long petId, PetParam petParam) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (!optionalPet.isPresent()) {
            return null;
        }

        Pet pet = optionalPet.get();
        pet.setPetType(petParam.getPetType());
        pet.setBirthday(petParam.getBirthday());
        pet.setName(petParam.getName());
        return pet;
    }

    @Override
    public Long deletePet(Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (!optionalPet.isPresent()) {
            return null;
        }
        petRepository.delete(optionalPet.get());
        return petId;
    }

    @Override
    public Page<Customer> queryCustomers(Pageable pageable, CustomerSearchCondition condition) {
        return customerRepository.findByCustomerParam(pageable, condition);
    }

    @Override
    public CardAccount updateCard(Long cardId, CardAccountParam cardAccountParam) {
        Optional<CardAccount> optionalCardAccount = cardAccountRepository.findById(cardId);
        if (!optionalCardAccount.isPresent()) {
            return null;
        }
        CardAccount cardAccount = optionalCardAccount.get();
        cardAccount.setCardNumber(cardAccountParam.getCardNumber());
        cardAccount.setVendor(cardAccountParam.getVendor());
        cardAccount.setOwnerName(cardAccountParam.getOwnerName());
        return cardAccount;
    }
}
