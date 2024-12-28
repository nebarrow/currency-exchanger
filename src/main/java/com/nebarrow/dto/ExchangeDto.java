package com.nebarrow.dto;

import com.nebarrow.entity.Currency;
import lombok.Builder;

@Builder
public record ExchangeDto(
        Currency baseCurrency,
        Currency targetCurrency,
        double rate,
        double amount,
        double convertedAmount) {}
