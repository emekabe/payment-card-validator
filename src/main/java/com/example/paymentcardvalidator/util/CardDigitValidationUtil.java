package com.example.paymentcardvalidator.util;

import org.springframework.stereotype.Component;

@Component
public class CardDigitValidationUtil {

    public boolean validateFirstEightDigits(String firstEightDigits) {
        return firstEightDigits != null && firstEightDigits.length() == 8;
    }

}
