package com.revbook.demo.entity;

import com.revbook.demo.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="connections")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long connectionId;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private User follower;

    // the user being followed
    @ManyToOne
    @JoinColumn(name="followee_id")
    private User followee;

}
