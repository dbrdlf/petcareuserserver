package com.yukil.petcareuserserver.repository.custom.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yukil.petcareuserserver.common.Querydsl4RepositorySupport;
import com.yukil.petcareuserserver.dto.CustomerDto;
import com.yukil.petcareuserserver.dto.CustomerParam;
import com.yukil.petcareuserserver.dto.CustomerSearchCondition;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.QAddress;
import com.yukil.petcareuserserver.entity.QCardAccount;
import com.yukil.petcareuserserver.entity.QPet;
import com.yukil.petcareuserserver.repository.custom.CustomerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.yukil.petcareuserserver.entity.QAddress.address;
import static com.yukil.petcareuserserver.entity.QCardAccount.cardAccount;
import static com.yukil.petcareuserserver.entity.QCustomer.customer;
import static com.yukil.petcareuserserver.entity.QPet.pet;
import static org.springframework.util.StringUtils.hasText;

//@RequiredArgsConstructor
@Repository
//@RequiredArgsConstructor
public class CustomerRepositoryImpl extends Querydsl4RepositorySupport implements CustomerRepositoryCustom  {

    public CustomerRepositoryImpl() {
        super(Customer.class);
    }
//    private final JPAQueryFactory jpaQueryFactory;
//    private final ModelMapper modelMapper;

    @Override
    public Page<CustomerDto> findByCustomerParam(Pageable pageable, CustomerSearchCondition condition) {

//        JPAQuery<Customer> query = jpaQueryFactory.selectFrom(customer);
//        List<Customer> fetch = query.fetch();
//        List<CustomerDto> customerDtos = fetch.stream().map(c -> modelMapper.map(c, CustomerDto.class)).collect(Collectors.toList());
//        return PageableExecutionUtils.getPage(customerDtos, pageable, query::fetchCount);

        return applyPagination(pageable, query -> query
                .select(Projections.bean(CustomerDto.class,
                        customer.id,
                        customer.email,
                        customer.name,
                        customer.age,
                        customer.address,
                        customer.phoneNumber
//                        customer.cardAccountList,
//                        customer.petList
                )).from(customer)
                .leftJoin(customer.address, address)
                .leftJoin(customer.cardAccountList, cardAccount)
                .leftJoin(customer.petList, pet)
                .where(emailEq(condition.getEmail()),
                        nameEq(condition.getName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
        );
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : customer.age.loe(ageLoe);
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : customer.age.goe(ageGoe);
    }

    private BooleanExpression nameEq(String name) {
        return hasText(name) ? customer.name.eq(name) : null;
    }

    private BooleanExpression emailEq(String email) {
        return hasText(email) ? customer.email.eq(email) : null;
    }
}
