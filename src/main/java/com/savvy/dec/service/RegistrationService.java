package com.savvy.dec.service;

import com.savvy.dec.entity.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {
    String register(RegistrationRequest registrationRequest);

    String confirmToken(String token);
}
