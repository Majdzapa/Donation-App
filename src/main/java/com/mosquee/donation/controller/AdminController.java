package com.mosquee.donation.controller;

import com.mosquee.donation.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping(value = "/allUsers")
    public List<UserRepresentation> getAllUsers() {
        return adminService.getAllUsers();
    }
}
