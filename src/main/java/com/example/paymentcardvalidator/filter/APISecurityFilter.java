package com.example.paymentcardvalidator.filter;

import com.example.paymentcardvalidator.dto.AuthenticateResponse;
import com.example.paymentcardvalidator.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
@WebFilter(urlPatterns = {"/card-scheme/verify", "/card-scheme/stats"})
public class APISecurityFilter extends OncePerRequestFilter {

    private final CustomerRepository customerRepository;

    public APISecurityFilter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestApiKey = request.getHeader("apiKey");
        String decodedCustomerData = new String(Base64.getDecoder().decode(requestApiKey));
        if (customerRepository.findCustomerByUsername(decodedCustomerData).isEmpty()) {
            AuthenticateResponse authenticateResponse = new AuthenticateResponse("Invalid apiKey received");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(authenticateResponse));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
