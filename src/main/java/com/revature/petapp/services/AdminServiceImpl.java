package com.revature.petapp.services;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.petapp.data.PetRepository;
import com.revature.petapp.models.Pet;

@Service
public class AdminServiceImpl implements AdminService {
	// field injection: very readable, but requires reflection
	//@Autowired
	private PetRepository petRepo;
	
	// constructor injection: mostly readable, easy to include multiple dependencies
	// in one constructor, you can't create the object if you don't have the dependencies,
	// can't change the values later
	//@Autowired // if you only have one constructor, you don't even need the Autowired annotation
	public AdminServiceImpl(PetRepository petRepo) {
		this.petRepo = petRepo;
	}

	@Override
	public Pet addPet(Pet pet) {
		pet.setId(0);
		pet = petRepo.save(pet);
		if (pet.getId()!=0) {
			return pet;
		}
		return null;
	}

	@Override
	public Pet editPet(Pet pet) {
		if (petRepo.findById(pet.getId()).isPresent()) {
			petRepo.save(pet);
			
			Optional<Pet> petOpt = petRepo.findById(pet.getId());
			if (petOpt.isPresent())
				return petOpt.get();
		}
		return null;
	}

}
