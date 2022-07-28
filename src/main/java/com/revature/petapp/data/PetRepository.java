package com.revature.petapp.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Status;

// set up Spring Data JPA:
// 1. map our models using the JPA
// 2. create the data access interfaces:
@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
	// JpaRepository provides the basic CRUD methods
	// if we want to add extra, custom ones, Spring Data JPA will implement those too
	// as long as we follow the right naming convention
	public List<Pet> findByStatus(Status status);
	
	// you can customize these a lot as long as you know the keywords
	public List<Pet> findBySpeciesNameContainingOrderByName(String name);
}
