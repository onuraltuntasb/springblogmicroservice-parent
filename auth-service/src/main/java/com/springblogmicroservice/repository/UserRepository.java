package com.springblogmicroservice.repository;

import com.springblogmicroservice.entity.RefreshToken;
import com.springblogmicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String userEmail);
    Optional<User> findByRefreshToken(RefreshToken refreshToken);
}
