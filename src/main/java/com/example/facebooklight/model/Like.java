package com.example.facebooklight.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "LikeTable")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long postId;
    private long actorId;
    private Date createDate;
}
