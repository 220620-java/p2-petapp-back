package com.revature.petapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.petapp.auth.Auth;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.models.User;
import com.revature.petapp.services.UserService;

@RestController // puts the @ResponseBody over all methods in the class
@RequestMapping(path = "/users")
public class UserController {
	private UserService userServ;

	public UserController(UserService userServ) {
		this.userServ = userServ;
	}

	// @ResponseBody // this method returns data directly in the body, rather than
	// returning a view
	@GetMapping(path = "/{id}")
	@Auth
	public ResponseEntity<User> getUserById(@PathVariable("id") Integer userId) {
		User user = userServ.getUser(userId);
		if (user != null) {
			// send a 200 status code with the user object as the response body
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		try {
			user = userServ.registerUser(user);
		} catch (UsernameAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@PutMapping(path = "/{id}")
	@Auth
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Integer id) {
		if (user.getId() == id) {
			user = userServ.updateUser(user);
			if (user != null) {
				return ResponseEntity.ok(user);
			} else {
				// TODO this could probably be better
				return ResponseEntity.badRequest().build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}
