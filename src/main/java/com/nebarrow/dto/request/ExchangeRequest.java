package com.nebarrow.dto.request;

public record ExchangeRequest(String baseCode, String targetCode, double amount) {
}
