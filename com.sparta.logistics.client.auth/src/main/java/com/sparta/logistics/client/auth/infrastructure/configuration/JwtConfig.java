package com.sparta.logistics.client.auth.infrastructure.configuration;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
@Configuration
public class JwtConfig {
    @Bean
    public SecretKey secretKey(@Value("${service.jwt.secret-key}") String secretKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }
}
