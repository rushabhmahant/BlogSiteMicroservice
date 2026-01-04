package com.blogsite.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

/*import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;*/

@Component
public class JwtUtil {

	/*private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private final long expiration = 1000 * 60 * 60; // 1 hour

	public String generateToken(String userEmail) {
		return Jwts.builder().setSubject(userEmail).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(key).compact();
	}

	public String validateTokenAndGetEmail(String token) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			return claims.getSubject();
		} catch (JwtException e) {
			return null; // invalid token
		}
	}*/

}
