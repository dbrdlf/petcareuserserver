package com.yukil.petcareuserserver.repository.custom;

import com.yukil.petcareuserserver.dto.CustomerDto;
import com.yukil.petcareuserserver.dto.CustomerParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {
    Page<CustomerDto> findByCustomerParam(Pageable pageable, CustomerParam customerParam);
}
