package com.mosquee.donation.service;

import com.mosquee.donation.common.keycloakadmin.KeycloakAdminClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final KeycloakAdminClientConfig keycloakAdminClientConfig;
    private final Keycloak keycloak;

    public List<UserRepresentation> getAllUsers() {

        return keycloak.realm(keycloakAdminClientConfig.getRealm()).users().list();
    }
}
