package com.myproject.leaderboard.controller;

import com.myproject.leaderboard.dto.ResponseDto;
import com.myproject.leaderboard.entity.User;
import com.myproject.leaderboard.exceptions.InvalidUserDetails;
import com.myproject.leaderboard.exceptions.UserNotFound;
import com.myproject.leaderboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody User user) throws InvalidUserDetails {
        try{
            User user1 = userService.registerUser(user);
            ResponseDto responseDto = new ResponseDto("User registered successfully", 200, user1);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }catch (InvalidUserDetails e){
            ResponseDto responseDto = new ResponseDto(e.getMessage(), 400, null);
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDto> getUserById(@PathVariable Long userId) {
        try{
            if(userId <= 0) throw new InvalidUserDetails("User not found");
            User user = userService.getUserById(userId);
            ResponseDto responseDto = new ResponseDto("Success", 200, user);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (UserNotFound | InvalidUserDetails e) {
            return new ResponseEntity<>(new ResponseDto("User not found", 400, null), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ResponseDto> deleteUserById(@PathVariable Long userId) {
        try{
            User user = userService.deleteById(userId);
            ResponseDto responseDto = new ResponseDto("User deleted successfully", 200, user);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (UserNotFound e) {
            return new ResponseEntity<>(new ResponseDto("User not found", 400, null), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<ResponseDto> updateUserById(@PathVariable Long userId, @RequestBody Long score){
        try{
            User user = userService.updateUserById(userId, score);
            return new  ResponseEntity<>(new ResponseDto("Updated user successfully", 200, user), HttpStatus.OK);
        }catch (UserNotFound | InvalidUserDetails e){
            return new  ResponseEntity<>(new ResponseDto(e.getMessage(), 400, null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsersSorted(){
        return userService.getAllUsers();
    }

}
