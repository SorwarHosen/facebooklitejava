package com.example.facebooklight.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "ImagesTable")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Photos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long entityId;
    private String photoUrl;
    private Date createDate;

    public Photos(long entityId, String photoUrl) {
        this.entityId = entityId;
        this.photoUrl = photoUrl;
        createDate = new Date();
    }
}
