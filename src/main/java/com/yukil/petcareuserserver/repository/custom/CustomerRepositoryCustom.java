package com.yukil.petcareuserserver.repository.custom;

import com.yukil.petcareuserserver.dto.CustomerDto;
import com.yukil.petcareuserserver.dto.CustomerParam;
import com.yukil.petcareuserserver.dto.CustomerSearchCondition;
import com.yukil.petcareuserserver.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {
    Page<Customer> findByCustomerParam(Pageable pageable, CustomerSearchCondition customerSearchCondition);
    void testQuery();
}
