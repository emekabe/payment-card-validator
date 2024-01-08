package com.example.paymentcardvalidator.service;

import com.example.paymentcardvalidator.dto.ExternalValidatorResponse;
import com.example.paymentcardvalidator.dto.StatsResponse;
import com.example.paymentcardvalidator.dto.VerifyCardResponse;
import com.example.paymentcardvalidator.entity.CardDetail;
import com.example.paymentcardvalidator.exception.InvalidCardDetailException;
import com.example.paymentcardvalidator.repository.CardDetailRepository;
import com.example.paymentcardvalidator.util.CardDigitValidationUtil;
import com.example.paymentcardvalidator.util.ExternalValidatorUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CardService {

    private final CardDetailRepository cardDetailRepository;
    private final ExternalValidatorUtil externalValidatorUtil;
    private final CardDigitValidationUtil cardDigitValidationUtil;

    public CardService(CardDetailRepository cardDetailRepository,
                       ExternalValidatorUtil externalValidatorUtil,
                       CardDigitValidationUtil cardDigitValidationUtil
    ) {
        this.cardDetailRepository = cardDetailRepository;
        this.externalValidatorUtil = externalValidatorUtil;
        this.cardDigitValidationUtil = cardDigitValidationUtil;
    }

    @Transactional
    public VerifyCardResponse verifyCard(String firstEightDigits) {
        validateFirstEightDigits(firstEightDigits);

        VerifyCardResponse verifyCardResponse = new VerifyCardResponse();
        Optional<CardDetail> cardDetailOptional = cardDetailRepository.findCardDetailByFirstEightDigits(firstEightDigits);

        if (cardDetailOptional.isPresent()) {
            verifyCardResponse.setSuccess(true);

            CardDetail cardDetail = cardDetailOptional.get();
            verifyCardResponse.setPayload(Map.ofEntries(
                    Map.entry("scheme", cardDetail.getScheme()),
                    Map.entry("type", cardDetail.getType()),
                    Map.entry("bank", cardDetail.getBank())
            ));

            CompletableFuture.runAsync(() -> incrementNumberOfHitsOnCardDetail(cardDetail));

            return verifyCardResponse;
        }

        ExternalValidatorResponse externalValidatorResponse = externalValidatorUtil.fetchCardDetailsFromExternalSource(firstEightDigits);
        if (externalValidatorResponse != null && externalValidatorResponse.getNumber() != null) {
            verifyCardResponse.setSuccess(true);
            verifyCardResponse.setPayload(Map.ofEntries(
                    Map.entry("scheme", externalValidatorResponse.getScheme()),
                    Map.entry("type", externalValidatorResponse.getType()),
                    Map.entry("bank", externalValidatorResponse.getBank().getName())
            ));

            CompletableFuture.runAsync(() -> saveNewCardDetail(firstEightDigits, externalValidatorResponse));

            return verifyCardResponse;
        }

        verifyCardResponse.setSuccess(false);
        return verifyCardResponse;
    }

    private void validateFirstEightDigits(String firstEightDigits) {
        if (!cardDigitValidationUtil.validateFirstEightDigits(firstEightDigits)) {
            throw new InvalidCardDetailException("First eight digits of payment card required and ensure exactly eight digits (numbers) are provided");
        }
    }

    private void incrementNumberOfHitsOnCardDetail(CardDetail cardDetail) {
        cardDetail.setNumberOfHits(cardDetail.getNumberOfHits() + 1);
        cardDetailRepository.save(cardDetail);
    }

    private void saveNewCardDetail(String firstEightDigits, ExternalValidatorResponse externalValidatorResponse) {
        CardDetail cardDetail = new CardDetail();

        cardDetail.setFirstEightDigits(firstEightDigits);
        cardDetail.setScheme(externalValidatorResponse.getScheme());
        cardDetail.setType(externalValidatorResponse.getType());
        cardDetail.setBank(externalValidatorResponse.getBank().getName());

        cardDetailRepository.save(cardDetail);
    }

    public StatsResponse getCardStats(int start, int limit) {
        StatsResponse statsResponse = new StatsResponse();

        PageRequest pageRequest = PageRequest.of(start, limit);
        List<CardDetail> cardDetails = cardDetailRepository.findAll(pageRequest).getContent();
        Map<String, String> statPayload = getStatsPayloadMap(cardDetails);

        statsResponse.setSuccess(true);
        statsResponse.setStart(start);
        statsResponse.setLimit(limit);
        statsResponse.setSize(cardDetailRepository.count());
        statsResponse.setPayload(statPayload);

        return statsResponse;
    }

    private Map<String, String> getStatsPayloadMap(List<CardDetail> cardDetails) {
        Map<String, String> statPayload = new LinkedHashMap<>();
        for (CardDetail cardDetail : cardDetails) {
            statPayload.put(cardDetail.getFirstEightDigits(), Integer.toString(cardDetail.getNumberOfHits()));
        }
        return statPayload;
    }

}
