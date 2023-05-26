package com.example.facebooklight.controller;

import com.example.facebooklight.dto.CommentDto;
import com.example.facebooklight.mapper.Mapper;
import com.example.facebooklight.model.Comment;
import com.example.facebooklight.model.ResponseData;
import com.example.facebooklight.model.User;
import com.example.facebooklight.repo.CommentRepo;
import com.example.facebooklight.repo.ImagesRepo;
import com.example.facebooklight.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController()
public class CommentController {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ImagesRepo imagesRepo;

    @PostMapping("/comment")
        public ResponseEntity<ResponseData<Comment>> comment(@RequestHeader Map<String, String> headers, @RequestBody Comment comment) {
        try {
            comment.setActorId(Long.parseLong(headers.get("user_id")));
            comment.setCreateDate(new Date());
            CommentDto commentDto = Mapper.toCommentDto(commentRepo.save(comment));
            setCommentUserData(commentDto);

            return new ResponseEntity<>(new ResponseData<>(commentDto, "comment posted", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setCommentUserData(CommentDto commentDto) {
        Optional<User> user = userRepo.findById(commentDto.getActorId());
        if(user.isPresent()){
            commentDto.setActorName(user.get().getName());
            Optional<String> image = imagesRepo.getPhotoByEntity(user.get().getId());
            if(image.isPresent()){
                commentDto.setActorImageUrl(image.get());
            }
        }
    }


    @GetMapping("/getAllComments/{postId}")
    public ResponseEntity<ResponseData<List<CommentDto>>> getAllComments(@RequestHeader Map<String, String> headers, @PathVariable Long postId) {
        try {
            List<CommentDto> comments = Mapper.toCommentDtoList(commentRepo.getAllCommentsByPostId(postId));

            for(CommentDto c: comments) {
                setCommentUserData(c);
            }

            return new ResponseEntity<>(new ResponseData<>(comments, "success", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateCommentById/{id}")
    public ResponseEntity<ResponseData<Comment>> updateUser(@RequestHeader Map<String, String> headers,@PathVariable Long id,
                                                         @RequestBody Comment newComment) {
        Optional<Comment> oldComment = commentRepo.findById(id);
        if (oldComment.isPresent()) {
            newComment.setId(oldComment.get().getId());
            Comment comment = commentRepo.save(newComment);
            return new ResponseEntity<>(new ResponseData<>(comment, "success", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(null, "comment not found", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<ResponseData<HttpStatus>> deleteUserById(@RequestHeader Map<String, String> headers,@PathVariable Long id) {
        Optional<Comment> oldComment = commentRepo.findById(id);
        if (oldComment.isPresent()) {
            commentRepo.delete(oldComment.get());
            return new ResponseEntity<>(new ResponseData<>(null, "comment successfully deleted", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(null, "comment not found", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        }
    }
}
