package com.savvy.dec.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;

}
