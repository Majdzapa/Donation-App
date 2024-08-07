package com.mosquee.donation.controller;

import com.mosquee.donation.dto.UserDto;
import com.mosquee.donation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> loginUser(@RequestBody UserDto user) {

        return new ResponseEntity<AccessTokenResponse>(authService.loginUser(user), HttpStatus.CREATED);
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto user) {

        return new ResponseEntity<String>(authService.registerUser(user), HttpStatus.CREATED);
    }


    @GetMapping("/user-details")
    public ResponseEntity<UserRepresentation> getUserDetails(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getClaim("sub");

        return ResponseEntity.ok(authService.getUserDetails(userId));
    }


    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable String userId, @RequestBody Map<String, String> request) {
        String newPassword = request.get("password");
        authService.updatePassword(userId, newPassword);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/users/{userId}/send-verify-email")
    public ResponseEntity<Void> sendVerificationEmail(@PathVariable String userId) {
        authService.sendVerificationEmail(userId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{userId}")
    public void updateUser(@PathVariable String userId, @RequestBody UserRepresentation userRepresentation) {
        authService.updateUser(userId, userRepresentation);
    }


}