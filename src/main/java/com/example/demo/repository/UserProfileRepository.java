package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.UserProfile;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findByUsername(String username);
}
