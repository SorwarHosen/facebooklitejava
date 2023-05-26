package com.example.facebooklight.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "PostTable")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long actorId;
    private String title;

    @Column(length = 4024)
    private String content;
    private Date createDate;

    public Post(long actorId, String title, String content) {
        this.actorId = actorId;
        this.title = title;
        this.content = content;
        createDate = new Date();
    }
}
