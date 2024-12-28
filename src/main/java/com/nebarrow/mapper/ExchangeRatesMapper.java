package com.nebarrow.mapper;

import com.nebarrow.dto.ExchangeRatesDto;
import com.nebarrow.dto.PostExchangeRatesDto;
import com.nebarrow.entity.ExchangeRate;

import java.util.Optional;

public class ExchangeRatesMapper {

    public static Optional<ExchangeRatesDto> toDto(ExchangeRate exchangeRate) {
        if (exchangeRate == null) return Optional.empty();
        var exchangeRatesDto = Optional.of(new ExchangeRatesDto(exchangeRate.getId(), exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate()));
        return exchangeRatesDto;
    }

    public static ExchangeRate toEntity(PostExchangeRatesDto exchangeRatesDto) {
        var exchangeRate = ExchangeRate.builder()
                .rate(exchangeRatesDto.rate())
                .baseCurrency(exchangeRatesDto.baseCurrency())
                .targetCurrency(exchangeRatesDto.targetCurrency())
                .build();
        return exchangeRate;
    }
}
