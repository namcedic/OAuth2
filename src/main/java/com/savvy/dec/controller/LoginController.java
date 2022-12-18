package com.savvy.dec.controller;

import com.google.gson.Gson;
import com.savvy.dec.entity.*;
import com.savvy.dec.exception.TokenRefreshException;
import com.savvy.dec.repository.UserRepository;
import com.savvy.dec.service.JWTProvider;
import com.savvy.dec.service.RefreshTokenService;
import com.savvy.dec.service.TokenRefreshRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoginController {
    public static final String TYPE_BEARER = "Bearer";

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private JwtResponse jwtResponse;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("invalid user or password");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("invalid user or password");
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), Collections.singleton(new SimpleGrantedAuthority(user.getRole()))));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String jwt = jwtProvider.generateJwtToken(customUserDetails);

//        List<String> roles = customUserDetails.getAuthorities().stream().map(item -> item.getAuthority())
//                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customUserDetails.getUser().getId());

        JwtResponse jwtResponse = new JwtResponse(jwt, TYPE_BEARER, refreshToken.getToken());

        Gson g = new Gson();
        String result = g.toJson(jwtResponse);
        return result;
    }

    @PostMapping(value = "/refreshtoken", produces = MediaType.APPLICATION_JSON_VALUE)
    public String refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService
                .findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtProvider.generateTokenFromUsername(user.getEmail());
                    TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(token, requestRefreshToken);
                    Gson g = new Gson();
                    return g.toJson(tokenRefreshResponse);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/signout")
    public String logoutUser() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = customUserDetails.getUser().getId();
        refreshTokenService.deleteByUserId(userId);
        return "Log out successful!";
    }
}
