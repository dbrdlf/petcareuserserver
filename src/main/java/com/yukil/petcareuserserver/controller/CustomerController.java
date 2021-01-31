package com.yukil.petcareuserserver.controller;

import com.yukil.petcareuserserver.common.ResponseMessage;
import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.entity.Address;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.Pet;
import com.yukil.petcareuserserver.hateoas.AddressDtoAssembler;
import com.yukil.petcareuserserver.hateoas.CardAccountDtoAssembler;
import com.yukil.petcareuserserver.hateoas.CustomerDtoAssembler;
import com.yukil.petcareuserserver.hateoas.PetDtoAssembler;
import com.yukil.petcareuserserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/customer", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDtoAssembler customerDtoAssembler;
    private final AddressDtoAssembler addressDtoAssembler;
    private final CardAccountDtoAssembler cardAccountDtoAssembler;
    private final PetDtoAssembler petDtoAssembler;
    private final PagedResourcesAssembler<Customer> pagedResourcesAssembler;

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable("id") Long id) {
        Customer customer = customerService.getCustomer(id);
        if (customer == null) {
            return ResponseEntity.noContent().build();
        }
        CustomerDto customerDto = customerDtoAssembler.toModel(customer);

        customerDto.add(linkTo(CustomerController.class).slash(id).withRel("update-customer"));
        customerDto.add(linkTo(CustomerController.class).slash(id).withRel("delete-customer"));
        customerDto.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerDto.add(Link.of("docs/index.html#resources-get-customer", "profile"));
        return ResponseEntity.ok(customerDto);
    }

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody CustomerParam customerParam, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Customer customer = customerService.createCustomer(customerParam);
        CustomerDto customerDto = customerDtoAssembler.toModel(customer);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(CustomerController.class).slash(customerDto.getId());
        customerDto.add(linkTo(CustomerController.class).slash(customerDto.getId()).withRel("update-customer"));
        customerDto.add(linkTo(CustomerController.class).slash(customerDto.getId()).withRel("delete-customer"));
        customerDto.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerDto.add(Link.of("docs/index.html#resources-add-customer", "profile"));
        return ResponseEntity.created(selfLinkBuilder.toUri()).body(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerParam param, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Customer customer = customerService.updateCustomer(id, param);
        CustomerDto customerDto = customerDtoAssembler.toModel(customer);
        customerDto.add(linkTo(CustomerController.class).slash(customerDto.getId()).withRel("delete-customer"));
        customerDto.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerDto.add(Link.of("docs/index.html#resources-update-customer", "profile"));
        return ResponseEntity.ok(customerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@PathVariable("id") Long id) {
        Long deleteCustomer = customerService.deleteCustomer(id);
        ResponseMessage<Long> responseMessage = new ResponseMessage<>(deleteCustomer);
        EntityModel customerResource = EntityModel.of(responseMessage);
        customerResource.add(linkTo(methodOn(CustomerController.class).deleteCustomer(id)).withSelfRel());
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-delete-customer", "profile"));

        return ResponseEntity.ok(customerResource);
    }


    @GetMapping("/address/{id}")
    public ResponseEntity getAddress(@PathVariable("id") Long id){
        Address address = customerService.getAddress(id);
        if (address == null) {
            return ResponseEntity.noContent().build();
        }
        AddressDto addressDto = addressDtoAssembler.toModel(address);
        addressDto.add(linkTo(methodOn(CustomerController.class).getAddress(id)).slash(id).withRel("update-address"));
        addressDto.add(linkTo(methodOn(CustomerController.class).getAddress(id)).slash(id).withRel("delete-address"));
        addressDto.add(Link.of("docs/index.html#resources-get-address", "profile"));
        return ResponseEntity.ok(addressDto);
    }



    @PutMapping("/address/{id}")
    public ResponseEntity updateAddress(
                                        @PathVariable("id") Long id,
                                        @RequestBody AddressParam addressParam,
                                        Errors errors){
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Address address = customerService.updateAddress(id, addressParam);
        AddressDto addressDto = addressDtoAssembler.toModel(address);
        addressDto.add(linkTo(methodOn(CustomerController.class).getAddress(id)).withRel("delete-address"));
        addressDto.add(Link.of("docs/index.html#resources-update-address", "profile"));

        return ResponseEntity.ok(addressDto);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity deleteAddress(
                                        @PathVariable("id") Long id) {
        Long deleteAddress = customerService.deleteAddress(id);
        ResponseMessage<Long> responseMessage = new ResponseMessage<>(deleteAddress);
        EntityModel customerResource = EntityModel.of(responseMessage);
        customerResource.add(linkTo(methodOn(CustomerController.class).deleteAddress(id)).withSelfRel());
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-delete-address", "profile"));

        return ResponseEntity.ok(customerResource);
    }


    @GetMapping("/card/{id}")
    public ResponseEntity getCard(@PathVariable("id") Long id){
        CardAccount cardAccount = customerService.getCard(id);
        CardAccountDto cardAccountDto = cardAccountDtoAssembler.toModel(cardAccount);
        cardAccountDto.add(linkTo(methodOn(CustomerController.class).getCard(id)).withRel("delete-card"));
        cardAccountDto.add(linkTo(methodOn(CustomerController.class).getCard(id)).withRel("update-card"));
        cardAccountDto.add(Link.of("docs/index.html#resources-get-card", "profile"));

        return ResponseEntity.ok(cardAccountDto);
    }

    @PutMapping("/card/{id}")
    public ResponseEntity updateCard(
                                     @PathVariable("id") Long id,
                                     @RequestBody CardAccountParam cardAccountParam,
                                     Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        CardAccount cardAccount = customerService.updateCard(id, cardAccountParam);
        CardAccountDto cardAccountDto = cardAccountDtoAssembler.toModel(cardAccount);
        cardAccountDto.add(linkTo(methodOn(CustomerController.class).updateCard(id, cardAccountParam, errors)).withRel("delete-card"));
        cardAccountDto.add(Link.of("docs/index.html#resources-update-card", "profile"));
        return ResponseEntity.ok(cardAccountDto);
    }

    @DeleteMapping("/card/{id}")
    public ResponseEntity deleteCard(
                                     @PathVariable("id") Long id
                                     ){
        Long deletedCardId = customerService.deleteCard(id);
        if (deletedCardId == null) {
            return ResponseEntity.noContent().build();
        }
        ResponseMessage<Long> responseMessage = new ResponseMessage<>(deletedCardId);
        EntityModel customerResource = EntityModel.of(responseMessage);
        customerResource.add(linkTo(methodOn(CustomerController.class).deleteCard(id)).withSelfRel());
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-delete-address", "profile"));
        return ResponseEntity.ok(customerResource);
    }


    @GetMapping("/pet/{id}")
    public ResponseEntity getPet(@PathVariable("id") Long id){
        Pet pet = customerService.getPet(id);
        PetDto petDto = petDtoAssembler.toModel(pet);
        petDto.add(linkTo(methodOn(CustomerController.class).getPet(id)).withRel("delete-pet"));
        petDto.add(linkTo(methodOn(CustomerController.class).getPet(id)).withRel("update-pet"));
        petDto.add(Link.of("docs/index.html#resources-get-card", "profile"));

        return ResponseEntity.ok(petDto);
    }


    @PutMapping("/pet/{id}")
    public ResponseEntity updatePet(
                                    @PathVariable("id") Long id,
                                    @RequestBody PetParam petParam,
                                    Errors errors) {

        Pet pet = customerService.updatePet(id, petParam);
        PetDto petDto = petDtoAssembler.toModel(pet);
        petDto.add(linkTo(methodOn(CustomerController.class).updatePet(id, petParam, errors)).withRel("delete-pet"));
        petDto.add(Link.of("docs/index.html#resources-update-pet", "profile"));
        return ResponseEntity.ok(petDto);
    }

    @DeleteMapping("/pet/{id}")
    public ResponseEntity deletePet(
                                     @PathVariable("id") Long id
    ){
        Long deletedPetId = customerService.deletePet(id);
        if (deletedPetId == null) {
            return ResponseEntity.noContent().build();
        }
        ResponseMessage<Long> responseMessage = new ResponseMessage<>(deletedPetId);
        EntityModel customerResource = EntityModel.of(responseMessage);
        customerResource.add(linkTo(methodOn(CustomerController.class).deletePet(id)).withSelfRel());
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-delete-pet", "profile"));
        return ResponseEntity.ok(customerResource);
    }



    @GetMapping
    public ResponseEntity queryCustomers(Pageable pageable, CustomerSearchCondition condition) {
        Page<Customer> customerPage = customerService.queryCustomers(pageable, condition);
        if (customerPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        PagedModel<CustomerDto> pagedModel = pagedResourcesAssembler.toModel(customerPage, customerDtoAssembler);
        pagedModel.add(Link.of("docs/index.html#resources-query-customers", "profile"));
        pagedModel.add(linkTo(CustomerController.class).withRel("query-customers"));
        return ResponseEntity.ok(pagedModel);
    }

    public List<CardAccountDto> getAllCard() {
        return null;
    }

    public List<PetDto> getAllPet() {
        return null;
    }
}
