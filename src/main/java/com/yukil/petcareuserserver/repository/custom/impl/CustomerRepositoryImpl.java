package com.yukil.petcareuserserver.repository.custom.impl;

import com.yukil.petcareuserserver.dto.CustomerDto;
import com.yukil.petcareuserserver.dto.CustomerParam;
import com.yukil.petcareuserserver.repository.custom.CustomerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepositoryCustom  {



    @Override
    public Page<CustomerDto> findByCustomerParam(Pageable pageable, CustomerParam customerParam) {
        return null;
    }
}
