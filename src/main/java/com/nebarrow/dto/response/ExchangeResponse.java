package com.nebarrow.dto.response;

import com.nebarrow.entity.Currency;
import lombok.Builder;

@Builder
public record ExchangeResponse(
        Currency baseCurrency,
        Currency targetCurrency,
        double rate,
        double amount,
        double convertedAmount) {}
