package com.yukil.petcareuserserver.hateoas;

import com.yukil.petcareuserserver.controller.CustomerController;
import com.yukil.petcareuserserver.dto.CardAccountDto;
import com.yukil.petcareuserserver.dto.PetDto;
import com.yukil.petcareuserserver.entity.CardAccount;
import com.yukil.petcareuserserver.entity.Pet;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PetDtoAssembler extends RepresentationModelAssemblerSupport<Pet, PetDto> {
    public PetDtoAssembler() {
        super(CustomerController.class, PetDto.class);
    }

    @Override
    public PetDto toModel(Pet pet) {
        PetDto petDto = PetDto.builder()
                .birthday(pet.getBirthday())
                .id(pet.getId())
                .name(pet.getName())
                .petType(pet.getPetType())
                .age(getAge(pet.getBirthday()))
                .build();
        petDto.add(linkTo(methodOn(CustomerController.class).getPet(pet.getId())).withSelfRel());
        petDto.add(linkTo(CustomerController.class).withRel("query-customers"));
        return petDto;
    }

    private Integer getAge(LocalDate birthday) {
        if (birthday == null) {
            return 0;
        }
        Period period = birthday.until(LocalDate.now());
        return period.getYears();
    }

    @Override
    public CollectionModel<PetDto> toCollectionModel(Iterable<? extends Pet> pets) {
        CollectionModel<PetDto> petDtos = super.toCollectionModel(pets);
        petDtos.add(linkTo(methodOn(CustomerController.class).getAllPet()).withSelfRel());
        return petDtos;
    }
}
