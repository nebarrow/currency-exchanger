package com.nebarrow.util;

import com.nebarrow.dto.request.CurrencyRequest;
import com.nebarrow.dto.request.ExchangeRateRequest;
import com.nebarrow.dto.request.ExchangeRatesRequest;
import com.nebarrow.dto.request.ExchangeRequest;
import com.nebarrow.validation.ParametersValidator;
import com.nebarrow.validation.ValidationResult;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Validator {
    private static final String WITHOUT_MISTAKES_CHARACTER = "";

    public ValidationResult validate(Object valueToValidate) {
        switch (valueToValidate.getClass().getSimpleName()) {
            case "CurrencyRequest": return validateCurrency((CurrencyRequest) valueToValidate);
            case "ExchangeRequest": return validateExchange((ExchangeRequest) valueToValidate);
            case "ExchangeRateRequest": return validateExchangeRate((ExchangeRateRequest) valueToValidate);
            default:
                return new ValidationResult("Unknown parameter type: " + valueToValidate.getClass().getSimpleName());
            }
        }

    private static ValidationResult validateCurrency(CurrencyRequest currency) {
        var nameMessageError = ParametersValidator.checkName(currency.name());
        var codeMessageError = ParametersValidator.checkCode(currency.code());
        var signMessageError = ParametersValidator.checkSign(currency.sign());

        if (!nameMessageError.isEmpty()) {
            return new ValidationResult(nameMessageError);
        } else if (!codeMessageError.isEmpty()) {
            return new ValidationResult(codeMessageError);
        } else if (!signMessageError.isEmpty()) {
            return new ValidationResult(signMessageError);
        }
        return new ValidationResult(WITHOUT_MISTAKES_CHARACTER);
    }

    private static ValidationResult validateExchange(ExchangeRequest exchange) {
        var baseCodeMessageError = ParametersValidator.checkCode(exchange.baseCode());
        var targetCodeMessageError = ParametersValidator.checkCode(exchange.targetCode());
        var amountMessageError = ParametersValidator.checkAmount(String.valueOf(exchange.amount()));

        if (!baseCodeMessageError.isEmpty()) {
            return new ValidationResult(baseCodeMessageError);
        } else if (!targetCodeMessageError.isEmpty()) {
            return new ValidationResult(targetCodeMessageError);
        } else if (!amountMessageError.isEmpty()) {
            return new ValidationResult(amountMessageError);
        }
        return new ValidationResult(WITHOUT_MISTAKES_CHARACTER);
    }

    private static ValidationResult validateExchangeRate(ExchangeRateRequest exchangeRatesRequest) {
        var baseCodeMessageError = ParametersValidator.checkCode(exchangeRatesRequest.baseCurrency());
        var targetCodeMessageError = ParametersValidator.checkCode(exchangeRatesRequest.targetCurrency());
        var rateMessageError = ParametersValidator.checkRate(String.valueOf(exchangeRatesRequest.rate()));

        if (!baseCodeMessageError.isEmpty()) {
            return new ValidationResult(baseCodeMessageError);
        } else if (!targetCodeMessageError.isEmpty()) {
            return new ValidationResult(targetCodeMessageError);
        } else if (!rateMessageError.isEmpty()) {
            return new ValidationResult(rateMessageError);
        }
        return new ValidationResult(WITHOUT_MISTAKES_CHARACTER);
    }

    private ValidationResult validateExchangeRates(ExchangeRatesRequest exchangeRatesRequest) {
        var baseCodeMessageError = ParametersValidator.checkCode(exchangeRatesRequest.baseCurrency());
        var targetCodeMessageError = ParametersValidator.checkCode(exchangeRatesRequest.targetCurrency());
        if (!baseCodeMessageError.isEmpty()) {
            return new ValidationResult(baseCodeMessageError);
        } else if (!targetCodeMessageError.isEmpty()) {
            return new ValidationResult(targetCodeMessageError);
        }
        return new ValidationResult(WITHOUT_MISTAKES_CHARACTER);
    }

}
