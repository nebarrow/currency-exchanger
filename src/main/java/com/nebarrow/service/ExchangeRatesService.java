package com.nebarrow.service;

import com.nebarrow.dao.ExchangeRatesDao;
import com.nebarrow.dto.ExchangeRatesDto;
import com.nebarrow.dto.PostExchangeRatesDto;
import com.nebarrow.exception.ElementAlreadyExistsException;
import com.nebarrow.exception.ElementNotFoundException;
import com.nebarrow.mapper.ExchangeRatesMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExchangeRatesService {

    private final ExchangeRatesDao exchangeRatesDao;

    public List<ExchangeRatesDto> getAll() {
        return exchangeRatesDao.findAll().stream()
                .map(ExchangeRatesMapper::toDto)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public ExchangeRatesDto getByName(String name) {
        return exchangeRatesDao.findByName(name).stream()
                .map(ExchangeRatesMapper::toDto)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException("This exchange rate not found"));
    }

    public ExchangeRatesDto create(PostExchangeRatesDto exchangeRatesDto) {
        var exchangeRate = ExchangeRatesMapper.toEntity(exchangeRatesDto);

        if (isExchangeRateExistsByConcatenate(exchangeRatesDto)) {
            throw new ElementAlreadyExistsException("This exchange rate already exists");
        }
        return ExchangeRatesMapper.toDto(exchangeRatesDao.create(exchangeRate)).get();
    }

    public ExchangeRatesDto update(PostExchangeRatesDto exchangeRatesDto) {
        var exchangeRate = ExchangeRatesMapper.toEntity(exchangeRatesDto);
        if (!isExchangeRateExistsByConcatenate(exchangeRatesDto)) {
            throw new ElementNotFoundException("This exchange rate not found");
        }
        return exchangeRatesDao.update(exchangeRate).stream()
                .flatMap(rate -> ExchangeRatesMapper.toDto(rate).stream())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't update this exchange rate"));
    }

    private boolean isExchangeRateExistsByConcatenate(PostExchangeRatesDto exchangeRatesDto) {
        var concatenatedCodes = exchangeRatesDto.baseCurrency().getCode() + exchangeRatesDto.targetCurrency().getCode();
        return exchangeRatesDao.findByName(concatenatedCodes).isPresent();
    }
}
