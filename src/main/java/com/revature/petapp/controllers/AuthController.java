package com.revature.petapp.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.petapp.models.User;
import com.revature.petapp.models.dtos.UserDTO;
import com.revature.petapp.services.TokenService;
import com.revature.petapp.services.UserService;

@RestController
@RequestMapping(path="/auth")
public class AuthController {
	private UserService userServ;
	private TokenService tokenServ;
	
	public AuthController(UserService userServ, TokenService tokenServ) {
		this.userServ = userServ;
		this.tokenServ = tokenServ;
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> logIn(@RequestBody Map<String, String> credentials) {
		String username = credentials.get("username");
		String password = credentials.get("password");
		
		User user = userServ.logIn(username, password);
		
		if (user!=null) {
			UserDTO userDto = new UserDTO(user);
			String jws = tokenServ.createToken(user);
			return ResponseEntity.status(200).header("Auth", jws).body(userDto);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
