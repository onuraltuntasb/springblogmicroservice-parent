package com.springblogmicroservice.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwtSecret}")
    private String jwtSigningKey;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    public JwtUtils(){}

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){return extractClaim(token,Claims::getExpiration);}

    public boolean hasClaim(String token, String claimName){
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) !=null;
    }

    public String getString(String name,Claims claims) {
        Object v = claims.get(name);
        return v != null ? String.valueOf(v) : null;
    }

    public String getAuthorityClaim(String token){
        Claims claims = extractAllClaims(token.substring(7));
        String authorities = getString("authorities",claims);
        String st = authorities.substring(12);
        String auth =st.substring(0,st.length() -2);

        return auth;
    }


    public<T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {

        System.out.println("signkey : "+jwtSigningKey );

        return Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();}


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());}

    public Boolean isTokenValid(String token){
        return (!isTokenExpired(token));
    }
}
