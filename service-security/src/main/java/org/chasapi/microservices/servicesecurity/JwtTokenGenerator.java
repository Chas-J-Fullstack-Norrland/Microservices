package org.chasapi.microservices.servicesecurity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

public class JwtTokenGenerator {

    private static final String SECRET ="THISSHOULDBEANUNSEENSEECRETUNSEENBYALLSECRET";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generateToken(String subject,String type,String role){
        try{
            return Jwts.builder()
                    .subject(subject)
                    .claim("type",type)
                    .claim("role",role)
                    .issuedAt(new Date())
                    .expiration(new Date(
                            System.currentTimeMillis() + 1800000
                    ))
                    .signWith(key)
                    .compact();
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }

    }

    public static boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e){
            System.err.println(e.getMessage());
            return false;
        }

    }




}
