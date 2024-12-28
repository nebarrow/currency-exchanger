package com.nebarrow.mapper;

import com.nebarrow.dto.CurrenciesDto;
import com.nebarrow.dto.PostCurrencyDto;
import com.nebarrow.entity.Currency;

import java.util.Optional;

public class CurrencyMapper {

    public static Optional<CurrenciesDto> toDto(Currency currency) {
        if (currency == null) return Optional.empty();
        var currenciesDto = Optional.of(new CurrenciesDto(currency.getId(), currency.getCode(), currency.getFullname(), currency.getSign()));
        return currenciesDto;
    }

    public static Currency toEntity(PostCurrencyDto currencyDto) {
        return Currency.builder()
                .fullname(currencyDto.name())
                .sign(currencyDto.sign())
                .code(currencyDto.code())
                .build();
    }
}
