package com.springblogmicroservice.service;


import com.springblogmicroservice.entity.RefreshToken;
import com.springblogmicroservice.entity.User;
import com.springblogmicroservice.exception.ResourceNotFoundException;
import com.springblogmicroservice.exception.TokenRefreshException;
import com.springblogmicroservice.repository.RefreshTokenRepository;
import com.springblogmicroservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwtRefreshExpirationSecond}")
    private int refreshTokenDuration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        System.out.println("refreshTokenDurationMs expire when : " + refreshTokenDuration);

        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDuration));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);

        System.out.println("refreshtToken : " + refreshToken);

        return refreshToken;
    }

    public Boolean verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            return false;
        }else{
            return true;
        }
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found in refresh token service!"));
        RefreshToken refreshToken = user.getRefreshToken();
        refreshTokenRepository.delete(refreshToken);
    }
}
