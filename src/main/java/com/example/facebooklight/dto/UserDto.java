package com.example.facebooklight.dto;

import com.example.facebooklight.model.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDto extends User {
    private String photo_url;
}
