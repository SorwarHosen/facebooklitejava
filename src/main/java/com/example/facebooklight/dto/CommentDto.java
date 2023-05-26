package com.example.facebooklight.dto;

import com.example.facebooklight.model.Comment;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDto extends Comment {
    private String actorName;
    private String actorImageUrl;
}
