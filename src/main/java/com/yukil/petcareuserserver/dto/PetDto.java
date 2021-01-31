package com.yukil.petcareuserserver.dto;

import com.yukil.petcareuserserver.entity.Customer;
import com.yukil.petcareuserserver.entity.PetType;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.time.Period;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class PetDto extends RepresentationModel<PetDto> {
    private Long id;
    private String name;
    private PetType petType;
    private LocalDate birthday;
    private Integer age;

    public Integer getAge() {
        if (birthday == null) {
            return 0;
        }
        Period period = birthday.until(LocalDate.now());
        return period.getYears();
    }
}
