package com.savvy.dec.service;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
