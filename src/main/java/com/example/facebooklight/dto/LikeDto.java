package com.example.facebooklight.dto;

import com.example.facebooklight.model.Like;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LikeDto extends Like {
    private String actorName;
    private String actorImageUrl;
}
