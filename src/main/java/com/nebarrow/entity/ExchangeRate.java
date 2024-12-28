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

    public Long getBaseCurrencyId() {
        return baseCurrency.getId();
    }

    public Long getTargetCurrencyId() {
        return targetCurrency.getId();
    }
}
