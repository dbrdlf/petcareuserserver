package com.yukil.petcareuserserver.controller;

import com.yukil.petcareuserserver.dto.*;
import com.yukil.petcareuserserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
        EntityModel customerResource = EntityModel.of(customerDto);
        customerResource.add(selfLinkBuilder.withRel("update-customer"));
        customerResource.add(selfLinkBuilder.withRel("delete-customer"));
        customerResource.add(linkTo(CustomerController.class).withRel("query-customers"));
        customerResource.add(Link.of("docs/index.html#resources-customer-create", "profile"));
        return ResponseEntity.created(uri).body(customerResource);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity addAddress(@RequestBody AddressParam addressParam, @PathVariable("id") Long customerId, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(customerService.addAddress(addressParam, customerId));
    }
    @PutMapping("/{id}/card")
    public ResponseEntity addCard(@RequestBody CardAccountParam cardAccountParam, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(customerService.addCard(cardAccountParam));
    }

    @PutMapping("/{id}/pet")
    public ResponseEntity addPet(@RequestBody PetParam petParam, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(customerService.addPet(petParam));
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
