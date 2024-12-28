package com.nebarrow.validation;

import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParametersValidator {

    private static final String WITHOUT_MISTAKES_CHARACTER = "";
    private static final Integer MAX_LENGTH_OF_NAME = 25;
    String message = "{\"message\": \"%s\"}";

    public static String checkName(String name) {
        var pattern = Pattern.compile("^[A-Za-z\\s]{1,25}$");

        if (name == null || name.isEmpty() || name.length() > MAX_LENGTH_OF_NAME) {
            return errorMessage("Name must be between 1 and 25 characters long");
        }

        if (!pattern.matcher(name).matches()) {
            return errorMessage("Name can only be english");
        }
        return WITHOUT_MISTAKES_CHARACTER;
    }

    public static String checkCode(String code) {
        var pattern = Pattern.compile("^[A-Za-z]{3}$");
        if (!pattern.matcher(code).matches()) {
            return errorMessage("Code should contains only english letters with length 3");
        }
        return WITHOUT_MISTAKES_CHARACTER;
    }

    public static String checkSign(String sign) {
        var pattern = Pattern.compile("^[\\p{Alnum}\\p{P}\\p{Sc}\\s]{1,5}$");
        if (!pattern.matcher(sign).matches()) {
            return errorMessage("Sign should contains only english letters or numbers with length from 1 to 5");
        }
        return WITHOUT_MISTAKES_CHARACTER;
    }

    public static String checkCodePair(String pair) {
        var pattern = Pattern.compile("^[A-Za-z]{6}$");
        if (!pattern.matcher(pair).matches()) {
            return errorMessage("Code pair should contains only english letters with length 6");
        }
        return WITHOUT_MISTAKES_CHARACTER;
    }

    public static String checkAmount(String amount) {
        var pattern = Pattern.compile("^[0-9]{1,8}(\\.[0-9]+)?$");
        if (!pattern.matcher(amount).matches()) {
            return errorMessage("Amount should contains only one num from 0 to 9 and must be less than 999");
        }
        return WITHOUT_MISTAKES_CHARACTER;
    }

    public static String checkRate(String rate) {
        var pattern = Pattern.compile("^[0-9]{1,3}(\\.[0-9]+)?$");
        if (!pattern.matcher(rate).matches()) {
            return errorMessage("Rate should contains only one num from 0 to 9 and must be less than 999");
        }
        return WITHOUT_MISTAKES_CHARACTER;
    }


    private String errorMessage(String errorMessage) {
        return message.formatted(errorMessage);
    }

}
