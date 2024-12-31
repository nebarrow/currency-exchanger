package com.nebarrow.mapper;

import com.nebarrow.dto.request.ExchangeRequest;
import com.nebarrow.dto.response.ExchangeResponse;
import com.nebarrow.entity.Currency;
import com.nebarrow.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IExchangeMapper {

    IExchangeMapper INSTANCE = Mappers.getMapper(IExchangeMapper.class);

    ExchangeResponse toDto(ExchangeRate exchangeRate);

    @Mapping(target = "baseCurrency", expression = "java(mapCurrency(exchangeRequest.baseCode()))")
    @Mapping(target = "targetCurrency", expression = "java(mapCurrency(exchangeRequest.targetCode()))")
    ExchangeRate toEntity(ExchangeRequest exchangeRequest);

    default Currency mapCurrency(String code) {
        return Currency.builder().code(code).build();
    }
}
