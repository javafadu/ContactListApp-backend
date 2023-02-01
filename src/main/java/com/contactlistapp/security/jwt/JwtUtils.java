package com.contactlistapp.security.jwt;


import com.contactlistapp.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component //for injection in other places
public class JwtUtils {

    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${contactlist.app.jwtSecret}") // value injection from application.yml
    private String jwtSecret;

    @Value("${contactlist.app.jwtExpirationMs}")  // value injection from application.yml
    private long jwtExpirationMs;

    // 1- GENERATE : Json Web Token (jwt) generating class
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // authentication.getPrincipal() : currently logged-in user

        // io.jsonwebtoken library standart structure to create a token
        return Jwts.builder()
                .setSubject("" + userDetails.getUser().getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }


    // 2- GET ID FROM TOKEN : which user id generated this token?
    public Long getIdFromJwtToken(String token) {
        String strId = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return Long.parseLong(strId);
    }

    // 3- VALIDATE TOKEN:
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token is expired {}",e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT Token is unsupported {}",e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("JWT Token is malformed {}",e.getMessage());
        } catch (SignatureException e) {
            logger.error("Invalid JWT Signature {}",e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT Token illegal args {}",e.getMessage());
        }

        return false;
    }


}
