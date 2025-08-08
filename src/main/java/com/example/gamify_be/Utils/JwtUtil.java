package com.example.gamify_be.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    final String secretKey = "abcd1234";
    public String createToken(String id){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(id)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            return null;
        }
    }

    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}
