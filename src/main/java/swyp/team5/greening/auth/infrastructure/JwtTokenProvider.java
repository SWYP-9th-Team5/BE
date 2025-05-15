package swyp.team5.greening.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swyp.team5.greening.auth.exception.AuthException;
import swyp.team5.greening.auth.exception.AuthExceptionMessage;
import swyp.team5.greening.auth.property.JwtProperty;
import swyp.team5.greening.auth.provider.TokenProvider;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

    private final JwtProperty jwtProperty;

    private String generateToken(
            Long id,
            long expireTime
    ) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireTime);
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.getClientSecret());

        Claims claims = Jwts.claims().setSubject(String.valueOf(id));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                .compact();
    }

    private Jws<Claims> getClaims(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.getClientSecret());

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthExceptionMessage.AUTH_TOKEN_EXPIRED);
        } catch (MalformedJwtException me) {
            throw new AuthException(AuthExceptionMessage.AUTH_TOKEN_MALFORMED);
        } catch (UnsupportedJwtException ue) {
            throw new AuthException(AuthExceptionMessage.AUTH_TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException ie) {
            throw new AuthException(AuthExceptionMessage.AUTH_TOKEN_ILLEGAL);
        } catch (SignatureException se) {
            throw new AuthException(AuthExceptionMessage.AUTH_TOKEN_NOT_SIGNATURE);
        }
    }

    //Jwt 토큰 생성
    @Override
    public String createToken(Long id) {
        long accessExpiryMinute = jwtProperty.getAccessExpiryTime() * 1000 * 60;

        return generateToken(id, accessExpiryMinute);
    }

    //Jwt 토큰 파싱
    @Override
    public Long getPayLoad(String token) {
        Jws<Claims> claims = getClaims(token);
        return Long.parseLong(claims.getBody().getSubject());
    }
}
