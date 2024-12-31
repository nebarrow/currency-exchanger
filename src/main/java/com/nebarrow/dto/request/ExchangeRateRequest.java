package com.nebarrow.dto.request;

public record ExchangeRateRequest(String baseCurrency, String targetCurrency, double rate) {
}
