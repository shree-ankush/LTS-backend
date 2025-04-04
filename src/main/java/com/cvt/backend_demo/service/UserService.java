package com.cvt.backend_demo.service;

import com.cvt.backend_demo.dto.UserDTO;
import com.cvt.backend_demo.entity.User;
import com.cvt.backend_demo.repository.UserRepository;
import com.cvt.backend_demo.util.PasswordValidator;
import com.cvt.backend_demo.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private KeycloakUserService keycloakService;
    private final PasswordEncoder passwordEncoder;
    private final Keycloak keycloak;

    public void syncUsersToKeycloak() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (isValidUser(user)) {
                keycloakService.createUserInKeycloak(user);
            }
        }
    }

    private boolean isValidUser(User user) {
        return user.getUsername() != null && !user.getUsername().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty() &&
                user.getFirstName() != null && !user.getFirstName().isEmpty() &&
                user.getLastName() != null && !user.getLastName().isEmpty() &&
                user.getOrganization() != null && !user.getOrganization().isEmpty() &&
                user.getPassword() != null && !user.getPassword().isEmpty();
    }


    public ResponseEntity registerEmployee(UserDTO dto) {

        if (!UserValidator.isValidUser(dto.getUsername(), dto.getEmail(), dto.getFirstName(), dto.getLastName(),
                dto.getOrganization(), dto.getPassword())) {
            throw new IllegalArgumentException("❌ Invalid user data");
        }
        User user1 = User.builder()
                .email(dto.getEmail()).firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .organization(dto.getOrganization())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .build();

        return ResponseEntity.ok(userRepository.save(user1));
    }


    public void updatePassword(String username, String newPassword) {
        // ✅ Validate new password according to security rules
        if (!PasswordValidator.isValid(newPassword)) {
            throw new IllegalArgumentException("Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
        }

        // ✅ Fetch user from PostgreSQL database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found in DB"));

        // ✅ Update password in database
        user.setPassword(newPassword);
        userRepository.save(user); // <- Password is updated in DB here

        // ✅ Initialize Keycloak client and access realm

        RealmResource realm = keycloak.realm("automated_leave_tracker");

        // ✅ Search for the user in Keycloak
        List<UserRepresentation> foundUsers = realm.users().search(username);
        if (foundUsers.isEmpty()) {
            throw new RuntimeException("User not found in Keycloak");
        }

        // ✅ Update password in Keycloak
        UserResource kcUser = realm.users().get(foundUsers.get(0).getId());
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        credential.setTemporary(false);

        kcUser.resetPassword(credential); // <- Password is updated in Keycloak here
    }


//    public boolean forgotPassword(String email) {
//        // 🔍 Check if user exists in DB
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty()) {
//            return false;
//        }
//
//        // 🔐 Trigger Keycloak email reset
//        RealmResource realm = keycloak.realm("automated_leave_tracker");
//        List<UserRepresentation> users = realm.users().search(email, 0, 1);
//
//        if (users.isEmpty()) {
//            return false;
//        }
//
//        UserResource userResource = realm.users().get(users.get(0).getId());
//
//        try {
//            userResource.executeActionsEmail(List.of("UPDATE_PASSWORD"));  // 🔄 Triggers reset email
//            return true;
//        } catch (Exception e) {
//            System.out.println("Failed to send reset email: " + e.getMessage());
//            return false;
//        }
//    }

}


