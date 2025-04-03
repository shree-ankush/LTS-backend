package com.cvt.backend_demo.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class UserController {
    @GetMapping("/user/hello")
    @PreAuthorize("hasRole('client_user')")
    public String hello(){
        return "hello keycloak world from spring boot.";
    }

    @GetMapping("/admin/hello")
    @PreAuthorize("hasRole('client_admin')")
    public String hello2(){
        return "hello keycloak world from spring boot - ADMIN." ;
    }



}
