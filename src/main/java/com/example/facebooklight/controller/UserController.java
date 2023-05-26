package com.example.facebooklight.controller;

import com.example.facebooklight.FileUploadService;
import com.example.facebooklight.dto.UserDto;
import com.example.facebooklight.mapper.Mapper;
import com.example.facebooklight.model.Photos;
import com.example.facebooklight.model.ResponseData;
import com.example.facebooklight.model.User;
import com.example.facebooklight.repo.ImagesRepo;
import com.example.facebooklight.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController()
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ImagesRepo imagesRepo;

    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseData<List<User>>> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();
            userRepo.findAll().forEach(users::add);

            if (users.isEmpty()) {
                return new ResponseEntity<>(new ResponseData<>(users, "no data found", HttpStatus.NO_CONTENT.value()), HttpStatus.OK);
            }

            return new ResponseEntity<>(new ResponseData<>(users, "success", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<ResponseData<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepo.findById(id);

        if (user.isPresent()) {
            UserDto userDto = Mapper.toUserDto(user.get());
            Optional<String> photoUrl = imagesRepo.getPhotoByEntity(user.get().getId());
            if (photoUrl.isPresent()) {
                userDto.setPhoto_url(photoUrl.get());
            }
            return new ResponseEntity<>(new ResponseData<>(userDto, "success", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(null, "no user found with this id", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        }

    }

    @PostMapping(value = "/signUp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<UserDto>> signUp(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String password,
            @RequestParam(required = false) MultipartFile picture
    ) {
        try {
            if(userRepo.getUserByEmail(email).isPresent()){
                return new ResponseEntity<>(new ResponseData<>(null, "User with this email is already exists", HttpStatus.BAD_REQUEST.value()), HttpStatus.OK);
            }

            UserDto usr = Mapper.toUserDto(userRepo.save(new User(name,email,phone,address,password)));
            if(picture!=null){
                try {
                    Photos p = imagesRepo.save(new Photos(usr.getId(), FileUploadService.fileUpload(picture)));
                    usr.setPhoto_url(p.getPhotoUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return new ResponseEntity<>(new ResponseData<>(usr, "registration successful", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping(value = "/uploadProfilePicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<UserDto>> uploadProfilePicture(
            @RequestHeader Map<String, String> headers,
            @RequestParam MultipartFile picture
    ) {
        try {
            Optional<User> user = userRepo.findById(Long.parseLong(headers.get("user_id")));

            if(user.isPresent()) {
                UserDto userDto = Mapper.toUserDto(user.get());
                Photos p = imagesRepo.save(new Photos(userDto.getId(), FileUploadService.fileUpload(picture)));
                userDto.setPhoto_url(p.getPhotoUrl());
                return new ResponseEntity<>(new ResponseData<>(userDto, "registration successful", HttpStatus.OK.value()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseData<>(null, "user not found", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseData<>(null, e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/signIn")
    public ResponseEntity<ResponseData<UserDto>> signIn(@RequestParam String email, @RequestParam String password) {
        Optional<User> usr = userRepo.signIn(email, password);
        if (usr.isPresent()) {
            UserDto userDto = Mapper.toUserDto(usr.get());
            Optional<String> photoUrl = imagesRepo.getPhotoByEntity(usr.get().getId());
            if (photoUrl.isPresent()) {
                userDto.setPhoto_url(photoUrl.get());
            }
            return new ResponseEntity<>(new ResponseData<>(userDto, "success", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(null, "username or password is wrong. try again", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        }

    }

    @PostMapping("/updateUserById/{id}")
    public ResponseEntity<ResponseData<User>> updateUser(@PathVariable Long id,
                                                         @RequestBody User newUserData) {
        Optional<User> oldUser = userRepo.findById(id);
        if (oldUser.isPresent()) {
            newUserData.setId(oldUser.get().getId());
            User user = userRepo.save(newUserData);
            return new ResponseEntity<>(new ResponseData<>(user, "success", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(null, "user not found", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<ResponseData<HttpStatus>> deleteUserById(@PathVariable Long id) {
        Optional<User> oldUser = userRepo.findById(id);
        if (oldUser.isPresent()) {
            userRepo.delete(oldUser.get());
            return new ResponseEntity<>(new ResponseData<>(null, "username successfully deleted", HttpStatus.OK.value()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseData<>(null, "user not found", HttpStatus.NOT_FOUND.value()), HttpStatus.OK);
        }
    }
}
