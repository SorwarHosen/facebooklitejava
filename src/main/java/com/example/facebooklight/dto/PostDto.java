package com.example.facebooklight.dto;

import com.example.facebooklight.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto extends Post {
    private int likesCount;
    private int commentsCount;
    private boolean isLiked;
    private String postImageUrl;
    private String actorName;
    private String actorImageUrl;
}
