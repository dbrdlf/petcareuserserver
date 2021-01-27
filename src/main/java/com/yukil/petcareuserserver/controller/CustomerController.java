package com.yukil.petcareuserserver.controller;

import com.yukil.petcareuserserver.common.ResponseMessage;
import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/customer", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping("/create-customer")
    public ResponseEntity createCustomer(@RequestBody CustomerParam customerParam, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        CustomerDto customerDto = customerService.createCustomer(customerParam);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(CustomerController.class).slash(customerDto.getId());
        URI uri = selfLinkBuilder.toUri();
        ResponseMessage responseMessage = new ResponseMessage(customerDto);
        EntityModel customerResource = EntityModel.of(responseMessage);
        customerResource.add(linkTo(methodOn(CustomerController.class).createCustomer(customerParam, errors)).withSelfRel());
        customerResource.add(selfLinkBuilder.withRel("update-customer"));
        customerResource.add(selfLinkBuilder.withRel("delete-customer"));
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-create-customer", "profile"));
        return ResponseEntity.created(uri).body(customerResource);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity addAddress(@PathVariable("id") Long customerId, @RequestBody AddressParam addressParam, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        AddressDto addressDto = customerService.addAddress(addressParam, customerId);
        if (addressDto == null) {
            return ResponseEntity.badRequest().build();
        }
        ResponseMessage responseMessage = new ResponseMessage(addressDto);
        EntityModel customerResource = EntityModel.of(responseMessage);
        customerResource.add(linkTo(methodOn(CustomerController.class).addAddress(customerId, addressParam, errors)).withSelfRel());
        customerResource.add(linkTo(methodOn(CustomerController.class).deleteAddress(customerId, addressDto.getId())).withRel("delete-address"));
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-address-create", "profile"));
        return ResponseEntity.ok(customerResource);
    }

    @PutMapping("/{id}/address/{addressId}")
    public ResponseEntity changeAddress(@PathVariable("id") Long customerId,
                                        @PathVariable("addressId") Long addressId,
                                        @RequestBody AddressParam addressParam,
                                        Errors errors){
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        AddressDto addressDto = customerService.changeAddress(addressId, addressParam);
        EntityModel customerResource = EntityModel.of(addressDto);
        customerResource.add(linkTo(methodOn(CustomerController.class).addAddress(customerId, addressParam, errors)).withRel("create-address"));
        customerResource.add(linkTo(methodOn(CustomerController.class).changeAddress(customerId, addressId, addressParam, errors)).withSelfRel());
        customerResource.add(linkTo(methodOn(CustomerController.class).addAddress(customerId, addressParam, errors)).withRel("delete-address"));
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-address-update", "profile"));
        ResponseMessage<EntityModel> message = new ResponseMessage(customerResource);

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}/address/{addressId}")
    public ResponseEntity deleteAddress(@PathVariable("id") Long customerId,
                                        @PathVariable("addressId") Long addressId) {
        Long deleteAddress = customerService.deleteAddress(addressId);
        EntityModel customerResource = EntityModel.of(deleteAddress);
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-address-delete", "profile"));
        ResponseMessage<Long> responseMessage = new ResponseMessage<>(deleteAddress);
        return ResponseEntity.ok(responseMessage);
    }

    @PutMapping("/{id}/card")
    public ResponseEntity addCard(@PathVariable("id") Long customerId, @RequestBody List<CardAccountParam> cardAccountParam, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        List<CardAccountDto> cardAccountDto = customerService.addCard(cardAccountParam, customerId);
        if (cardAccountDto == null) {
            return ResponseEntity.badRequest().build();
        }
        List<EntityModel> customerResources = cardAccountDto.stream().map(c -> {
                    EntityModel<CardAccountDto> entityModel = EntityModel.of(c);
                    entityModel.add(linkTo(methodOn(CustomerController.class).addCard(customerId, cardAccountParam, errors)).withSelfRel());
                    entityModel.add(linkTo(methodOn(CustomerController.class).changeCard(customerId, c.getId(), new CardAccountParam(), errors)).withRel("update-card"));
                    entityModel.add(linkTo(methodOn(CustomerController.class).deleteCard(customerId, c.getId(), errors)).withRel("delete-card"));
                    return entityModel;
                }
        ).collect(Collectors.toList());
        return ResponseEntity.ok().body(CollectionModel.of(customerResources)
                .add(Link.of("docs/index.html#resources-card-create", "profile"))
                .add(linkTo(CustomerController.class).withRel("query-customers"))
        );
    }

    @PutMapping("/{id}/card/{cardId}")
    public ResponseEntity changeCard(@PathVariable("id") Long customerId,
                                     @PathVariable("cardId") Long cardId,
                                     @RequestBody CardAccountParam cardAccountParam,
                                     Errors errors) {

        return ResponseEntity.ok(cardId);
    }

    @DeleteMapping("/{id}/card/{cardId}")
    public ResponseEntity deleteCard(@PathVariable("id") Long customerId,
                                     @PathVariable("cardId") Long cardId,
                                     Errors errors){

        return ResponseEntity.ok(cardId);
    }


    @PutMapping("/{id}/pet")
    public ResponseEntity addPet(@PathVariable("id") Long customerId, @RequestBody List<PetParam> petParam, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        List<PetDto> petDtos = customerService.addPet(petParam, customerId);
        EntityModel customerResource = EntityModel.of(petDtos);
        customerResource.add(linkTo(methodOn(CustomerController.class).addPet(customerId, petParam, errors)).withSelfRel());
        customerResource.add(linkTo(methodOn(CustomerController.class).addPet(customerId, petParam, errors)).withRel("update-pet"));
        customerResource.add(linkTo(methodOn(CustomerController.class).addPet(customerId, petParam, errors)).withRel("delete-pet"));
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-pet-create", "profile"));
        return ResponseEntity.ok(petDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable("id") Long id) {
        CustomerDto customerDto= customerService.getCustomer(id);
        if (customerDto == null) {
            return ResponseEntity.notFound().build();
        }
        WebMvcLinkBuilder selfLink = linkTo(CustomerController.class).slash(customerDto.getId());
        EntityModel customerModel = EntityModel.of(customerDto);
        customerModel.add(selfLink.withRel("self"));
        customerModel.add(selfLink.withRel("update-customer"));
        customerModel.add(selfLink.withRel("delete-customer"));

//        CustomerResource customerResource = CustomerResource.of(customerDto);
        return ResponseEntity.ok(customerModel);
    }
}
