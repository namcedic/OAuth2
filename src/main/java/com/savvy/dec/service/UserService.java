package com.savvy.dec.service;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public String processOAuthPostLogin(String username, String clientName);
}
