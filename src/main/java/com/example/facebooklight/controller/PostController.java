package com.example.facebooklight.controller;

import com.example.facebooklight.FileUploadService;
import com.example.facebooklight.dto.PostDto;
import com.example.facebooklight.mapper.Mapper;
import com.example.facebooklight.model.Photos;
import com.example.facebooklight.model.Post;
import com.example.facebooklight.model.ResponseData;
import com.example.facebooklight.model.User;
import com.example.facebooklight.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController()
public class PostController {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private ImagesRepo imagesRepo;

    @PostMapping(value = "/addPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<PostDto>> addPost(
            @RequestHeader Map<String, String> headers,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(value = "post_image",required = false) MultipartFile postImage
    ) {
        try {
            PostDto post = Mapper.toPostDto(postRepo.save(new Post(Long.parseLong(headers.get("user_id")), title, content)));

            if(postImage!=null){
                try {
                    Photos p = imagesRepo.save(new Photos(post.getId(), FileUploadService.fileUpload(postImage)));
                    post.setPostImageUrl(p.getPhotoUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new ResponseEntity<>(new ResponseData<>(post, "post successful", HttpStatus.OK.value()), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getPostById/{postId}")
    public ResponseEntity<ResponseData<PostDto>> getPostById(@RequestHeader Map<String, String> headers, @PathVariable Long postId) {
        try {
            Optional<Post> post = postRepo.findById(postId);

            if (post.isPresent()) {
                PostDto postDto = Mapper.toPostDto(post.get());
                return new ResponseEntity<>(new ResponseData<>(postDto, "success", HttpStatus.OK.value()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseData<>(null, "no post found with this id", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllPost")
    public ResponseEntity<ResponseData<List<PostDto>>> getAllPosts(
            @RequestHeader Map<String, String> headers,
            @RequestParam(required = false) Long userId
    ) {
        try {
            List<Post> postList;
            if(userId==null){
                postList = postRepo.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
            }else {
                postList = postRepo.getAllPostByUserId(userId);
            }

            List<PostDto> posts = preparePostWithLikesAndComments(Long.parseLong(headers.get("user_id")), Mapper.toPostDtoList(postList));
            return new ResponseEntity<>(new ResponseData<>(posts, "success", HttpStatus.OK.value()), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, "internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<PostDto> preparePostWithLikesAndComments(Long userId, List<PostDto> postDtos) {
        for (PostDto p : postDtos) {
            p.setLikesCount(likeRepo.getAllLikesByPostId(p.getId()).size());
            p.setCommentsCount(commentRepo.getAllCommentsByPostId(p.getId()).size());
            if (likeRepo.getLikeByLikerAndPostId(userId, p.getId()).isPresent()) {
                p.setLiked(true);
            }

            User user = userRepo.getById(p.getActorId());
            p.setActorName(user.getName());

            if (imagesRepo.getPhotoByEntity(user.getId()).isPresent()) {
                p.setActorImageUrl(imagesRepo.getPhotoByEntity(user.getId()).get());
            }

            Optional<String> photoUrl = imagesRepo.getPhotoByEntity(p.getId());
            photoUrl.ifPresent(p::setPostImageUrl);

        }
        return postDtos;
    }

    @PostMapping("/updatePostById/{id}")
    public ResponseEntity<ResponseData<Post>> updatePost(@RequestHeader Map<String, String> headers,
                                                         @PathVariable Long id,
                                                         @RequestBody Post newPost) {
        Optional<Post> oldPost = postRepo.findById(id);
        if (oldPost.isPresent()) {
            newPost.setId(oldPost.get().getId());
            Post post = postRepo.save(newPost);
            return new ResponseEntity<>(new ResponseData<>(post, "success", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(null, "post not found", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<ResponseData<HttpStatus>> deletePostById(@RequestHeader Map<String, String> headers,
                                                                   @PathVariable Long id) {
        Optional<Post> post = postRepo.findById(id);
        if (post.isPresent()) {
            postRepo.delete(post.get());
            return new ResponseEntity<>(new ResponseData<>(null, "post successfully deleted", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(null, "post not found", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        }
    }
}
