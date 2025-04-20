package dev.prithwish.ledgerlux.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtProvider {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.access-token.expiration.time}")
    private long accessTokenExpirationTime;
    @Value("${jwt.refresh-token.expiration.time}")
    private long refreshTokenExpirationTime;

    public JwtProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public long getAccessTokenExpirationTime() {
        return accessTokenExpirationTime;
    }

    public String generateAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        GrantedAuthority authority = user.getAuthorities().iterator().next();
        return generateTokenWithUsername(user.getUsername(), authority.getAuthority(), accessTokenExpirationTime);
    }

    public String generateRefreshToken(String username) {
        return generateTokenWithUsername(username, "REFRESH_TOKEN", refreshTokenExpirationTime);
    }

    public String extractSubject(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        Instant expiresAt = jwt.getExpiresAt();
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    private String generateTokenWithUsername(String username, String role, long expirationTime) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofMillis(expirationTime)))
                .subject(username)
                .claim("scope", role).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
