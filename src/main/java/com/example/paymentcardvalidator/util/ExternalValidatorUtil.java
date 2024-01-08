package com.example.paymentcardvalidator.util;

import com.example.paymentcardvalidator.dto.ExternalValidatorResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ExternalValidatorUtil {

    public ExternalValidatorResponse fetchCardDetailsFromExternalSource(String firstEightDigits) {
        WebClient webClient = WebClient.create("https://lookup.binlist.net");

        ExternalValidatorResponse response = null;
        try {
            response = webClient
                    .get()
                    .uri("/" + firstEightDigits)
                    .retrieve()
                    .bodyToMono(ExternalValidatorResponse.class)
                    .block();
        } catch (Exception e) {
            System.err.println("An eror occured while fetching card details from 3rd party API");
        }

        return response;
    }

}
