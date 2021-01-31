package com.yukil.petcareuserserver.hateoas;

import com.yukil.petcareuserserver.controller.CustomerController;
import com.yukil.petcareuserserver.dto.CardAccountDto;
import com.yukil.petcareuserserver.entity.CardAccount;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CardAccountDtoAssembler extends RepresentationModelAssemblerSupport<CardAccount, CardAccountDto> {
    public CardAccountDtoAssembler() {
        super(CustomerController.class, CardAccountDto.class);
    }

    @Override
    public CardAccountDto toModel(CardAccount cardAccount) {
        CardAccountDto cardAccountDto = CardAccountDto.builder()
                .id(cardAccount.getId())
                .vendor(cardAccount.getVendor())
                .cardNumber(cardAccount.getCardNumber())
                .ownerName(cardAccount.getOwnerName())
                .build();
        cardAccountDto.add(linkTo(methodOn(CustomerController.class).getCard(cardAccount.getId())).withSelfRel());
        cardAccountDto.add(linkTo(CustomerController.class).withRel("query-customers"));
        return cardAccountDto;
    }

    @Override
    public CollectionModel<CardAccountDto> toCollectionModel(Iterable<? extends CardAccount> cardAccounts) {
        CollectionModel<CardAccountDto> cardAccountDtos = super.toCollectionModel(cardAccounts);
        cardAccountDtos.add(linkTo(methodOn(CustomerController.class).getAllCard()).withSelfRel());
        return cardAccountDtos;
    }
}
