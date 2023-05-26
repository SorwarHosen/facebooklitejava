package com.example.facebooklight.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "FriendshipTable")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userOneId;
    private long userTwoId;
    private Date createDate;

    public Friendship(long userOneId, long userTwoId, Date createDate) {
        this.userOneId = userOneId;
        this.userTwoId = userTwoId;
        this.createDate = new Date();
    }
}
