package com.revature.petapp.auth;

import java.security.Key;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtConfig {
	private String salt = System.getenv("JWT_SECRET");
	
	private int expiration = 86400000;
	
	// i'm using RS256 because it's widely supported. this algorithm
	// requires a 540-character (2048 bit) key.
	private final SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;
	private Key signingKey;
	
	// this annotation means that the method should be
	// executed after dependency injection is complete
	@PostConstruct
	public void createKey() {
		byte[] saltyBytes = DatatypeConverter.parseBase64Binary(salt);
		signingKey = new SecretKeySpec(saltyBytes, sigAlg.getJcaName());
	}
	
	public int getExpiration() {
		return this.expiration;
	}
	
	public SignatureAlgorithm getSigAlg() {
		return this.sigAlg;
	}
	
	public Key getSigningKey() {
		return this.signingKey;
	}
}
