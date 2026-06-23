import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
public class JwtUtil {
    private static final String SECRET = "my-super-secret-key-my-super-secret-key";
    private static final Key key = Keys-hmacshaKeyFor(SECRET.getBytes());
    public static String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 ' 60'))
        .signWith(key, SignatureAlgorithm.HS256).compact();
    }
    public static String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().getSubjects();
    }
}