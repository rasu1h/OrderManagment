package source.testmodule.Infrastructure.Configurations.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import source.testmodule.Domain.Model.User;
import source.testmodule.Infrastructure.Persitence.Entity.UserJpaEntity;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {


    /**
     * Генерация токена
     *
     * @param userDetails данные пользователя
     * @return функция генерации токена
     */

    public String generateToken(UserDetails userDetails) {

        Map<String,Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserJpaEntityDetails){
            claims.put("id", customUserJpaEntityDetails.getId());
            claims.put("role", customUserJpaEntityDetails.getRole());
        }
        return generateToken(claims,userDetails);
    }
    /***
     * Генерация токена
     * @param extraClaims  дополнительные данные для токена
     * @param userDetails данные пользователя
     * @return токен
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        final Integer  jwtExpirationMs = 86400000; // 24 часа
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Получение ключа для подписи токена из секрета
     *
     * @return ключ
     */
    private Key getSigningKey() {
        final String jwtSecret = "VWygB1YE8ZAPsKi9olMTQM7rP+gMtv5hKR1FFhYK0ohD8JM3qSFg5Hb1RQOME9hbU+zwhswfxjmxIjnsFlvACg";
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public  Map<String,Object> getUserDataFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Валидация данных пользователя из токена
     * @param token
     * @return
     */

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}