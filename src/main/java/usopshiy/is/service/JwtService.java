package usopshiy.is.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import usopshiy.is.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Date;
import java.security.Key;

/**
 * Сервис для работы с JWT (JSON Web Tokens).
 * Предоставляет функционал для генерации, валидации и извлечения информации
 * из JWT-токенов, используемых для аутентификации и авторизации пользователей.
 */
@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /**
     * Извлекает имя пользователя (subject) из JWT-токена.
     *
     * @param token JWT-токен
     * @return имя пользователя, извлеченное из токена
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Генерирует JWT-токен на основе данных пользователя.
     * Для объектов типа User дополнительно добавляет в claims поля id и role.
     *
     * @param userDetails объект UserDetails с информацией о пользователе
     * @return сгенерированный JWT-токен
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    /**
     * Проверяет валидность JWT-токена для указанного пользователя.
     * Токен считается валидным, если имя пользователя в токене совпадает
     * с именем пользователя в UserDetails и токен не истек.
     *
     * @param token JWT-токен для проверки
     * @param userDetails объект UserDetails с информацией о пользователе
     * @return true, если токен валиден, иначе false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Извлекает конкретное поле (claim) из JWT-токена с помощью функции-резолвера.
     *
     * @param token JWT-токен
     * @param claimsResolvers функция для извлечения конкретного поля из Claims
     * @param <T> тип извлекаемого значения
     * @return значение указанного поля
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерирует JWT-токен с дополнительными полями (claims).
     *
     * @param extraClaims дополнительные поля для включения в токен
     * @param userDetails объект UserDetails с информацией о пользователе
     * @return сгенерированный JWT-токен
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Проверяет, истек ли срок действия JWT-токена.
     *
     * @param token JWT-токен для проверки
     * @return true, если токен истек, иначе false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия токена.
     *
     * @param token JWT-токен
     * @return дата истечения срока действия
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает все claims (поля) из JWT-токена.
     *
     * @param token JWT-токен
     * @return объект Claims со всеми полями токена
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Формирует ключ для подписи JWT на основе секретного ключа из конфигурации.
     *
     * @return ключ для подписи JWT
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}