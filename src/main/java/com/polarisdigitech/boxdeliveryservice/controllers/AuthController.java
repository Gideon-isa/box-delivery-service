package com.polarisdigitech.boxdeliveryservice.controllers;

import com.polarisdigitech.boxdeliveryservice.auth.SignupRequest;
import com.polarisdigitech.boxdeliveryservice.auth.SignupResponse;
import com.polarisdigitech.boxdeliveryservice.auth.SignupService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Validated({Default.class}) @Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(201).body(signupService.signup(request));
    }
}
