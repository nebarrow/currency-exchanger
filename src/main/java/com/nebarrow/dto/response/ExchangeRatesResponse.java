package com.nebarrow.dto.response;


import com.nebarrow.entity.Currency;

public record ExchangeRatesResponse(long id,
                                    Currency baseCurrency,
                                    Currency targetCurrency,
                                    double rate) {}
