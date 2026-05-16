//package com.mzp.carrental.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.function.Function;
//
//@Component
//public class JWTUtils {
//
//    private SecretKey Key;
//    private  static  final long EXPIRATION_TIME = 86400000;  //24 hours
//
//    public JWTUtils(){
//        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
//        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
//        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
//    }
//
//    public String generateToken(UserDetails userDetails){
//        return Jwts.builder()
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(Key)
//                .compact();
//    }
//    public  String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
//        return Jwts.builder()
//                .claims(claims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(Key)
//                .compact();
//    }
//
//    public  String extractUsername(String token){
//        return  extractClaims(token, Claims::getSubject);
//    }
//
//    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
//        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
//    }
//
//    public  boolean isTokenValid(String token, UserDetails userDetails){
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    public  boolean isTokenExpired(String token){
//        return extractClaims(token, Claims::getExpiration).before(new Date());
//    }
//
//
//}

package com.mzp.carrental.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtils {

    private final SecretKey Key;
    private static final long EXPIRATION_TIME = 86400000;  // 24 hours = 86400000

    public JWTUtils() {
        String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // Generate Token
    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority()); // Add role
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername()) // Email as subject
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }
//    public String generateToken(UserDetails userDetails, Integer agencyId) {
//        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
//        claims.put("agencyId", agencyId); // ✅ Store agency ID in JWT
//
//        return Jwts.builder()
//                .claims(claims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(Key)
//                .compact();
//    }

    public  String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    // Extract username (email) from token
//    public String extractUsername(String token) {
//        return extractClaims(token, Claims::getSubject);
//    }
//
//    // Extract user role from token
//    public String extractUserRole(String token) {
//        return extractClaims(token, claims -> claims.get("role", String.class));
//    }

    public String extractUsername(String token) {
        try {
            String username = extractClaims(token, Claims::getSubject);
//            System.out.println("🔍 Extracted Username from JWT: " + username);
            return username;
        } catch (Exception e) {
//            System.out.println("❌ Error extracting username from JWT: " + e.getMessage());
            return null;
        }
    }

    public String extractUserRole(String token) {
        try {
            String role = extractClaims(token, claims -> claims.get("role", String.class));
//            System.out.println("🔍 Extracted Role from JWT: " + role);
            return role;
        } catch (Exception e) {
            System.out.println("❌ Error extracting role from JWT: " + e.getMessage());
            return null;
        }
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            if (username == null || !username.equals(userDetails.getUsername())) {
                throw new RuntimeException("JWT username mismatch");
            }
            if (isTokenExpired(token)) {
                throw new RuntimeException("JWT token is expired");
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("JWT validation failed: " + e.getMessage());
        }
    }


    // Generic method to extract claims
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    // Validate Token
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}

