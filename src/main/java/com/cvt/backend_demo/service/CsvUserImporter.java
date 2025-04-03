package com.cvt.backend_demo.service;


import com.cvt.backend_demo.dto.UserDTO;
import com.cvt.backend_demo.entity.User;
import com.cvt.backend_demo.repository.UserRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class CsvUserImporter {

    @Autowired
    private UserRepository userRepository;

//    @Bean
//    CommandLineRunner runCsvImport(CsvReaderService csvReaderService, KeycloakUserService userService) {
//        return args -> {
//            String filePath = "src/main/resources/users.csv"; // ✅ Ensure this file exists
//
//            try {
//                // ✅ Read CSV file
////                List<UserDTO> users1 = csvReaderService.readCsv(filePath);
//                List<UserDTO> users = csvReaderService.readCsv(filePath);
//                 System.out.println("📄 CSV File Read Successfully. Processing Users...");
//
//                for (UserDTO user : users) {
//                    String userId = userService.createUser(user);
//                    if (userId != null) {
////                        userService.sendVerificationEmail(userId);
//                        System.out.println("✅ User Created: " + user.getEmail());
//                    } else {
//                        System.out.println("❌ Failed to Create User: " + user.getEmail());
//                    }
//                }
//                System.out.println("🎉 CSV User Import Completed!");
//
//            } catch (IOException e) {
//                System.err.println("❌ Error Reading CSV File: " + e.getMessage());
//            }
//        };
//    }

    public void importUsersFromCsv(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // Remove header

            for (String[] row : rows) {
                String username = row[0];
                String email = row[1];
                String firstName = row[2];
                String lastName = row[3];
                String organization = row[4];
                String password = row[5];

                // Check if user already exists before saving
                if (!userRepository.existsByUsername(username) && !userRepository.existsByEmail(email)) {
//                    User user = new User(username, email, firstName, lastName, organization, password);
                    User user = User.builder().username(username)
                            .email(email)
                            .firstName(firstName)
                            .lastName(lastName)
                            .organization(organization)
                            .password(password)
                            .build();
                    userRepository.save(user);
                    System.out.println("✅ User Created: " + username);
                } else {
                    System.out.println("⚠️ User already exists: " + username);
                }
            }

        } catch (IOException | CsvException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }
}