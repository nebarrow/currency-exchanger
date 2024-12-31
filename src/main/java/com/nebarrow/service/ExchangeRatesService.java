package com.nebarrow.service;

import com.nebarrow.dao.ExchangeRatesDao;
import com.nebarrow.dto.response.ExchangeRatesResponse;
import com.nebarrow.dto.request.ExchangeRateRequest;
import com.nebarrow.exception.ElementAlreadyExistsException;
import com.nebarrow.exception.ElementNotFoundException;
import com.nebarrow.mapper.IExchangeRatesMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExchangeRatesService {

    private final IExchangeRatesMapper MAPPER = IExchangeRatesMapper.INSTANCE;
    private final ExchangeRatesDao exchangeRatesDao;

    public List<ExchangeRatesResponse> getAll() {
        return exchangeRatesDao.findAll().stream()
                .map(MAPPER::toDto)
                .collect(Collectors.toList());
    }

    public ExchangeRatesResponse getByName(String name) {
        return exchangeRatesDao.findByName(name).stream()
                .map(MAPPER::toDto)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException("This exchange rate not found"));
    }

    public ExchangeRatesResponse create(ExchangeRateRequest exchangeRatesDto) {
        var exchangeRate = MAPPER.toEntity(exchangeRatesDto);

        if (isExchangeRateExistsByConcatenate(exchangeRatesDto)) {
            throw new ElementAlreadyExistsException("This exchange rate already exists");
        }
        return MAPPER.toDto(exchangeRatesDao.create(exchangeRate));
    }

    public ExchangeRatesResponse update(ExchangeRateRequest exchangeRates) {
        var exchangeRate = MAPPER.toEntity(exchangeRates);
        if (!isExchangeRateExistsByConcatenate(exchangeRates)) {
            throw new ElementNotFoundException("This exchange rate not found");
        }
        return exchangeRatesDao.update(exchangeRate).stream()
                .map(MAPPER::toDto)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't update this exchange rate"));
    }

    private boolean isExchangeRateExistsByConcatenate(ExchangeRateRequest exchangeRates) {
        var concatenatedCodes = exchangeRates.baseCurrency() + exchangeRates.targetCurrency();
        return exchangeRatesDao.findByName(concatenatedCodes).isPresent();
    }
}
