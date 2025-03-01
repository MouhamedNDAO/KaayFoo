package com.inn.cafe.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

    private String secret = "hakunamatata";

    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);

    }
    
    public Date extractExpirationToken(String token){
        return extractClaim(token, Claims::getExpiration);
    }
        
    private <T> T extractClaim(String token, Function<Claims,T> ClaimsResolver) {
        final Claims claims = extractAllClaims(token);
                return  ClaimsResolver.apply(claims);
         
    }
        
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

    } 
    
    private Boolean isTokenExpired(String token){
        return extractExpirationToken(token).before(new Date());
    }

    public String generateToken(String username,String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
