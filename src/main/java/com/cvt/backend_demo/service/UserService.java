package com.cvt.backend_demo.service;

import com.cvt.backend_demo.dto.UserDTO;
import com.cvt.backend_demo.entity.User;
import com.cvt.backend_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

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


