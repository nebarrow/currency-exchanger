package com.nebarrow.service;

import com.nebarrow.dao.ExchangeRatesDao;
import com.nebarrow.dto.request.ExchangeRequest;
import com.nebarrow.dto.response.ExchangeResponse;
import com.nebarrow.entity.ExchangeRate;
import com.nebarrow.exception.ElementNotFoundException;
import com.nebarrow.mapper.IExchangeMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ExchangeService {

    private final IExchangeMapper MAPPER = IExchangeMapper.INSTANCE;
    private final ExchangeRatesDao exchangeDao;

    public ExchangeResponse exchangeCurrenciesWithAmount(ExchangeRequest exchangeRequest) {
        var exchangeEntity = getEntity(exchangeRequest);
        if (!isExchangeRateExistsByCodes(exchangeEntity)) {
            throw new ElementNotFoundException("This exchange rate not found");
        }
        return exchangeDao.findRateByCodes(exchangeEntity).stream()
                .map(MAPPER::toDto)
                .findFirst()
                .map(exchange -> new ExchangeResponse(
                        exchange.baseCurrency(),
                        exchange.targetCurrency(),
                        exchange.rate(),
                        exchangeRequest.amount(),
                        exchange.rate() * exchangeRequest.amount()))
                .orElseThrow(() -> new RuntimeException("Can't convert this currencies"));
    }

    private boolean isExchangeRateExistsByCodes(ExchangeRate exchangeRequest) {
        return exchangeDao.findRateByCodes(exchangeRequest).isPresent();
    }

    private ExchangeRate getEntity(ExchangeRequest exchangeEntity) {
        return MAPPER.toEntity(exchangeEntity);
    }
}
