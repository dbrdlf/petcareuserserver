package com.yukil.petcareuserserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchCondition {
    private String email;
    private String name;
    private String phoneNumber;
    private String password;
    private Integer ageGoe;
    private Integer ageLoe;
}
