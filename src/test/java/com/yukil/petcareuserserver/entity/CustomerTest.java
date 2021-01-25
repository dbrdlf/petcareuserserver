package com.yukil.petcareuserserver.entity;

import com.yukil.petcareuserserver.repository.AddressRepository;
import com.yukil.petcareuserserver.repository.CardAccountRepository;
import com.yukil.petcareuserserver.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomerTest {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CardAccountRepository cardAccountRepository;
    @Autowired
    AddressRepository addressRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
        cardAccountRepository.deleteAll();
        addressRepository.deleteAll();
    }
    @Test
    @DisplayName("사용자 생성 테스트")
    public void customerSave() throws Exception {

        Customer customer = createCustomer();
        CardAccount cardAccount = createCardAccount(customer);
        Address address = createAddress(customer);
        assertThat(customer.getName()).isEqualTo("yukil");
        assertThat(customer.getAddressList().get(0).getCity()).isEqualTo("seoul");
        assertThat(cardAccount.getVendor()).isEqualTo(Vendor.KB);
        assertThat(address.getCity()).isEqualTo("seoul");
        assertThat(address.getZipcode()).isEqualTo(07770);

    }

    private Address createAddress(Customer customer) {
        Address address = Address.builder()
                                 .city("seoul")
                                 .street("komdalae")
                                 .zipcode(07770)
                                 .customer(customer)
                                 .build();
        address.saveCustomer(customer);
        return addressRepository.save(address);
    }

    private CardAccount createCardAccount(Customer customer) {
        CardAccount cardAccount = CardAccount.builder()
                                             .ownerName("yukil")
                                             .cardNumber("123412341234")
                                             .vendor(Vendor.KB)
                                             .customer(customer)
                                             .build();
        cardAccount.saveCustomer(customer);
        return cardAccountRepository.save(cardAccount);
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
    @DisplayName("사용자 정보 변경 테스트")
    public void changeCustomerInfo() throws Exception {
        //given
        Customer customer = createCustomer();
        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        if(!optionalCustomer.isPresent()){
            System.out.println("notfound");
        }
        Customer findCustomer = optionalCustomer.get();
        findCustomer.setEmail("dbrdlf61@nate.com");
        findCustomer.setName("yyyy");
        //when
        customerRepository.save(findCustomer);

        //then
        assertThat(findCustomer.getName()).isEqualTo("yyyy");
        assertThat(findCustomer.getEmail()).isEqualTo("dbrdlf61@nate.com");
    }
    
    @Test
    public void changePetInfo() throws Exception {
        //given
        
        //when
         
        //then
    }

}