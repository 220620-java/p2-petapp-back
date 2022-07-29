package com.revature.petapp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.petapp.PetAppBootApplication;
import com.revature.petapp.auth.JwtConfig;
import com.revature.petapp.exceptions.FailedAuthenticationException;
import com.revature.petapp.models.User;

import io.jsonwebtoken.Jwts;

@SpringBootTest(classes=PetAppBootApplication.class)
public class TokenServiceTest {
	@Autowired
	private JwtConfig jwtConfig;
	@Autowired
	private TokenService tokenServ;
	
	@Test
	public void createTokenSuccess() {
		User user = new User();
		String jws = tokenServ.createToken(user);
		
		assertDoesNotThrow(() -> {
			Jwts.parserBuilder().setSigningKey(jwtConfig.getSigningKey()).build().parseClaimsJws(jws);
		});
	}
	
	@Test
	public void createTokenNullUser() {
		assertEquals("", tokenServ.createToken(null));
	}
	
	@Test
	public void createTokenInvalidUser() {
		assertEquals("", tokenServ.createToken(new User(null, null)));
	}
	
	@Test
	public void validateTokenSuccess() {
		long now = System.currentTimeMillis();
		
		String validToken = Jwts.builder()
				.setId(String.valueOf(1))
				.setSubject("test")
				.claim("role", "User")
				.setIssuer("petapp")
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + jwtConfig.getExpiration()))
				.signWith(jwtConfig.getSigningKey())
				.compact();
		
		assertDoesNotThrow(() -> {
			tokenServ.validateToken(validToken);
		});
	}
	
	@Test
	public void validateTokenInvalidToken() {
		assertThrows(FailedAuthenticationException.class, () -> {
			tokenServ.validateToken("aaaaa");
		});
	}
}
