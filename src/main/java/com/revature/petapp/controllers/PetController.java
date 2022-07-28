package com.revature.petapp.controllers;

import java.util.List;

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
import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.User;
import com.revature.petapp.services.AdminService;
import com.revature.petapp.services.UserService;

// in spring mvc, the controller stereotype
// will specify a controller that returns views
// however, that isn't RESTful
//@Controller

// this is a specialization of Controller that
// implicitly places @ResponseBody over each
// handler method. this just states that none
// of the methods will return views
@RestController
@RequestMapping(path = "/pets")
public class PetController {
	private UserService userServ;
	private AdminService adminServ;

	public PetController(UserService userServ, AdminService adminServ) {
		this.userServ = userServ;
		this.adminServ = adminServ;
	}

	// @ResponseBody tells Spring that this method
	// is NOT returning a view, it is just returning
	// data that should go directly into the HTTP
	// response body
	// we don't need to put this manually because we
	// are using the @RestController stereotype
	// @ResponseBody
	// @RequestMapping(method=RequestMethod.GET, path="/pets")
	@GetMapping
	public ResponseEntity<List<Pet>> viewAllPets() {
		List<Pet> pets = userServ.viewAllPets();
		// return ResponseEntity.status(200).body(pets);
		// return ResponseEntity.status(HttpStatus.OK).body(pets);
		return ResponseEntity.ok(pets);
	}

	// if the path variable has the same name as
	// a method parameter, we only need to use the
	// @PathVariable annotation without specifying the name.
	// otherwise, see updatePet method
	@GetMapping(path = "/{id}")
	public ResponseEntity<Pet> getPetById(@PathVariable Integer id) {
		Pet pet = userServ.getPet(id);

		if (pet != null) {
			// OK sets status code to 200
			return ResponseEntity.ok(pet);
		} else {
			// notFound sets status code to 404
			return ResponseEntity.notFound().build();
		}
	}

	// @RequestBody allows us to get the body of the
	// HTTP request parsed to a Java object
	@PostMapping
	@Auth(requiredRole = "admin")
	public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
		pet = adminServ.addPet(pet);
		return ResponseEntity.status(HttpStatus.CREATED).body(pet);
	}

	@PutMapping(path = "/{petId}")
	@Auth(requiredRole = "admin")
	public ResponseEntity<Pet> updatePet(@RequestBody Pet pet, @PathVariable("petId") Integer id) {
		if (pet.getId() == id) {
			pet = adminServ.editPet(pet);
			return ResponseEntity.ok(pet);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@PutMapping(path = "/{petId}/adopt/{userId}")
	@Auth
	public ResponseEntity<User> adoptPet(@RequestBody Pet pet, @PathVariable Integer petId,
			@PathVariable Integer userId) {
		User user = userServ.getUser(userId);
		if (pet != null && user != null) {
			if (pet.getId() == petId) {
				try {
					user = userServ.adoptPet(pet, user);
					return ResponseEntity.ok(user);
				} catch (AlreadyAdoptedException e) {
					return ResponseEntity.status(HttpStatus.CONFLICT).build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}
	}
}
