package com.nebarrow.mapper;

import com.nebarrow.dto.ExchangeDto;
import com.nebarrow.dto.ExchangeRatesDto;
import com.nebarrow.entity.ExchangeRate;

import java.util.Optional;

public class ExchangeMapper {

    public static Optional<ExchangeDto> toDto(ExchangeRate exchangeRate) {
        if (exchangeRate == null) return Optional.empty();
        return Optional.of(
                ExchangeDto.builder()
                        .baseCurrency(exchangeRate.getBaseCurrency())
                        .targetCurrency(exchangeRate.getTargetCurrency())
                        .rate(exchangeRate.getRate())
                        .build());
    }
}
