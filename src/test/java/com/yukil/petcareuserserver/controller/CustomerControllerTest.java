package com.yukil.petcareuserserver.controller;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    CustomerRepository customerRepository;
    @Test
    @DisplayName("사용자 한명을 조회")
    public void getCustomerTest() throws Exception {
        Customer customer = createCustomer();
        //given
        mockMvc.perform(get("/api/customer/{id}", customer.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)

        )

               .andExpect(status().isOk())
               .andDo(print())
               .andDo(document("get-a-customer",
                       links(linkWithRel("self").description("link to self"))
                       ))
        ;
        //when

        //then
    }

    private Customer createCustomer() {
        Customer customer = Customer.builder()
                                    .name("yukil")
                                    .email("dbrdlf61@gmail.com")
                                    .password("pass")
                                    .phoneNumber("010-1234-1234")
                                    .build();
        return customerRepository.save(customer);
    }


}