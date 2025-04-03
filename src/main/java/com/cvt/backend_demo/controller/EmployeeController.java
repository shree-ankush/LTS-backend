package com.cvt.backend_demo.controller;


import com.cvt.backend_demo.dto.EmployeeDTO;
import com.cvt.backend_demo.dto.UserDTO;
import com.cvt.backend_demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private final UserService userService;

    @PostMapping("/emp/register")
    public ResponseEntity registerEmployee(@RequestBody UserDTO userDTO){
        return userService.registerEmployee(userDTO);
    }

}
