package com.cvt.backend_demo.controller;

import com.cvt.backend_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/sync-users")
    public String syncUsers() {
        userService.syncUsersToKeycloak();
        return "Users synchronized successfully!";
    }
}
