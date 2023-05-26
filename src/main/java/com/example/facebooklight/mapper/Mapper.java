package com.example.facebooklight.mapper;

import com.example.facebooklight.dto.CommentDto;
import com.example.facebooklight.dto.LikeDto;
import com.example.facebooklight.dto.PostDto;
import com.example.facebooklight.dto.UserDto;
import com.example.facebooklight.model.Comment;
import com.example.facebooklight.model.Like;
import com.example.facebooklight.model.Post;
import com.example.facebooklight.model.User;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public static PostDto toPostDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setActorId(post.getActorId());
        postDto.setCreateDate(post.getCreateDate());
        return postDto;
    }

    public static List<PostDto> toPostDtoList(List<Post> posts){
        List<PostDto> postDto = new ArrayList<>();
        for (Post p : posts) {
            postDto.add(toPostDto(p));
        }
        return postDto;
    }


    public static UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setAddress(user.getAddress());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setCreateDate(user.getCreateDate());

        return userDto;
    }

    public static List<UserDto> toUserDtoList(List<User> users){
        List<UserDto> userDtos = new ArrayList<>();
        for (User u : users) {
            userDtos.add(toUserDto(u));
        }
        return userDtos;
    }


    public static CommentDto toCommentDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setComment(comment.getComment());
        commentDto.setPostId(comment.getPostId());
        commentDto.setActorId(comment.getActorId());
        commentDto.setCreateDate(comment.getCreateDate());
        return commentDto;
    }

    public static List<CommentDto> toCommentDtoList(List<Comment> comments){
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment c : comments) {
            commentDtos.add(toCommentDto(c));
        }
        return commentDtos;
    }


    public static LikeDto toLikeDto(Like like){
        LikeDto likeDto = new LikeDto();
        likeDto.setId(like.getId());
        likeDto.setPostId(like.getPostId());
        likeDto.setActorId(like.getActorId());
        likeDto.setCreateDate(like.getCreateDate());
        return likeDto;
    }

    public static List<LikeDto> toLikeDtoList(List<Like> likes){
        List<LikeDto> likeDto = new ArrayList<>();
        for (Like l : likes) {
            likeDto.add(toLikeDto(l));
        }
        return likeDto;
    }

}
