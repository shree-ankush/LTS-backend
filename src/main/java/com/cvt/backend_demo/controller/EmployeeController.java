package com.cvt.backend_demo.controller;


import com.cvt.backend_demo.dto.UserDTO;
import com.cvt.backend_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/demo/emp")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerEmployee(@RequestBody UserDTO userDTO){
        return userService.registerEmployee(userDTO);
    }


    @PatchMapping("/update-password")
    @PreAuthorize("hasAnyRole('client_user', 'client_admin')")
    public ResponseEntity<?> updatePassword(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String newPassword
    ) {
        String username = jwt.getClaimAsString("preferred_username");

        try {
            userService.updatePassword(username, newPassword);
            return ResponseEntity.ok("Password updated successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Password update failed: " + e.getMessage());
        }
    }


//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
//        boolean result = userService.forgotPassword(email);
//        if (result) {
//            return ResponseEntity.ok("Password reset email sent successfully!");
//        } else {
//            return ResponseEntity.badRequest().body("User not found or failed to send email.");
//        }
//    }

}
