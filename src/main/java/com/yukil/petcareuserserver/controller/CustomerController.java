package com.yukil.petcareuserserver.controller;

import com.yukil.petcareuserserver.common.ResponseMessage;
import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.hateoas.AddressDtoAssembler;
import com.yukil.petcareuserserver.hateoas.CardAccountDtoAssembler;
import com.yukil.petcareuserserver.hateoas.CustomerDtoAssembler;
import com.yukil.petcareuserserver.hateoas.PetDtoAssembler;
import com.yukil.petcareuserserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
        CustomerDto customerDto = customerService.getCustomer(id);
        if (customerDto == null) {
            return ResponseEntity.noContent().build();
        }
        customerDto.add(linkTo(CustomerController.class).slash(id).withRel("update-customer"));
        customerDto.add(linkTo(CustomerController.class).slash(id).withRel("delete-customer"));
        customerDto.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerDto.add(Link.of("docs/index.html#resources-get-customer", "profile"));
        return ResponseEntity.ok(customerDto);
    }

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody CustomerParam customerParam, Errors errors, HttpServletRequest request) {
        CustomerDto customerDto = customerService.createCustomer(customerParam);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        WebMvcLinkBuilder selfLinkBuilder = linkTo(CustomerController.class).slash(customerDto.getId());
        customerDto.add(linkTo(CustomerController.class).slash(customerDto.getId()).withRel("update-customer"));
        customerDto.add(linkTo(CustomerController.class).slash(customerDto.getId()).withRel("delete-customer"));
        customerDto.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerDto.add(Link.of("docs/index.html#resources-add-customer", "profile"));
        return ResponseEntity.created(selfLinkBuilder.toUri()).body(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerParam param, Errors errors) {
        CustomerDto customerDto = customerService.updateCustomer(id, param);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
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

    @GetMapping("/{id}/pet")
    public ResponseEntity queryPet(@PathVariable("id") Long id){
        CollectionModel<PetDto> petDtos = customerService.queryPet(id);
        return ResponseEntity.ok(petDtos);
    }


    @GetMapping("/address/{id}")
    public ResponseEntity getAddress(@PathVariable("id") Long id){
        AddressDto addressDto = customerService.getAddress(id);
        if (addressDto == null) {
            return ResponseEntity.noContent().build();
        }
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
        AddressDto addressDto = customerService.updateAddress(id, addressParam);
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
        CardAccountDto cardAccountDto = customerService.getCard(id);
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
        CardAccountDto cardAccountDto = customerService.updateCard(id, cardAccountParam);
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
        PetDto petDto = customerService.getPet(id);
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

        PetDto petDto = customerService.updatePet(id, petParam);
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
        PagedModel<CustomerDto> pagedModel = customerService.queryCustomers(pageable, condition);
        if (pagedModel == null) {
            return ResponseEntity.noContent().build();
        }
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
