package com.example.paymentcardvalidator.controller;

import com.example.paymentcardvalidator.dto.StatsResponse;
import com.example.paymentcardvalidator.dto.VerifyCardResponse;
import com.example.paymentcardvalidator.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-scheme")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/verify/{firstEightDigits}")
    public ResponseEntity<VerifyCardResponse> verifyCard (@PathVariable(name = "firstEightDigits") String firstEightDigits) {
        return ResponseEntity.ok(cardService.verifyCard(firstEightDigits));
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getCardStats(@RequestParam(defaultValue = "1") int start,
                                                      @RequestParam(defaultValue = "3") int limit) {
        return ResponseEntity.ok(cardService.getCardStats(start, limit));
    }

}
