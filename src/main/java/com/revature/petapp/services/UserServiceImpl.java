package com.revature.petapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.petapp.data.PetRepository;
import com.revature.petapp.data.StatusRepository;
import com.revature.petapp.data.UserRepository;
import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Status;
import com.revature.petapp.models.User;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepo;
	private PetRepository petRepo;
	private StatusRepository statusRepo;
	
	// because this is my only constructor, Spring will do dependency injection
	// automatically when it creates this class in the IoC container
	// and i don't need the Autowired annotation
	public UserServiceImpl(UserRepository userRepo, PetRepository petRepo, StatusRepository statusRepo) {
		this.userRepo = userRepo;
		this.petRepo = petRepo;
		this.statusRepo = statusRepo;
	}

	@Override
	public User registerUser(User user) throws UsernameAlreadyExistsException {
		user.setId(0);
		user = userRepo.save(user);
		if (user.getId()==0) {
			throw new UsernameAlreadyExistsException();
		}
		return user;
	}

	@Override
	public User logIn(String username, String password) {
		User user = userRepo.findByUsername(username);
		if (user != null && (password!=null && password.equals(user.getPassword()))) {
			return user;
		} else {
			return null;
		}
	}

	@Override
	public List<Pet> viewAllPets() {
		Status availableStatus = statusRepo.findByName("Available");
		return petRepo.findByStatus(availableStatus);
	}

	@Override
	@Transactional
	public User adoptPet(Pet pet, User user) throws AlreadyAdoptedException {
		if (user == null || pet == null) {
			return null;
		}
		if ("Adopted".equals(pet.getStatus().getName())) {
			throw new AlreadyAdoptedException();
		}
		
		Status adoptedStatus = statusRepo.findByName("Adopted");
		pet.setStatus(adoptedStatus);
		List<Pet> pets = user.getPets();
		pets.add(pet);
		user.setPets(pets);

		petRepo.save(pet);
		userRepo.save(user);
		
		return user;
	}

	@Override
	public Pet getPet(int id) {
		Optional<Pet> petOpt = petRepo.findById(id);
		
		if (petOpt.isPresent()) {
			return petOpt.get();
		} else return null;
	}

	@Override
	public User getUser(int id) {
		Optional<User> userOpt = userRepo.findById(id);
		
		if (userOpt.isPresent()) {
			return userOpt.get();
		} else return null;
	}

	@Override
	public User updateUser(User user) {
		if (userRepo.findById(user.getId()).isPresent()) {
			userRepo.save(user);
			
			Optional<User> userOpt = userRepo.findById(user.getId());
			if (userOpt.isPresent())
				return userOpt.get();
		}
		return null;
	}
}
