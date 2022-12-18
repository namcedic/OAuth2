package com.savvy.dec.service;

import com.savvy.dec.entity.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);
   Optional<ConfirmationToken> getToken(String token);

    int setConfirmedAt(String token);
}
