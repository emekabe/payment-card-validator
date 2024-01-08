package com.example.paymentcardvalidator.util;

import org.springframework.stereotype.Component;

@Component
public class CardDigitValidationUtil {

    public boolean validateFirstEightDigits(String firstEightDigits) {
        try {
            Long.parseLong(firstEightDigits);
        } catch (NumberFormatException e) {
            System.err.println("Number not passed as card digits");
            return false;
        }

        return firstEightDigits.length() == 8;
    }

}
