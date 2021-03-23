package com.yukil.petcareuserserver.repository.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.yukil.petcareuserserver.common.Querydsl4RepositorySupport;
import com.yukil.petcareuserserver.dto.CustomerSearchCondition;
import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.repository.custom.CustomerRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Period;

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
    public Page<Customer> findByCustomerParam(Pageable pageable, CustomerSearchCondition condition) {

//        JPAQuery<Customer> query = jpaQueryFactory.selectFrom(customer);
//        List<Customer> fetch = query.fetch();
//        List<CustomerDto> customerDtos = fetch.stream().map(c -> modelMapper.map(c, CustomerDto.class)).collect(Collectors.toList());
//        return PageableExecutionUtils.getPage(customerDtos, pageable, query::fetchCount);

        return applyPagination(pageable, query -> query
                .selectFrom(customer)

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

    @Override
    public void testQuery() {
        BooleanBuilder builder = new BooleanBuilder();
        select(customer.id).from(customer).where(Expressions.booleanTemplate("{0}  {1} > 0", customer.id, "2")).fetch();
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : customer.birthday.loe(getBirthday(ageLoe));
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : customer.birthday.goe(getBirthday(ageGoe));
    }

    private BooleanExpression nameEq(String name) {
        return hasText(name) ? customer.name.eq(name) : null;
    }

    private BooleanExpression emailEq(String email) {
        return hasText(email) ? customer.email.eq(email) : null;
    }

    private Integer getAge(LocalDate birthday) {
        if (birthday == null) {
            return 0;
        }
        Period period = birthday.until(LocalDate.now());
        return period.getYears();
    }

    private LocalDate getBirthday(Integer age) {
        return LocalDate.of(LocalDate.now().getYear() - age, LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
    }



}
