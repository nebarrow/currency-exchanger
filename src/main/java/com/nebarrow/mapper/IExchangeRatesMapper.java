package com.nebarrow.mapper;

import com.nebarrow.dto.request.ExchangeRateRequest;
import com.nebarrow.dto.response.ExchangeRatesResponse;
import com.nebarrow.entity.Currency;
import com.nebarrow.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IExchangeRatesMapper {

    IExchangeRatesMapper INSTANCE = Mappers.getMapper(IExchangeRatesMapper.class);

    ExchangeRatesResponse toDto(ExchangeRate exchangeRate);

    ExchangeRate toEntity(ExchangeRateRequest exchangeRateRequest);

    default Currency map(String value) {
        return Currency.builder()
                .code(value)
                .build();
    }
}
