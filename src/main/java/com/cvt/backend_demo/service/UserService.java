package com.cvt.backend_demo.service;

import com.cvt.backend_demo.dto.UserDTO;
import com.cvt.backend_demo.entity.User;
import com.cvt.backend_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private KeycloakUserService keycloakService;

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

        User user1 = User.builder()
                .email(dto.getEmail()).firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .organization(dto.getOrganization())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .build();

        return ResponseEntity.ok(userRepository.save(user1));
    }
}


