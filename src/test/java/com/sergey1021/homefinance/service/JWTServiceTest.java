package com.sergey1021.homefinance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
    }

    @Test
    void testGenerateAndValidateToken() {
        String email = "test@example.com";
        String token = jwtService.generateToken(email);

        assertNotNull(token);
        assertEquals(email, jwtService.extractUsername(token));
    }

    @Test
    void testValidateToken() {
        String email = "user@example.com";
        String token = jwtService.generateToken(email);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);

        assertTrue(jwtService.validateToken(token, userDetails));
    }
}
