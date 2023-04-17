package com.springblogmicroservice.repository;


import com.springblogmicroservice.entity.RefreshToken;
import com.springblogmicroservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteById(Long id);
}
