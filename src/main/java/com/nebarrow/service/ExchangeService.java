package com.nebarrow.service;

import com.nebarrow.dao.ExchangeRatesDao;
import com.nebarrow.dto.ExchangeDto;
import com.nebarrow.exception.ElementNotFoundException;
import com.nebarrow.mapper.ExchangeMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeRatesDao exchangeDao;

    public ExchangeDto exchangeCurrenciesWithAmount(String baseCode, String targetCode, double amount) {
        if (!isExchangeRateExistsByCodes(baseCode, targetCode)) {
            throw new ElementNotFoundException("This exchange rate not found");
        }
        return exchangeDao.findRateByCodes(baseCode, targetCode).stream()
                .map(ExchangeMapper::toDto)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .map(exchange -> new ExchangeDto(
                        exchange.baseCurrency(),
                        exchange.targetCurrency(),
                        exchange.rate(),
                        amount,
                        exchange.rate() * amount))
                .orElseThrow(() -> new RuntimeException("Can't convert this currencies"));
    }

    private boolean isExchangeRateExistsByCodes(String baseCode, String targetCode) {
        return exchangeDao.findRateByCodes(baseCode, targetCode).isPresent();
    }
}
