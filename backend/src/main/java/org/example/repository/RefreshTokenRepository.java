package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.exception.ResourceNotFoundException;
import org.example.security.jwt.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    @Autowired
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProperties jwtProperties;

    public void saveToken(String username, String token){
        redisTemplate.opsForValue()
                .set(username, token, jwtProperties.getRefreshValidity(), TimeUnit.SECONDS);
    }

    public String getToken(String username){
        return redisTemplate.opsForValue().get(username);
    }

    public void deleteToken(String username){
        Boolean isDeleted = redisTemplate.delete(username);
        if (Boolean.FALSE.equals(isDeleted)){
            throw new ResourceNotFoundException("Token with key " + username + "not found!");
        }
    }
}
