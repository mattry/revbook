package com.revbook.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/*
    Class to model users.
    Users consist of first names, last names, email addresses, and passwords.
    Emails must be unique.
    Users can also have connections to other users.
*/

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private String password;

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
    private Set<Connection> followers = new HashSet<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<Connection> following = new HashSet<>();

}
