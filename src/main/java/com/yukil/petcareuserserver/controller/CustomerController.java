package com.yukil.petcareuserserver.controller;

import com.yukil.petcareuserserver.config.CustomerResource;
import com.yukil.petcareuserserver.dto.CustomerDto;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/customer", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable("id") Long id) {
        CustomerDto customerDto= customerService.getCustomer(id);
        if (customerDto == null) {
            return ResponseEntity.notFound().build();
        }
        EntityModel customerModel = EntityModel.of(customerDto);
//        CustomerResource customerResource = CustomerResource.of(customerDto);
        return ResponseEntity.ok(customerModel);
    }
}
