package com.example.paymentcardvalidator.service;

import com.example.paymentcardvalidator.dto.AuthenticateRequest;
import com.example.paymentcardvalidator.dto.AuthenticateResponse;
import com.example.paymentcardvalidator.entity.Customer;
import com.example.paymentcardvalidator.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class AuthenticateService {

    private final CustomerRepository customerRepository;

    public AuthenticateService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public AuthenticateResponse login(AuthenticateRequest authenticateRequest) {
        String username = authenticateRequest.getUsername();
        Optional<Customer> customerOptional = customerRepository.findCustomerByUsername(username);

        if (customerOptional.isPresent() && customerOptional.get().getPassword().equals(authenticateRequest.getPassword())) {
            return new AuthenticateResponse("Success", Base64.getEncoder().encodeToString(username.getBytes()));
        }

        return new AuthenticateResponse("Invalid username or password");
    }

    public AuthenticateResponse signup (AuthenticateRequest authenticateRequest) {
        if (customerRepository.findCustomerByUsername(authenticateRequest.getUsername()).isPresent()) {
            return new AuthenticateResponse("Customer by that username already exists");
        }

        Customer customer = new Customer();
        customer.setUsername(authenticateRequest.getUsername());
        customer.setPassword(authenticateRequest.getPassword()); // Password should be hashed before saving for security purposes

        customerRepository.save(customer);
        return new AuthenticateResponse("Success");
    }

}
