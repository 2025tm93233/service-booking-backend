package com.servicebooking.repository;

import com.servicebooking.model.User;
import com.servicebooking.model.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndRole(String email, UserRole role);

    List<User> findByRole(UserRole role);
}
