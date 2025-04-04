package com.cvt.backend_demo.service;


import com.cvt.backend_demo.dto.UserDTO;
import com.cvt.backend_demo.entity.User;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakUserService {


    private final Keycloak keycloak;
    private final String realm = "automated_leave_tracker"; // ✅ Change this to your Keycloak realm name

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
//        Keycloak keycloak2 = KeycloakBuilder.builder()
//                .serverUrl("http://localhost:8080")
//                .realm("automated_leave_tracker")
//                .grantType("password")
//                .clientId("admin-cli")  // Make sure client has admin permissions
//                .username("ankush")
//                .password("ankush")
//                .build();
//
//        String token = keycloak2.tokenManager().getAccessToken().getToken();
//        System.out.println("Access Token: " + token);
    }




    public String createUser(UserDTO userDTO) {
        try {
            // ✅ Create UserRepresentation
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEnabled(true);
            user.setEmailVerified(false);

            System.out.println(" " + user.getEmail());

            // ✅ Get Realm Resource
            RealmResource realmResource = keycloak.realm(realm);
            Response response = realmResource.users().create(user);

            // ✅ Check if user was created successfully
            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                System.out.println("✅ User Created: " + userDTO.getEmail());

                // ✅ Set Password
                setPassword(userId, userDTO.getPassword());
                // ✅ Assign Client Role to User (Add this line)
//                assignClientRole(userId, "leave-portal", "client_user");
                return userId;
            } else {
                // ✅ Print exact error from Keycloak
                String errorMessage = response.readEntity(String.class);
                System.err.println("❌ User creation failed: " + response.getStatus() + " - " + errorMessage);
                throw new RuntimeException("User creation failed: " + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace(); // ✅ Print the full error stack
            throw new RuntimeException("Exception while creating user", e);
        }
    }


    public void createUserInKeycloak(User userDTO) {
        // Get the Keycloak realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Create user representation
        UserRepresentation userRep = new UserRepresentation();
        userRep.setUsername(userDTO.getUsername());
        userRep.setEmail(userDTO.getEmail());
        userRep.setFirstName(userDTO.getFirstName());
        userRep.setLastName(userDTO.getLastName());
        userRep.setEnabled(true);

        // Set credentials
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDTO.getPassword());
        credential.setTemporary(false);

        userRep.setCredentials(Collections.singletonList(credential));

        // Create user in Keycloak
        usersResource.create(userRep);
    }

    private void setPassword(String userId, String password) {
        String customPassword = "pass";
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(customPassword);
        credential.setTemporary(false); // Set to true if you want users to reset passwords on first login

        keycloak.realm(realm).users().get(userId).resetPassword(credential);
        System.out.println("🔑 Password Set for User: " + userId);
    }
    private void assignClientRole(String userId, String clientId, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);

        // ✅ Print All Clients to Debug
        List<ClientRepresentation> clients = realmResource.clients().findAll();
        System.out.println("Available Clients:");
        for (ClientRepresentation client : clients) {
            System.out.println(" - " + client.getClientId());
        }


        // ✅ Get Client UUID
        List<ClientRepresentation> foundClients = realmResource.clients().findByClientId(clientId);
        if (foundClients.isEmpty()) {
            throw new RuntimeException("❌ Client with ID '" + clientId + "' not found!");
        }
        String clientUUID = foundClients.get(0).getId();

        // ✅ Get Role Representation (Assign "user" instead of "client_user")
        RoleRepresentation clientRole = realmResource.clients()
                .get(clientUUID)
                .roles()
                .get("user")  // ✅ Change "client_user" → "user"
                .toRepresentation();

        // ✅ Assign Role to User
        realmResource.users().get(userId)
                .roles()
                .clientLevel(clientUUID)
                .add(Collections.singletonList(clientRole));

        System.out.println("✅ Assigned Client Role 'user' to User: " + userId);
    }




//
//    public void sendVerificationEmail(String userId) {
//        keycloak.realm(realm).users().get(userId).sendVerifyEmail();
//        System.out.println("📧 Verification Email Sent to User ID: " + userId);
//    }
}

