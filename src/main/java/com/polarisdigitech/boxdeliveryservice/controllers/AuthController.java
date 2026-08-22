package com.polarisdigitech.boxdeliveryservice.controllers;

import com.polarisdigitech.boxdeliveryservice.auth.Role;
import com.polarisdigitech.boxdeliveryservice.auth.SignupRequest;
import com.polarisdigitech.boxdeliveryservice.auth.SignupResponse;
import com.polarisdigitech.boxdeliveryservice.auth.SignupService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "Sign up as ADMIN.",
            description = "Sign up as an Admin"
    )
    @PostMapping("/signup/admin")
    public ResponseEntity<SignupResponse> signupAdmin(@Validated({Default.class}) @Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(201).body(signupService.signup(request, Role.ADMIN));
    }

    @Operation(
            summary = "Sign up as an Viewer.",
            description = "Sign up as a Viewer"
    )
    @PostMapping("/signup/viewer")
    public ResponseEntity<SignupResponse> signupView(@Validated({Default.class}) @Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(201).body(signupService.signup(request, Role.VIEWER));
    }

    @Operation(
            summary = "Sign up as an OPERATOR",
            description = "Sign up as a Operator"
    )
    @PostMapping("/signup/operator")
    public ResponseEntity<SignupResponse> signupOperator(@Validated({Default.class}) @Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(201).body(signupService.signup(request, Role.OPERATOR));
    }
}
