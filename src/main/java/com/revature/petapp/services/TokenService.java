package com.revature.petapp.services;

import java.util.Optional;

import com.revature.petapp.exceptions.FailedAuthenticationException;
import com.revature.petapp.exceptions.TokenExpirationException;
import com.revature.petapp.models.User;
import com.revature.petapp.models.dtos.UserDTO;

public interface TokenService {
	public String createToken(User user);
	public Optional<UserDTO> validateToken(String token) throws FailedAuthenticationException, TokenExpirationException;
	public int getDefaultExpiration();
}
