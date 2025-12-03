package com.ecommerce.ecommerce_backend.repository;

import com.ecommerce.ecommerce_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    // Find a user by email (used for login, loading current user, etc.)
    Optional<User> findByEmail(String email);

    // Check if a user exists with given email (used in register to prevent duplicates)
    boolean existsByEmail(String email);
}
