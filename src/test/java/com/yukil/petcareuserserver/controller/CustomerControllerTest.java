package com.yukil.petcareuserserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukil.petcareuserserver.dto.AddressParam;
import com.yukil.petcareuserserver.dto.CardAccountParam;
import com.yukil.petcareuserserver.dto.CustomerParam;
import com.yukil.petcareuserserver.dto.PetParam;
import com.yukil.petcareuserserver.entity.*;
import com.yukil.petcareuserserver.repository.AddressRepository;
import com.yukil.petcareuserserver.repository.CardAccountRepository;
import com.yukil.petcareuserserver.repository.CustomerRepository;
import com.yukil.petcareuserserver.repository.PetRepository;
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
import java.util.List;

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
    AddressRepository addressRepository;
    @Autowired
    CardAccountRepository cardAccountRepository;
    @Autowired
    PetRepository petRepository;
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
                .andDo(document("create-customer",
                        links(linkWithRel("self").description("link to self"),
                                linkWithRel("update-customer").description("link to udpate-customer"),
                                linkWithRel("delete-customer").description("link to delete-customer"),
                                linkWithRel("query-customers").description("link to query-customers"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("customer name"),
                                fieldWithPath("email").description("customer email"),
                                fieldWithPath("age").description("customer age"),
                                fieldWithPath("password").description("customer password"),
                                fieldWithPath("phoneNumber").description("customer phoneNumber")
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
                                fieldWithPath("address").description("고객의 주소"),
                                fieldWithPath("cardAccountList").description("고객의 신용카드 리스트. 여러카드를 저장해 놓고 사용 할 수 있기때문에 list로 저장"),
                                fieldWithPath("petList").description("고객의 반려동물 리스트. 여러마리를 저장해 놓고 사용 할 수 있기때문에 list로 저장"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.update-customer.href").description("link to update customer"),
                                fieldWithPath("_links.delete-customer.href").description("link to delete customer")
                        )
                ))
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
    @DisplayName("사용자 주소 수정")
    public void changeCustomerAddress() throws Exception {
        //given
        Customer customer = createCustomer();
        Address address = createAddress(customer);
        //when
        AddressParam addressParam = AddressParam.builder()
                .city("busan")
                .street("haeundae")
                .zipcode(12345)
                .build();
        //then
        mockMvc.perform(put("/api/customer/{id}/address/{addressId}", customer.getId(), address.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressParam))
        ).andExpect(status().isOk())
                .andDo(print())
                ;
    }
    @Test
    @DisplayName("사용자 주소 삭제")
    public void deleteCustomerAddress() throws Exception {
        Customer customer = createCustomer();
        Address address = createAddress(customer);
        mockMvc.perform(delete("/api/customer/{id}/address/{addressId}", customer.getId(), address.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
//                .andExpect(document("address-delete",
//                        links(linkWithRel("self").description("link to self"),
//                                linkWithRel("self").description("link to self")
//                                )
//                        ))
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
                                                  .build();
        CardAccountParam cardAccountParam2 = CardAccountParam.builder()
                                                  .cardNumber("4321-4321-4321-4321")
                                                  .vendor(Vendor.SHINHAN)
                                                  .ownerName("yukil")
                                                  .build();

        List<CardAccountParam> cardList = Arrays.asList(cardAccountParam, cardAccountParam2);
        //then
        mockMvc.perform(put("/api/customer/{id}/card", customer.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardList))
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카드 정보 수정")
    public void changeCustomerCardAccount() throws Exception {
        //given
        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        //when
        CardAccountParam cardAccountParam = CardAccountParam.builder()
                .cardNumber("4444-5555-6666-7777")
                .ownerName("yukil")
                .vendor(Vendor.SHINHAN)
                .build();
        //then
        mockMvc.perform(put("/api/customer/{id}/card/{cardId}", customer.getId(), cardAccount.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardAccountParam))
        ).andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("카드 정보 삭제")
    public void deleteCustomerCardAccount() throws Exception {
        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        mockMvc.perform(delete("/api/customer/{id}/card/{cardId}", customer.getId(), cardAccount.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
//                .andExpect(document("address-delete",
//                        links(linkWithRel("self").description("link to self"),
//                                linkWithRel("self").description("link to self")
//                                )
//                        ))
        ;
    }

    @Test
    @DisplayName("반려동물 정보 수정")
    public void changeCustomerPet() throws Exception {
        //given
        Customer customer = createCustomer();
        Pet pet = createPet(customer);
        //when
        PetParam petParam = PetParam.builder()
                .age(3)
                .name("asdf")
                .petType(PetType.CAT)
                .build();
        //then
        mockMvc.perform(put("/api/customer/{id}/pet/{petId}", customer.getId(), pet.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petParam))
        ).andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("반려동물 정보 삭제")
    public void deleteCustomerPet() throws Exception {
        Customer customer = createCustomer();
        Pet pet = createPet(customer);

        mockMvc.perform(delete("/api/customer/{id}/pet/{petId}", customer.getId(), pet.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
//                .andExpect(document("address-delete",
//                        links(linkWithRel("self").description("link to self"),
//                                linkWithRel("self").description("link to self")
//                                )
//                        ))
        ;
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
                          .build();
        PetParam petParam2 = PetParam.builder()
                          .petType(PetType.DOG)
                          .name("daeng-daeng")
                          .age(1)
                          .build();
        List<PetParam> petParamList = Arrays.asList(petParam, petParam2);
        //then
        mockMvc.perform(put("/api/customer/{id}/pet", customer.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petParamList))
        )
                .andDo(print())
                .andExpect(status().isOk());
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

    private Address createAddress(Customer customer) {
        Address address = Address.builder()
                .customer(customer)
                .city("seoul")
                .street("komdalae")
                .zipcode(07770)
                .build();
        return addressRepository.save(address);
    }

    private CardAccount createCardAccount(Customer customer) {
        CardAccount cardAccount = CardAccount.builder()
                .customer(customer)
                .vendor(Vendor.KB)
                .ownerName("yukil")
                .cardNumber("1234-1234-1234-1234")
                .build();
        return cardAccountRepository.save(cardAccount);
    }

    private Pet createPet(Customer customer) {
        Pet pet = Pet.builder()
                .age(2)
                .customer(customer)
                .petType(PetType.CAT)
                .name("moong-chi")
                .build();
        return petRepository.save(pet);
    }
}