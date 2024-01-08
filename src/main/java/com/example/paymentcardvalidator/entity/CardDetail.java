package com.example.paymentcardvalidator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class CardDetail {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_eight_digits", nullable = false, unique = true)
    private String firstEightDigits;

    @Column(name = "scheme", nullable = false)
    private String scheme;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "bank")
    private String bank;

    @Column(name = "number_of_hits", nullable = false)
    private Integer numberOfHits = 1;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFirstEightDigits() {
        return firstEightDigits;
    }

    public void setFirstEightDigits(String firstEightDigits) {
        this.firstEightDigits = firstEightDigits;
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

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Integer getNumberOfHits() {
        return numberOfHits;
    }

    public void setNumberOfHits(Integer numberOfHits) {
        this.numberOfHits = numberOfHits;
    }
}
