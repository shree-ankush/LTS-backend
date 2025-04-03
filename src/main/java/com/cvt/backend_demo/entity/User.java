package com.cvt.backend_demo.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("userName")
    private String username;

    @Column(nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @JsonProperty("firstName")
    @Column(nullable = false)
    private String firstName;

    @JsonProperty("lastName")
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @JsonProperty("organization")
    private String organization;

    @JsonProperty("password")
    @Column(nullable = false)
    private String password;


//    public User(String username, String email, String firstName, String lastName, String organization, String password) {
//        this.username = username;
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.organization=organization;
//        this.password = password;
//    }

}
