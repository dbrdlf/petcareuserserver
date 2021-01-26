package com.yukil.petcareuserserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukil.petcareuserserver.dto.AddressParam;
import com.yukil.petcareuserserver.dto.CardAccountParam;
import com.yukil.petcareuserserver.dto.CustomerParam;
import com.yukil.petcareuserserver.dto.PetParam;
import com.yukil.petcareuserserver.entity.*;
import com.yukil.petcareuserserver.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.TemplateVariable.requestParameter;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ModelMapper modelMapper;
    @Test
    @DisplayName("사용자 한명을 조회")
    public void getCustomerTest() throws Exception {
        Customer customer = createCustomer();
        //given
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/customer/{id}", customer.getId())
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .accept(MediaTypes.HAL_JSON)
        )
               .andExpect(status().isOk())
               .andDo(print())
               .andDo(document("get-a-customer",
                       links(linkWithRel("self").description("link to self"),
                               linkWithRel("update-customer").description("link to self"),
                               linkWithRel("delete-customer").description("link to self")
                       ),
                       requestHeaders(
                               headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                               headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                       ),
                       pathParameters(
                               parameterWithName("id").description("customer id")
                       ),
                       responseHeaders(
                               headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                       ),
                       responseFields(
                               fieldWithPath("id").description("id of customer"),
                               fieldWithPath("email").description("email of customer"),
                               fieldWithPath("name").description("name of customer"),
                               fieldWithPath("phoneNumber").description("phone number of customer"),
                               fieldWithPath("age").description("age of customer"),
                               fieldWithPath("addressList").description("고객의 주소 리스트. 여러군데를 저장해 놓고 사용 할 수 있기때문에 list로 저장"),
                               fieldWithPath("cardAccountList").description("고객의 신용카드 리스트. 여러카드를 저장해 놓고 사용 할 수 있기때문에 list로 저장"),
                               fieldWithPath("petList").description("고객의 반려동물 리스트. 여러마리를 저장해 놓고 사용 할 수 있기때문에 list로 저장"),
                               fieldWithPath("_links.self.href").description("link to self"),
                               fieldWithPath("_links.update-customer.href").description("link to update customer"),
                               fieldWithPath("_links.delete-customer.href").description("link to delete customer")
                       )
               ))
        ;
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

    @Test
    @DisplayName("사용자 추가")
    public void createCustomerTest() throws Exception {
        CustomerParam customerParam = CustomerParam.builder()
                                         .name("yukil")
                                         .email("dbrdlf61@gmail.com")
                                         .age(37)
                                         .password("1234")
                                         .phoneNumber("010-6504-6334")
                                         .build();
        mockMvc.perform(post("/api/customer/create-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(customerParam))
        ).andDo(print())
        .andExpect(status().isCreated())
        ;
    }

    @Test
    @DisplayName("사용자 주소정보 추가")
    public void addCustomerAddress() throws Exception {
        //given
        Customer customer = createCustomer();
        //when
        AddressParam addressParam = AddressParam.builder()
                                    .city("seoul")
                                    .street("komdalae")
                                    .zipcode(07770)
                                    .build();
        //then
        mockMvc.perform(put("/api/customer/{id}/address", customer.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressParam))
        ).andExpect(status().isOk())
        .andDo(print())
//        .andExpect(jsonPath("customerDto.id"))
//        .andExpect(jsonPath(""))
        ;
    }

    @Test
    @DisplayName("사용자 카드정보 추가")
    public void addCustomerCardAccount() throws Exception {
        //given
        Customer customer = createCustomer();
        //when
        CardAccountParam cardAccountParam = CardAccountParam.builder()
                                                  .cardNumber("1234-1234-1234-1234")
                                                  .vendor(Vendor.KB)
                                                  .ownerName("yukil")
                                                  .customerParam(modelMapper.map(customer, CustomerParam.class))
                                                  .build();
        //then
        mockMvc.perform(put("/api/customer/{id}/card", customer.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardAccountParam))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 반려동물 정보 추가")
    public void addCustomerPet() throws Exception {
        //given
        Customer customer = createCustomer();
        //when
        PetParam petParam = PetParam.builder()
                          .petType(PetType.CAT)
                          .name("moong-chi")
                          .age(2)
                          .customer(customer)
                          .build();
        //then
        mockMvc.perform(put("/api/customer/{id}/pet", customer.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petParam))
        ).andExpect(status().isOk());
    }


}