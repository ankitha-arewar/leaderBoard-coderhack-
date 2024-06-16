package com.myproject.leaderboard.service;

import com.myproject.leaderboard.entity.User;
import com.myproject.leaderboard.enums.Badge;
import com.myproject.leaderboard.exceptions.InvalidUserDetails;
import com.myproject.leaderboard.exceptions.UserNotFound;
import com.myproject.leaderboard.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) throws InvalidUserDetails{
        if(user.getUserId() <= 0){
            throw new InvalidUserDetails("User Id must be greater than 0");
        }
        if(user.getUsername() == null || user.getUsername().isEmpty()){
            throw new InvalidUserDetails("User Name cannot be empty");
        }
        if(userRepository.findByUserId(user.getUserId()) != null){
            throw new InvalidUserDetails("User with given Id already exists");
        }
        User user1 = new User();
        user1.setUserId(user.getUserId());
        user1.setUsername(user.getUsername());
        user1.setScore(0L);
        user1.setBadges(new HashSet<>());
        return userRepository.save(user1);
    }

    public User getUserById(Long userId) throws UserNotFound {
        User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new UserNotFound("User not found");
        }
        return user;
    }

    public User deleteById(Long userId) throws UserNotFound {
        User user = getUserById(userId);
        return userRepository.deleteByUserId(user.getUserId());
    }

    public User updateUserById(Long userId, Long score) throws UserNotFound, InvalidUserDetails {
        User user = getUserById(userId);
        User user1 = userRepository.findByUserId(userId);
        if(score < 0 || score > 100) throw new InvalidUserDetails("Score must be in the range (0 , 100)");
        HashSet<Badge> badges = user.getBadges();
        if(score>=60){
            badges.add(Badge.CODE_MASTER);
            badges.add(Badge.CODE_CHAMP);
            badges.add(Badge.CODE_NINJA);
        }else if(score>=30){
            badges.add(Badge.CODE_CHAMP);
            badges.add(Badge.CODE_NINJA);
            badges.remove(Badge.CODE_MASTER);
        }else if(score > 0) {
            badges.add(Badge.CODE_NINJA);
            badges.remove(Badge.CODE_MASTER);
            badges.remove(Badge.CODE_CHAMP);
        }else{
            badges.clear();
        }
        user1.setScore(score);
        user1.setBadges(badges);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByUserIdAsc();
    }

}
