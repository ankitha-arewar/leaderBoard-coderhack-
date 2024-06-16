package com.myproject.leaderboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.myproject.leaderboard.entity.User;
import com.myproject.leaderboard.exceptions.InvalidUserDetails;
import com.myproject.leaderboard.exceptions.UserNotFound;
import com.myproject.leaderboard.repo.UserRepository;
import com.myproject.leaderboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() throws InvalidUserDetails {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("User1");
        user.setScore(50L);

        when(userRepository.save(any())).thenReturn(user);
        User savedUser = userService.registerUser(user);
        assertEquals(user.getUserId(), savedUser.getUserId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getScore(), savedUser.getScore());
    }

    @Test
    public void testRegisterUser_withInvalidUserId() {
        User user = new User();
        user.setUserId(0L);
        user.setUsername("User1");
        user.setScore(50L);

        InvalidUserDetails exception = assertThrows(InvalidUserDetails.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("User Id must be greater than 0", exception.getMessage());
    }

    @Test
    public void testRegisterUser_withEmptyUsername() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("");
        user.setScore(50L);

        InvalidUserDetails exception = assertThrows(InvalidUserDetails.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("User Name cannot be empty", exception.getMessage());
    }

    @Test
    public void testRegisterUser_withExistingUserId() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("User1");
        user.setScore(50L);

        when(userRepository.findByUserId(1L)).thenReturn(user);

        InvalidUserDetails exception = assertThrows(InvalidUserDetails.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("User with given Id already exists", exception.getMessage());
    }

    @Test
    public void testGetUserById_UserFound() throws UserNotFound {
        User user = new User(1L, "User1", 50L, new HashSet<>());
        when(userRepository.findByUserId(1L)).thenReturn(user);

        User retrievedUser = userService.getUserById(1L);

        assertNotNull(retrievedUser);
        assertEquals(1L, retrievedUser.getUserId());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findByUserId(1L)).thenReturn(null);

        UserNotFound exception = assertThrows(UserNotFound.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testDeleteById_UserFound() throws UserNotFound {
        User user = new User(1L, "User1", 50L, new HashSet<>());
        when(userRepository.findByUserId(anyLong())).thenReturn(user);
        when(userRepository.deleteByUserId(anyLong())).thenReturn(user);
        User deletedUser = userService.deleteById(anyLong());
        assertNotNull(deletedUser);
        assertEquals(1L, deletedUser.getUserId());
    }

    @Test
    public void testDeleteById_UserNotFound() {
        when(userRepository.findByUserId(1L)).thenReturn(null);

        UserNotFound exception = assertThrows(UserNotFound.class, () -> {
            userService.deleteById(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testUpdateUserById_InvalidScore() {
        User user = new User(1L, "User1", 50L, new HashSet<>());
        when(userRepository.findByUserId(1L)).thenReturn(user);
        long score = 150L;

        InvalidUserDetails exception = assertThrows(InvalidUserDetails.class, () -> {
            userService.updateUserById(1L, score);
        });

        assertEquals("Score must be in the range (0 , 100)", exception.getMessage());
    }

    @Test
    public void testUpdateUserById_UserNotFound() {
        when(userRepository.findByUserId(1L)).thenReturn(null);
        long score = 75L;

        UserNotFound exception = assertThrows(UserNotFound.class, () -> {
            userService.updateUserById(1L, score);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testUpdateUserById_Score60() throws UserNotFound, InvalidUserDetails {
        User user = new User(1L, "User1", 50L, new HashSet<>());
        when(userRepository.findByUserId(1L)).thenReturn(user);
        long score = 60L;
        userService.updateUserById(1L, score);
    }

    @Test
    public void testUpdateUserById_Score30() throws UserNotFound, InvalidUserDetails {
        User user = new User(1L, "User1", 50L, new HashSet<>());
        when(userRepository.findByUserId(1L)).thenReturn(user);
        long score = 30L;
        User updatedUser = userService.updateUserById(1L, score);
    }

    @Test
    public void testUpdateUserById_Score0() throws UserNotFound, InvalidUserDetails {
        User user = new User(1L, "User1", 50L, new HashSet<>());
        when(userRepository.findByUserId(1L)).thenReturn(user);
        long score = 0L;
        User updatedUser = userService.updateUserById(1L, score);
    }

    @Test
    public void testUpdateUserById_Score100() throws UserNotFound, InvalidUserDetails {
        User user = new User(1L, "User1", 50L, new HashSet<>());
        when(userRepository.findByUserId(1L)).thenReturn(user);
        long score = 100L;
        User updatedUser = userService.updateUserById(1L, score);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1L, "User1", 50L, new HashSet<>());
        User user2 = new User(2L, "User2", 60L, new HashSet<>());
        when(userRepository.findAllByOrderByUserIdAsc()).thenReturn(List.of(user1, user2));
        assertEquals(2, userService.getAllUsers().size());
        assertEquals(1L, userService.getAllUsers().get(0).getUserId());
        assertEquals(2L, userService.getAllUsers().get(1).getUserId());
    }

}
