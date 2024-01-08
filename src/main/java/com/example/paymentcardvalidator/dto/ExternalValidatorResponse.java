package com.example.paymentcardvalidator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalValidatorResponse {

    private NumberPojo number;
    private String scheme;
    private String type;
    private String brand;
    private CountryPojo country;
    private BankPojo bank;

    public NumberPojo getNumber() {
        return number;
    }

    public void setNumber(NumberPojo number) {
        this.number = number;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public CountryPojo getCountry() {
        return country;
    }

    public void setCountry(CountryPojo country) {
        this.country = country;
    }

    public BankPojo getBank() {
        return bank;
    }

    public void setBank(BankPojo bank) {
        this.bank = bank;
    }
}
