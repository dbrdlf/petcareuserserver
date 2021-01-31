package com.yukil.petcareuserserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukil.petcareuserserver.config.RestDocsConfiguration;
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
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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


//TODO 미완성 REST Docs 완성 시키기
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
        createCardAccount(customer);
        createPet(customer);
        //given
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/customer/{id}", customer.getId())
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .accept(MediaTypes.HAL_JSON)
        )
               .andExpect(status().isOk())
               .andDo(print())
               .andDo(document("get-customer",
                       links(linkWithRel("self").description("self"),
                               linkWithRel("update-customer").description("사용자 수정용 링크"),
                               linkWithRel("delete-customer").description("사용자 삭제용 링크"),
                               linkWithRel("query-customers").description("사용자 목록 조회"),
                               linkWithRel("profile").description("profile")
                       ),
                       requestHeaders(
                               headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                               headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                       ),
                       pathParameters(
                               parameterWithName("id").description("사용자 id")
                       ),
                       responseHeaders(
                               headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                       ),
                       responseFields(
                               fieldWithPath("id").description("사용자 id"),
                               fieldWithPath("email").description("사용자 email"),
                               fieldWithPath("name").description("사용자 이름"),
                               fieldWithPath("phoneNumber").description("사용자 핸드폰 번호"),
                               fieldWithPath("age").description("사용자 나이"),
                               fieldWithPath("birthday").description("사용자 생년월일"),
                               fieldWithPath("address.id").description("사용자 주소 id"),
                               fieldWithPath("address.city").description("사용자 주소 도시"),
                               fieldWithPath("address.street").description("사용자 주소 도로명"),
                               fieldWithPath("address.zipcode").description("사용자 주소 우편번호"),
                               fieldWithPath("address._links.self.href").description("사용자 주소 링크"),
                               fieldWithPath("cardAccounts.[].id").description("카드 id"),
                               fieldWithPath("cardAccounts.[].ownerName").description("카드 소유주"),
                               fieldWithPath("cardAccounts.[].cardNumber").description("카드 번호"),
                               fieldWithPath("cardAccounts.[].vendor").description("카드 사"),
                               fieldWithPath("cardAccounts.[]._links.self.href").description("카드 정보 링크"),
                               fieldWithPath("pets.[].id").description("반려동물 id"),
                               fieldWithPath("pets.[].name").description("반려동물 이름"),
                               fieldWithPath("pets.[].petType").description("반려동물 종류"),
                               fieldWithPath("pets.[].birthday").description("반려동물 생일"),
                               fieldWithPath("pets.[].age").description("반려동물 나이"),
                               fieldWithPath("pets.[]._links.self.href").description("반려동물 정보 링크"),
                               fieldWithPath("_links.self.href").description("self"),
                               fieldWithPath("_links.update-customer.href").description("사용자 수정용 링크"),
                               fieldWithPath("_links.delete-customer.href").description("사용자 삭제용 링크"),
                               fieldWithPath("_links.profile.href").description("profile"),
                               fieldWithPath("_links.query-customers.href").description("사용자 목록 조회")
                       )
               ))
        ;
    }

    @Test
    @DisplayName("사용자 추가")
    public void createCustomerTest() throws Exception {
        CustomerParam customerParam = CustomerParam.builder()
                .name("lee")
                .email("dbrdlf61@gmail.com")
                .birthday(LocalDate.of(1984, 5, 30))
                .password("1234")
                .addressParam(AddressParam.builder().city("ssss").street("2222").zipcode(1111).build())
                .cardAccountList(Arrays.asList(CardAccountParam.builder().cardNumber("1234-1234-1234-1234").ownerName("lee").vendor(Vendor.KB).build()))
                .petList(Arrays.asList(PetParam.builder().petType(PetType.CAT).birthday(LocalDate.of(2018,12,8)).name("moong-chi").build()))
                .phoneNumber("010-6504-6334")
                .build();
        mockMvc.perform(post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(customerParam))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("create-customer",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("update-customer").description("사용자 수정용 링크"),
                                linkWithRel("delete-customer").description("사용자 삭제용 링크"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("email").description("사용자 email"),
                                fieldWithPath("birthday").description("사용자 생년월일"),
                                fieldWithPath("password").description("사용자 패스워드"),
                                fieldWithPath("phoneNumber").description("사용자 핸드폰 번호"),
                                fieldWithPath("addressParam.city").description("사용자 주소 도시"),
                                fieldWithPath("addressParam.street").description("사용자 주소 도로명"),
                                fieldWithPath("addressParam.zipcode").description("사용자 주소 우편번호"),
                                fieldWithPath("cardAccountList.[].ownerName").description("사용자 카드 소유주"),
                                fieldWithPath("cardAccountList.[].cardNumber").description("사용자 카드 번호"),
                                fieldWithPath("cardAccountList.[].vendor").description("사용자 카드사"),
                                fieldWithPath("petList.[].name").description("반려동물 이름"),
                                fieldWithPath("petList.[].petType").description("반려동물 종류"),
                                fieldWithPath("petList.[].birthday").description("반려동물 생일")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 id"),
                                fieldWithPath("email").description("사용자 email"),
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("phoneNumber").description("사용자 핸드폰 번호"),
                                fieldWithPath("age").description("사용자 나이"),
                                fieldWithPath("birthday").description("사용자 생년월일"),
                                fieldWithPath("address.id").description("사용자 주소 id"),
                                fieldWithPath("address.city").description("사용자 주소 도시"),
                                fieldWithPath("address.street").description("사용자 주소 도로명"),
                                fieldWithPath("address.zipcode").description("사용자 주소 우편번호"),
                                fieldWithPath("address._links.self.href").description("사용자 주소 링크"),
                                fieldWithPath("cardAccounts.[].id").description("카드 id"),
                                fieldWithPath("cardAccounts.[].ownerName").description("카드 소유주"),
                                fieldWithPath("cardAccounts.[].cardNumber").description("카드 번호"),
                                fieldWithPath("cardAccounts.[].vendor").description("카드 사"),
                                fieldWithPath("cardAccounts.[]._links.self.href").description("카드 정보 링크"),
                                fieldWithPath("pets.[].id").description("반려동물 id"),
                                fieldWithPath("pets.[].name").description("반려동물 이름"),
                                fieldWithPath("pets.[].petType").description("반려동물 종류"),
                                fieldWithPath("pets.[].birthday").description("반려동물 생일"),
                                fieldWithPath("pets.[].age").description("반려동물 나이"),
                                fieldWithPath("pets.[]._links.self.href").description("반려동물 정보 링크"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.update-customer.href").description("사용자 수정용 링크"),
                                fieldWithPath("_links.delete-customer.href").description("사용자 삭제용 링크"),
                                fieldWithPath("_links.profile.href").description("profile"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회")
                        )
                ))
        ;

    }

    @Test
    @DisplayName("사용자 정보 수정")
    public void updateCustomer() throws Exception {
        //given
        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        createPet(customer);
        //when
        CustomerParam customerParam = CustomerParam.builder()
                .birthday(LocalDate.of(1990, 5, 20))
                .email("ddd@ddd.com")
                .password("2222")
                .name("kim")
                .phoneNumber("010-1234-1234")
                .build();
        //then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/customer/{id}", customer.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerParam))
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-customer",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("delete-customer").description("사용자 정보 삭제"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("사용자 id")

                        ),
                        relaxedRequestFields(
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("phoneNumber").description("사용자 핸드폰번호"),
                                fieldWithPath("birthday").description("사용자 생년월일"),
                                fieldWithPath("password").description("사용자 패스워드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 id"),
                                fieldWithPath("email").description("사용자 email"),
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("phoneNumber").description("사용자 핸드폰 번호"),
                                fieldWithPath("age").description("사용자 나이"),
                                fieldWithPath("birthday").description("사용자 생년월일"),
                                fieldWithPath("address.id").description("사용자 주소 id"),
                                fieldWithPath("address.city").description("사용자 주소 도시"),
                                fieldWithPath("address.street").description("사용자 주소 도로명"),
                                fieldWithPath("address.zipcode").description("사용자 주소 우편번호"),
                                fieldWithPath("address._links.self.href").description("사용자 주소 링크"),
                                fieldWithPath("cardAccounts.[].id").description("카드 id"),
                                fieldWithPath("cardAccounts.[].ownerName").description("카드 소유주"),
                                fieldWithPath("cardAccounts.[].cardNumber").description("카드 번호"),
                                fieldWithPath("cardAccounts.[].vendor").description("카드 사"),
                                fieldWithPath("cardAccounts.[]._links.self.href").description("카드 정보 링크"),
                                fieldWithPath("pets.[].id").description("반려동물 id"),
                                fieldWithPath("pets.[].name").description("반려동물 이름"),
                                fieldWithPath("pets.[].petType").description("반려동물 종류"),
                                fieldWithPath("pets.[].birthday").description("반려동물 생일"),
                                fieldWithPath("pets.[].age").description("반려동물 나이"),
                                fieldWithPath("pets.[]._links.self.href").description("반려동물 정보 링크"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.delete-customer.href").description("사용자 정보 삭제"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("_links.profile.href").description("profile")
                        ))
                )
        ;
    }

    @Test
    @DisplayName("사용자 정보 삭제")
    public void deleteCustomer() throws Exception {
        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        createPet(customer);
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/customer/{id}", customer.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-customer",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("사용자 id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("data").description("삭제된 사용자 id"),
                                fieldWithPath("timestamp").description("api 호출 시간"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("_links.profile.href").description("profile")
                        ))
                )
        ;
    }

    @Test
    @DisplayName("사용자 주소정보 조회")
    public void getAddress() throws Exception{
        Customer customer = createCustomer();
        createCardAccount(customer);
        createPet(customer);
        //given
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/customer/address/{id}", customer.getAddress().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-address",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("update-address").description("주소 수정용 링크"),
                                linkWithRel("delete-address").description("주소 삭제용 링크"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("주소 id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("주소 id"),
                                fieldWithPath("city").description("주소 도시"),
                                fieldWithPath("street").description("주소 도로명"),
                                fieldWithPath("zipcode").description("주소 우편번호"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.update-address.href").description("주소 수정용 링크"),
                                fieldWithPath("_links.delete-address.href").description("주소 삭제용 링크"),
                                fieldWithPath("_links.profile.href").description("profile"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("사용자 주소 수정")
    public void changeCustomerAddress() throws Exception {
        //given
        Customer customer = createCustomer();
//        Address address = createAddress(customer);
        //when
        AddressParam addressParam = AddressParam.builder()
                .city("busan")
                .street("haeundae")
                .zipcode(12345)
                .build();
        //then
        //pathParameters를 사용할 경우 RestDocumentationRequestBuilders사용
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/customer/address/{id}", customer.getAddress().getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressParam))
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-address",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("delete-address").description("사용자 주소 삭제"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("주소 id")
                        ),
                        requestFields(
                                fieldWithPath("city").description("주소 도시"),
                                fieldWithPath("street").description("주소 도로명"),
                                fieldWithPath("zipcode").description("주소 우편번호")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("주소 id"),
                                fieldWithPath("city").description("주소 도시"),
                                fieldWithPath("street").description("주소 도로명"),
                                fieldWithPath("zipcode").description("주소 우편번호"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.delete-address.href").description("사용자 주소 삭제"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("_links.profile.href").description("profile")
                        ))
                )
        ;
    }
    @Test
    @DisplayName("사용자 주소 삭제")
    public void deleteCustomerAddress() throws Exception {
        Customer customer = createCustomer();
//        Address address = createAddress(customer);
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/customer/address/{id}", customer.getAddress().getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-address",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("주소 id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("data").description("삭제된 주소 id"),
                                fieldWithPath("timestamp").description("api 호출 시간"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("_links.profile.href").description("profile")
                        ))
                )
        ;
    }

    @Test
    @DisplayName("사용자 카드정보 조회")
    public void getCard() throws Exception{
        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        createPet(customer);
        //given
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/customer/card/{id}", cardAccount.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-card",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("update-card").description("카드 정보 수정용 링크"),
                                linkWithRel("delete-card").description("카드 정보 삭제용 링크"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("카드 id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("카드 id"),
                                fieldWithPath("vendor").description("카드 사"),
                                fieldWithPath("cardNumber").description("카드 번호"),
                                fieldWithPath("ownerName").description("카드 소유주"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.update-card.href").description("카드 정보 수정용 링크"),
                                fieldWithPath("_links.delete-card.href").description("카드 정보 삭제용 링크"),
                                fieldWithPath("_links.profile.href").description("profile"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회")
                        )
                ))
        ;
    }


    @Test
    @DisplayName("카드 정보 수정")
    public void updateCardAccount() throws Exception {
        //given
        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        //when
        CardAccountParam cardAccountParam = CardAccountParam.builder()
                .cardNumber("4444-5555-6666-7777")
                .ownerName("lee")
                .vendor(Vendor.SHINHAN)
                .build();
        //then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/customer/card/{id}", cardAccount.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardAccountParam))
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-card",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("delete-card").description("카드 정보 삭제"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("카드 id")

                        ),
                        requestFields(
                                fieldWithPath("cardNumber").description("카드 번호"),
                                fieldWithPath("vendor").description("카드사"),
                                fieldWithPath("ownerName").description("카드 소유자 이름")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("카드 id"),
                                fieldWithPath("cardNumber").description("카드 번호"),
                                fieldWithPath("vendor").description("카드 사"),
                                fieldWithPath("ownerName").description("카드 소유자 이름"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.delete-card.href").description("카드 정보 삭제"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("_links.profile.href").description("profile")
                        ))
                )
        ;
    }

    @Test
    @DisplayName("카드 정보 삭제")
    public void deleteCustomerCardAccount() throws Exception {
        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/customer/card/{id}", cardAccount.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-card",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("카드 id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("data").description("삭제된 카드 id"),
                                fieldWithPath("timestamp").description("api 호출 시간"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("_links.profile.href").description("profile")
                        ))
                )
        ;
    }
    @Test
    @DisplayName("카드 정보 삭제 실패(없는 카드 번호)")
    public void deleteCustomerCardAccountNotFound() throws Exception {
        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        mockMvc.perform(delete("/api/customer/card/{id}", customer.getId(), 1000L)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent())
                .andDo(print())
        ;
    }


    @Test
    @DisplayName("반려동물 정보 조회")
    public void getPet() throws Exception{
        Customer customer = createCustomer();
        Pet pet = createPet(customer);
        //given
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/customer/pet/{id}", pet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-pet",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("update-pet").description("반려동물 정보 수정용 링크"),
                                linkWithRel("delete-pet").description("반려동물 정보 삭제용 링크"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("반려동물 id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("반려동물 id"),
                                fieldWithPath("name").description("반려동물 이름"),
                                fieldWithPath("petType").description("반려동물 종류"),
                                fieldWithPath("age").description("반려동물 나이"),
                                fieldWithPath("birthday").description("반려동물 생년월일"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.update-pet.href").description("반려동물 정보 수정용 링크"),
                                fieldWithPath("_links.delete-pet.href").description("반려동물 정보 삭제용 링크"),
                                fieldWithPath("_links.profile.href").description("profile"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회")
                        )
                ))
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
                .birthday(LocalDate.of(2019, 1, 30))
                .name("asdf")
                .petType(PetType.CAT)
                .build();
        //then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/customer/pet/{id}", pet.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petParam))
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-pet",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("delete-pet").description("반려동물 정보 삭제"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("반려동물 id")

                        ),
                        requestFields(
                                fieldWithPath("petType").description("반려동물 종류"),
                                fieldWithPath("name").description("반려동물 이름"),
                                fieldWithPath("birthday").description("반려동물 생년월일")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("반려동물 id"),
                                fieldWithPath("petType").description("반려동물 종류"),
                                fieldWithPath("name").description("반려동물 이름"),
                                fieldWithPath("age").description("반려동물 나이"),
                                fieldWithPath("birthday").description("반려동물 생년월일"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.delete-pet.href").description("반려동물 정보 삭제"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("_links.profile.href").description("profile")
                        ))
                )
        ;
    }

    @Test
    @DisplayName("반려동물 정보 삭제")
    public void deleteCustomerPet() throws Exception {
        Customer customer = createCustomer();
        Pet pet = createPet(customer);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/customer/pet/{id}", pet.getId())
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andDo(print())
                .andDo(document("delete-pet",
                        links(linkWithRel("self").description("self"),
                                linkWithRel("query-customers").description("사용자 목록 조회"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        pathParameters(
                                parameterWithName("id").description("반려동물 id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("data").description("삭제된 반려동물 id"),
                                fieldWithPath("timestamp").description("api 호출 시간"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("_links.profile.href").description("profile")
                        ))
                )

        ;
    }
    @Test
    @DisplayName("반려동물 정보 삭제(없는 반려동물)")
    public void deleteCustomerPetNotFound() throws Exception {
        Customer customer = createCustomer();
        Pet pet = createPet(customer);

        mockMvc.perform(delete("/api/customer/pet/{petId}", 1000L)
                .accept(MediaTypes.HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent())
                .andDo(print())
        ;
    }



    @Test
    @DisplayName("사용자 리스트 조회")
    public void queryCustomers() throws Exception{
        IntStream.range(1, 30).forEach(i -> createCustomer(i));
        mockMvc.perform(get("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .param("page", "0")
                .param("size", "20")
                .param("sort", "name,DESC")
//                .param("name", "yukil_1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("query-customers",
                        links(linkWithRel("self").description("self"),

                                linkWithRel("first").description("첫 페이지"),
                                linkWithRel("next").description("다음 페이지"),
                                linkWithRel("last").description("마지막 페이지"),
                                linkWithRel("profile").description("profile"),
                                linkWithRel("query-customers").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestParameters(
                                parameterWithName("page").description("조회 할 페이지 번호"),
                                parameterWithName("size").description("조회 할 페이지 크기"),
                                parameterWithName("sort").description("정렬")
//                                parameterWithName("name").description("사용자 이름")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.customers.[].id").description("사용자 id"),
                                fieldWithPath("_embedded.customers.[].email").description("사용자 email"),
                                fieldWithPath("_embedded.customers.[].name").description("사용자 이름"),
                                fieldWithPath("_embedded.customers.[].phoneNumber").description("사용자 핸드폰 번호"),
                                fieldWithPath("_embedded.customers.[].age").description("사용자 나이"),
                                fieldWithPath("_embedded.customers.[].birthday").description("사용자 생년월일"),
                                fieldWithPath("_embedded.customers.[].address.id").description("주소 id"),
                                fieldWithPath("_embedded.customers.[].address.city").description("주소 도시"),
                                fieldWithPath("_embedded.customers.[].address.street").description("주소 도로명"),
                                fieldWithPath("_embedded.customers.[].address.zipcode").description("주소 우편번호"),
                                fieldWithPath("_embedded.customers.[].address._links.self.href").description("주소 링크"),
                                fieldWithPath("_embedded.customers.[].cardAccounts.[].id").description("카드 id"),
                                fieldWithPath("_embedded.customers.[].cardAccounts.[].ownerName").description("카드 소유주"),
                                fieldWithPath("_embedded.customers.[].cardAccounts.[].cardNumber").description("카드 카드번호"),
                                fieldWithPath("_embedded.customers.[].cardAccounts.[].vendor").description("카드사"),
                                fieldWithPath("_embedded.customers.[].cardAccounts.[]._links.self.href").description("카드 링크"),
                                fieldWithPath("_embedded.customers.[].pets.[].id").description("반려동물 id"),
                                fieldWithPath("_embedded.customers.[].pets.[].name").description("반려동물 이름"),
                                fieldWithPath("_embedded.customers.[].pets.[].petType").description("반려동물 종류"),
                                fieldWithPath("_embedded.customers.[].pets.[].birthday").description("반려동물 생일"),
                                fieldWithPath("_embedded.customers.[].pets.[].age").description("반려동물 나이"),
                                fieldWithPath("_embedded.customers.[].pets.[]._links.self.href").description("반려동물 링크"),
                                fieldWithPath("_embedded.customers.[]._links.self.href").description("self"),
                                fieldWithPath("_links.first.href").description("첫페이지"),
                                fieldWithPath("_links.next.href").description("다음페이지"),
                                fieldWithPath("_links.last.href").description("마지막페이지"),
                                fieldWithPath("_links.self.href").description("self"),
                                fieldWithPath("_links.profile.href").description("profile"),
                                fieldWithPath("_links.query-customers.href").description("사용자 목록 조회"),
                                fieldWithPath("page.size").description("페이지 별 목록 수"),
                                fieldWithPath("page.totalElements").description("전체 목록 수"),
                                fieldWithPath("page.number").description("현재 페이지 번호"),
                                fieldWithPath("page.totalPages").description("총 페이지 수")
                        )
                ))

        ;

    }

    private Customer createCustomer(int i) {
        Address address = Address.builder()
//                .customer(customer)
                .city("seoul")
                .street("komdalae")
                .zipcode(07770)
                .build();
        Address savedAddress = addressRepository.save(address);
        Customer customer = Customer.builder()
                .name("yukil_" + i)
                .email("dbrdlf61@gmail.com")
                .password("pass")
                .address(address)
                .phoneNumber("010-1234-1234")
                .birthday(LocalDate.of(1984,5,30))
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        createCardAccount(savedCustomer);
        createPet(savedCustomer);
        return savedCustomer;
    }

    private Customer createCustomer() {
        Address address = Address.builder()
//                .customer(customer)
                .city("seoul")
                .street("komdalae")
                .zipcode(07770)
                .build();
        Address savedAddress = addressRepository.save(address);
        Customer customer = Customer.builder()
                .name("lee")
                .email("dbrdlf61@gmail.com")
                .password("pass")
                .phoneNumber("010-1234-1234")
                .address(savedAddress)
//                .address(Address.builder().city("seoul").street("komdalae").zipcode(07770).build())
                .birthday(LocalDate.of(1984,5,30))
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
                .ownerName("lee")
                .cardNumber("1234-1234-1234-1234")
                .build();
        return cardAccountRepository.save(cardAccount);
    }

    private Pet createPet(Customer customer) {
        Pet pet = Pet.builder()
                .birthday(LocalDate.of(2018,12,8))
                .customer(customer)
                .petType(PetType.CAT)
                .name("moong-chi")
                .build();
        return petRepository.save(pet);
    }
}