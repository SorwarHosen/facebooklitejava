package com.example.facebooklight.controller;

import com.example.facebooklight.dto.LikeDto;
import com.example.facebooklight.mapper.Mapper;
import com.example.facebooklight.model.Like;
import com.example.facebooklight.model.ResponseData;
import com.example.facebooklight.model.User;
import com.example.facebooklight.repo.ImagesRepo;
import com.example.facebooklight.repo.LikeRepo;
import com.example.facebooklight.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController()
public class LikeController {

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ImagesRepo imagesRepo;

    @PostMapping("/likePost/{postId}")
    public ResponseEntity<ResponseData<Like>> likePost(@RequestHeader Map<String, String> headers,@PathVariable Long postId) {
        try {
            Like like = new Like();
            like.setActorId(Long.parseLong(headers.get("user_id")));
            like.setPostId(postId);
            Like savedLike = likeRepo.save(like);
            return new ResponseEntity<>(new ResponseData<>(savedLike, "post liked", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/unlikePost/{postId}")
    public ResponseEntity<ResponseData<Like>> unlikePost(@RequestHeader Map<String, String> headers, @PathVariable Long postId) {

            Optional<Like> like = likeRepo.getLikeByLikerAndPostId(Long.parseLong(headers.get("user_id")),postId);
            if(like.isPresent()){
                likeRepo.delete(like.get());
                return new ResponseEntity<>(new ResponseData<>(like.get(), "post unliked", HttpStatus.OK.value()), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ResponseData<>(null, "To unlike a post you have to like it first", HttpStatus.BAD_REQUEST.value()), HttpStatus.OK);
            }


    }

    @GetMapping("/getAllLikes/{postId}")
    public ResponseEntity<ResponseData<List<LikeDto>>> getAllLikes(@PathVariable Long postId) {
        try {
            List<LikeDto> likeDtos = Mapper.toLikeDtoList(likeRepo.getAllLikesByPostId(postId));

            for(LikeDto l: likeDtos) {
                setLikeUserData(l);
            }

            return new ResponseEntity<>(new ResponseData<>(likeDtos, "success", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void setLikeUserData(LikeDto likeDto) {
        Optional<User> user = userRepo.findById(likeDto.getActorId());
        if(user.isPresent()){
            likeDto.setActorName(user.get().getName());
            Optional<String> image = imagesRepo.getPhotoByEntity(user.get().getId());
            if(image.isPresent()){
                likeDto.setActorImageUrl(image.get());
            }
        }
    }

}
