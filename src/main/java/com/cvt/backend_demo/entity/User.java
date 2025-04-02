package com.cvt.backend_demo.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    private String password;


    public User(String username, String email, String firstName, String lastName, String organization, String password) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization=organization;
        this.password = password;
    }
}
