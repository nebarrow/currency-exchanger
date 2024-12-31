package com.nebarrow.mapper;

import com.nebarrow.dto.request.CurrencyRequest;
import com.nebarrow.dto.response.CurrenciesResponse;
import com.nebarrow.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface ICurrencyMapper {

    ICurrencyMapper INSTANCE = Mappers.getMapper(ICurrencyMapper.class);

    CurrenciesResponse toDto(Currency currency);

    Currency toEntity(CurrencyRequest request);
}
