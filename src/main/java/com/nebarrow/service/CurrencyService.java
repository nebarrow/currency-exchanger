package com.nebarrow.service;

import com.nebarrow.dao.CurrencyDao;
import com.nebarrow.dto.CurrenciesDto;
import com.nebarrow.dto.PostCurrencyDto;
import com.nebarrow.exception.ElementAlreadyExistsException;
import com.nebarrow.exception.ElementNotFoundException;
import com.nebarrow.mapper.CurrencyMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyDao currencyDao;

    public List<CurrenciesDto> getAllCurrencies() {
        return currencyDao.findAll().stream()
                .map(CurrencyMapper::toDto)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public CurrenciesDto getCurrency(String name) {
        return currencyDao.findByName(name).stream()
                .map(CurrencyMapper::toDto)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException("Currency not found"));
    }

    public CurrenciesDto create(PostCurrencyDto currencyDto) {
        var currency = CurrencyMapper.toEntity(currencyDto);
        if (currencyDao.findByName(currency.getCode()).isPresent()) {
            throw new ElementAlreadyExistsException("Currency with this code already exists");
        }
        return CurrencyMapper.toDto(currencyDao.create(currency)).get();
    }
}
