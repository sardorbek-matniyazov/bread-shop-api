package demobreadshop.security;

import demobreadshop.domain.Role;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

@Component
@Slf4j
public class JwtProvider {
    private static final String SECRET_KEY = "my-secure-key-is-banana";
    private static final Long EXPIRE_DATE = 1000L * 60 * 60 * 24 * 7;

    private static final Logger logger = Logger.getLogger(JwtProvider.class.getName());

    public String generateToken(String username, Set<Role> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DATE))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            logger.warning("invalid jwt signature");
        } catch (MalformedJwtException e) {
            logger.warning("invalid jwt token");
        } catch (ExpiredJwtException e) {
            logger.warning("token expired");
        }
        return "unDone";
    }

}
