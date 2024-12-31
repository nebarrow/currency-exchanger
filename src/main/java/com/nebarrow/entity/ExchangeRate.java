package com.nebarrow.entity;

import lombok.*;

import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ExchangeRate {
    private Long id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private Double rate;

    public String getBaseCurrencyCode() { return baseCurrency.getCode(); }

    public String getTargetCurrencyCode() { return targetCurrency.getCode(); }
}
