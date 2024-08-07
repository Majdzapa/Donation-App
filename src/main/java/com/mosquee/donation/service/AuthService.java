package com.mosquee.donation.service;

import com.mosquee.donation.common.keycloakadmin.KeycloakAdminClientConfig;
import com.mosquee.donation.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakAdminClientConfig keycloakAdminClientConfig;
    private final Keycloak keycloak;


    public void updatePassword(String userId, String newPassword) {
        UserResource userResource = keycloakAdminClientConfig.getUserResource(userId);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(newPassword);
        credentialRepresentation.setTemporary(false);

        userResource.resetPassword(credentialRepresentation);
    }

    public void sendVerificationEmail(String userId) {
        UserResource userResource = keycloakAdminClientConfig.getUserResource(userId);
        userResource.sendVerifyEmail(keycloakAdminClientConfig.getClientId());
    }


    public void updateUser(String userId, UserRepresentation updatedUser) {

        keycloak.realm(keycloakAdminClientConfig.getRealm())
                .users()
                .get(userId)
                .update(updatedUser);
    }

    public AccessTokenResponse loginUser(UserDto user) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakAdminClientConfig.getKeycloakServerUrl())
                    .realm(keycloakAdminClientConfig.getRealm())
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(keycloakAdminClientConfig.getClientId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .scope("openid")
                    .build();

            return keycloak.tokenManager().getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException("Failed to login user", e);
        }
    }


    public String registerUser(UserDto userDto) {

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);

        // Create password credentials
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userDto.getPassword());
        user.setCredentials(Collections.singletonList(passwordCred));

        // Create user
        Response response = keycloak.realm(keycloakAdminClientConfig.getRealm()).users().create(user);

        if (response.getStatus() == 201) {
            return "User registered successfully";
        } else {
            throw new RuntimeException("An error occurred when creating user: " + response.getStatusInfo());
        }
    }


    public UserRepresentation getUserDetails(String userId) {

        return keycloak.realm(keycloakAdminClientConfig.getRealm()).users().get(userId).toRepresentation();
    }
}
