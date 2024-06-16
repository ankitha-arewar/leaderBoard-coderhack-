package com.myproject.leaderboard.repo;

import com.myproject.leaderboard.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, Long> {
    User findByUserId(Long userId);
    User deleteByUserId(Long userId);
    List<User> findAllByOrderByUserIdAsc();

}
