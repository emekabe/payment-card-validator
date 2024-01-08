package com.example.paymentcardvalidator.service;

import com.example.paymentcardvalidator.dto.BankPojo;
import com.example.paymentcardvalidator.dto.ExternalValidatorResponse;
import com.example.paymentcardvalidator.dto.StatsResponse;
import com.example.paymentcardvalidator.dto.VerifyCardResponse;
import com.example.paymentcardvalidator.entity.CardDetail;
import com.example.paymentcardvalidator.exception.InvalidCardDetailException;
import com.example.paymentcardvalidator.repository.CardDetailRepository;
import com.example.paymentcardvalidator.util.CardDigitValidationUtil;
import com.example.paymentcardvalidator.util.ExternalValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardDetailRepository cardDetailRepository;

    @Mock
    private ExternalValidatorUtil externalValidatorUtil;

    @Mock
    private CardDigitValidationUtil cardDigitValidationUtil;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void verifyCard_ValidCardDetails_Success() {
        String firstEightDigits = "12345678";

        ExternalValidatorResponse externalValidatorResponse = new ExternalValidatorResponse();
        externalValidatorResponse.setScheme("visa");
        externalValidatorResponse.setType("debit");
        externalValidatorResponse.setBank(new BankPojo("Mintyn"));
        when(externalValidatorUtil.fetchCardDetailsFromExternalSource(firstEightDigits))
                .thenReturn(externalValidatorResponse);

        when(cardDigitValidationUtil.validateFirstEightDigits(firstEightDigits))
                .thenReturn(true);

        when(cardDetailRepository.findCardDetailByFirstEightDigits(firstEightDigits))
                .thenReturn(Optional.of(new CardDetail("12345678", "visa", "debit", "BankName")));

        VerifyCardResponse verifyCardResponse = cardService.verifyCard(firstEightDigits);

        assertTrue(verifyCardResponse.isSuccess());
        assertNotNull(verifyCardResponse.getPayload());
    }

    @Test
    void verifyCard_InvalidCardDetails_ThrowsException() {
        String firstEightDigits = "invalid";

        when(cardDigitValidationUtil.validateFirstEightDigits(firstEightDigits))
                .thenReturn(false);

        assertThrows(InvalidCardDetailException.class, () -> cardService.verifyCard(firstEightDigits));

        verify(cardDigitValidationUtil, times(1)).validateFirstEightDigits(firstEightDigits);
        verify(cardDetailRepository, never()).findCardDetailByFirstEightDigits(any());
        verify(externalValidatorUtil, never()).fetchCardDetailsFromExternalSource(any());
        verify(cardDetailRepository, never()).save(any());
    }

    @Test
    void getCardStats_ValidInput_Success() {
        int start = 0;
        int limit = 10;

        List<CardDetail> cardDetails = List.of(new CardDetail("12345678", "visa", "debit", "BankName"));
        when(cardDetailRepository.findAll((Pageable) any()))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(cardDetails));

        StatsResponse statsResponse = cardService.getCardStats(start, limit);

        assertTrue(statsResponse.isSuccess());
        assertEquals(start, statsResponse.getStart());
        assertEquals(limit, statsResponse.getLimit());
        assertNotNull(statsResponse.getPayload());

        verify(cardDetailRepository, times(1)).findAll((Pageable) any());
    }
}
