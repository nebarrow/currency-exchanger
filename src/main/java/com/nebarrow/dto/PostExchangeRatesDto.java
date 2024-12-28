package com.nebarrow.dto;

import com.nebarrow.entity.Currency;

public record PostExchangeRatesDto(Currency baseCurrency, Currency targetCurrency, double rate) {}
