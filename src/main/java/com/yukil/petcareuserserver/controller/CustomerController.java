package com.yukil.petcareuserserver.controller;

import com.yukil.petcareuserserver.common.ResponseMessage;
import com.yukil.petcareuserserver.config.CustomerResource;
import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
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
                    entityModel.add(linkTo(methodOn(CustomerController.class).deleteCard(customerId, c.getId())).withRel("delete-card"));
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
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        CardAccountDto cardAccountDto = customerService.changeCardAccount(cardId, cardAccountParam);

        ResponseMessage<EntityModel> message = new ResponseMessage(cardAccountDto);
        EntityModel customerResource = EntityModel.of(message);
        customerResource.add(linkTo(methodOn(CustomerController.class).addCard(customerId, Arrays.asList(cardAccountParam), errors)).withRel("create-card"));
        customerResource.add(linkTo(methodOn(CustomerController.class).changeCard(customerId, cardId, cardAccountParam, errors)).withSelfRel());
        customerResource.add(linkTo(methodOn(CustomerController.class).deleteCard(customerId, cardId)).withRel("delete-card"));
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-update-card", "profile"));
        return ResponseEntity.ok(customerResource);
    }

    @DeleteMapping("/{id}/card/{cardId}")
    public ResponseEntity deleteCard(@PathVariable("id") Long customerId,
                                     @PathVariable("cardId") Long cardId
                                     ){
        Long deletedCardId = customerService.deleteCard(cardId);
        if (deletedCardId == null) {
            return ResponseEntity.noContent().build();
        }
        ResponseMessage<Long> responseMessage = new ResponseMessage<>(deletedCardId);
        EntityModel customerResource = EntityModel.of(responseMessage);
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-delete-address", "profile"));
        return ResponseEntity.ok(customerResource);
    }


    @PutMapping("/{id}/pet")
    public ResponseEntity addPet(@PathVariable("id") Long customerId, @RequestBody List<PetParam> petParam, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        List<PetDto> petDtos = customerService.addPet(petParam, customerId);
        ResponseMessage message = new ResponseMessage(petDtos);
        EntityModel customerResource = EntityModel.of(message);
        customerResource.add(linkTo(methodOn(CustomerController.class).addPet(customerId, petParam, errors)).withSelfRel());
        customerResource.add(linkTo(methodOn(CustomerController.class).addPet(customerId, petParam, errors)).withRel("update-pet"));
        customerResource.add(linkTo(methodOn(CustomerController.class).addPet(customerId, petParam, errors)).withRel("delete-pet"));
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-pet-create", "profile"));
        return ResponseEntity.ok(customerResource);
    }

    @PutMapping("/{id}/pet/{petId}")
    public ResponseEntity changePet(@PathVariable("id") Long customerId,
                                     @PathVariable("petId") Long petId,
                                     @RequestBody PetParam petParam,
                                     Errors errors) {

        PetDto petDto = customerService.changePet(petId, petParam);

        ResponseMessage<EntityModel> message = new ResponseMessage(petDto);
        EntityModel customerResource = EntityModel.of(message);
        customerResource.add(linkTo(methodOn(CustomerController.class).addPet(customerId, Arrays.asList(petParam), errors)).withRel("create-card"));
        customerResource.add(linkTo(methodOn(CustomerController.class).changePet(customerId, petId, petParam, errors)).withSelfRel());
        customerResource.add(linkTo(methodOn(CustomerController.class).deletePet(customerId, petId)).withRel("delete-card"));
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-update-card", "profile"));
        return ResponseEntity.ok(customerResource);
    }

    @DeleteMapping("/{id}/pet/{petId}")
    public ResponseEntity deletePet(@PathVariable("id") Long customerId,
                                     @PathVariable("petId") Long petId
    ){
        Long deletedPetId = customerService.deletePet(petId);
        if (deletedPetId == null) {
            return ResponseEntity.noContent().build();
        }
        ResponseMessage<Long> responseMessage = new ResponseMessage<>(deletedPetId);
        EntityModel customerResource = EntityModel.of(responseMessage);
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-delete-pet", "profile"));
        return ResponseEntity.ok(customerResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable("id") Long id) {
        CustomerDto customerDto = customerService.getCustomer(id);
        if (customerDto == null) {
            return ResponseEntity.notFound().build();
        }
        WebMvcLinkBuilder selfLink = linkTo(CustomerController.class).slash(customerDto.getId());
        ResponseMessage message = new ResponseMessage(customerDto);

        EntityModel customerResource = EntityModel.of(message);
        customerResource.add(selfLink.withRel("self"));
        customerResource.add(selfLink.withRel("update-customer"));
        customerResource.add(selfLink.withRel("delete-customer"));

        return ResponseEntity.ok(customerResource);
    }

    @GetMapping
    public ResponseEntity queryCustomers(Pageable pageable, CustomerSearchCondition condition) {
        Page<CustomerDto> customerDtoPage = customerService.queryCustomers(pageable, condition);
        return ResponseEntity.ok(customerDtoPage);
    }
}
