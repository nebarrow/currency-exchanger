package com.nebarrow.dto;


import com.nebarrow.entity.Currency;

public record ExchangeRatesDto(long id,
                               Currency baseCurrency,
                               Currency targetCurrency,
                               double rate) {}
