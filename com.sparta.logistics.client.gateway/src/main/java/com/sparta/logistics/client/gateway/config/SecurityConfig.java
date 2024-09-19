package com.sparta.logistics.client.gateway.config;

import com.sparta.logistics.client.gateway.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Base64;
@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${service.jwt.secret-key}") // Base64 Encode 한 SecretKey
    private String secretKeyString;
    private final RedisService redisService;

    public SecurityConfig(RedisService redisService) {
        this.redisService = redisService;
    }
    private static final String[] RESOURCE_WHITELIST = {
            "/v3/**", // v3 : SpringBoot 3(없으면 swagger 예시 api 목록 제공)
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",
    };
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF 비활성화
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterBefore(jwtAuthenticationFilter(redisService), SecurityWebFiltersOrder.HTTP_BASIC);
        return http.build();
    }
//    .sessionManagement(session -> session
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless Session
    @Bean
    public WebFilter jwtAuthenticationFilter(RedisService redisService) {
        // TODO: 게이트웨이 jwt 인증 처리 필터
        return (exchange, chain) -> {

            // /auth/login 경로는 필터를 적용하지 않음
            if (exchange.getRequest().getURI().getPath().equals("/auth/signin")||exchange.getRequest().getURI().getPath().equals("/auth/signup")) {
                return chain.filter(exchange);
            }

            HttpHeaders headers = exchange.getRequest().getHeaders();
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7).trim();
                log.info(token);
                try {
                    byte[] bytes = Base64.getDecoder().decode(secretKeyString);
                    var secretKey = Keys.hmacShaKeyFor(bytes);
                    Claims claims = Jwts.parser()
                            .verifyWith(secretKey)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();
                    String username = claims.get("username", String.class);
                    String Role = claims.get("role", String.class);
                    Long userId = claims.get("user_id", Long.class);

                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-User-Name", username)
                            .header("X-User-Id", String.valueOf(userId))// 사용자명 헤더 추가
                            .header("X-User-Roles", String.valueOf(redisService.getValue(username)))  // 권한 정보를 claims에서 직접 가져옵니다
                            .build();

                    // 수정된 요청으로 필터 체인 계속 처리
                    ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                    return chain.filter(modifiedExchange);

                    // 추가적인 JWT 처리 로직을 넣을 수 있음
                } catch (MalformedJwtException e) {
                    log.error("Malformed JWT Token", e);
                    return Mono.error(new RuntimeException("Malformed JWT Token"));
                } catch (ExpiredJwtException e) {
                    log.error("Expired JWT Token", e);
                    return Mono.error(new RuntimeException("Expired JWT Token"));
                } catch (Exception e) {
                    log.error("JWT Token Error", e);
                    return Mono.error(new RuntimeException("Invalid JWT Token"));
                }
            }

            return chain.filter(exchange);
        };
    }
}
