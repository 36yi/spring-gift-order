package gift.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String createToken(String userId, String role, @Nullable String kakaoAccessToken) {
        Date now = new Date();
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        JwtBuilder builder = Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration));

        if (kakaoAccessToken != null && !kakaoAccessToken.isEmpty()) {
            builder.claim("kakaoAccessToken", kakaoAccessToken);
        }

        return builder.signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public boolean validateToken(String token) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            Key key = Keys.hmacShaKeyFor(keyBytes);

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    public String getKakaoAccessTokenFromToken(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("kakaoAccessToken", String.class);
    }
}