package com.savvy.dec.service;

import com.savvy.dec.entity.Provider;
import com.savvy.dec.entity.User;
import com.savvy.dec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository repo;
    @Override
    public String processOAuthPostLogin(String email, String clientName) {
        User existUser = repo.findByEmail(email);
        if (existUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setProvider(Provider.valueOf(clientName.toUpperCase()));
            newUser.setEnabled(true);
            newUser.setRole("USER");

            repo.save(newUser);

            return "Created new user: " + email;
        }
        return "User exist";

    }
}
