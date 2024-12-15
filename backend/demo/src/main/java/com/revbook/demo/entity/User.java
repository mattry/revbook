package com.revbook.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String email;

    private String firstName;
    private String lastName;

    @JsonIgnore
    @NonNull
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
    private Set<Connection> followers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private Set<Connection> following = new HashSet<>();

}
