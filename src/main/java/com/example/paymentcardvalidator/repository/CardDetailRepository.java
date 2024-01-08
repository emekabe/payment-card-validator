package com.example.paymentcardvalidator.repository;

import com.example.paymentcardvalidator.entity.CardDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardDetailRepository extends JpaRepository<CardDetail, Long> {

    Optional<CardDetail> findCardDetailByFirstEightDigits(String firstEightDigits);

}
