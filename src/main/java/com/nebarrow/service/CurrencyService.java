package com.nebarrow.service;

import com.nebarrow.dao.CurrencyDao;
import com.nebarrow.dto.response.CurrenciesResponse;
import com.nebarrow.dto.request.CurrencyRequest;
import com.nebarrow.exception.ElementAlreadyExistsException;
import com.nebarrow.exception.ElementNotFoundException;
import com.nebarrow.mapper.ICurrencyMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CurrencyService {

    private final ICurrencyMapper MAPPER = ICurrencyMapper.INSTANCE;
    private final CurrencyDao currencyDao;

    public List<CurrenciesResponse> getAllCurrencies() {
        return currencyDao.findAll().stream()
                .map(MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public CurrenciesResponse getCurrency(CurrencyRequest currencyRequest) {
        return currencyDao.findByName(currencyRequest.name()).stream()
                .map(MAPPER::toDto)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException("Currency not found"));
    }

    public CurrenciesResponse create(CurrencyRequest currencyDto) {
        var currency = MAPPER.toEntity(currencyDto);
        if (currencyDao.findByName(currency.getCode()).isPresent()) {
            throw new ElementAlreadyExistsException("Currency with this code already exists");
        }
        return MAPPER.toDto(currencyDao.create(currency));
    }
}
