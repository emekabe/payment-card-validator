package com.example.paymentcardvalidator.controller;

import com.example.paymentcardvalidator.dto.AuthenticateRequest;
import com.example.paymentcardvalidator.dto.AuthenticateResponse;
import com.example.paymentcardvalidator.service.AuthenticateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    private final AuthenticateService authenticateService;

    public AuthenticateController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticateResponse> loginCustomer(@RequestBody AuthenticateRequest authenticateRequest) {
        AuthenticateResponse loginResponse = authenticateService.login(authenticateRequest);
        if (loginResponse.getToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthenticateResponse> signupCustomer(@RequestBody AuthenticateRequest authenticateRequest) {
        return ResponseEntity.ok(authenticateService.signup(authenticateRequest));
    }

}
