package com.revature.petapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.revature.petapp.PetAppBootApplication;
import com.revature.petapp.data.PetRepository;
import com.revature.petapp.data.StatusRepository;
import com.revature.petapp.data.UserRepository;
import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Status;
import com.revature.petapp.models.User;

@SpringBootTest(classes=PetAppBootApplication.class)
public class UserServiceTest {
	@MockBean
	private UserRepository userRepo;
	@MockBean
	private PetRepository petRepo;
	@MockBean
	private StatusRepository statusRepo;
	
	@Autowired
	private UserService userServ;
	
	@Test
	public void registerSuccessfully() throws UsernameAlreadyExistsException, SQLException {
		// setup
		User mockUser = new User();
		User mockUserWithId = new User();
		mockUserWithId.setId(10);
		Mockito.when(userRepo.save(mockUser)).thenReturn(mockUserWithId);

		// call method
		User returnedUser = userServ.registerUser(mockUser);

		// assertion
		assertNotNull(returnedUser);
	}

	@Test
	public void registerUsernameAlreadyExists() throws SQLException {
		User mockUser = new User();
		mockUser.setUsername("sierra");
		Mockito.when(userRepo.save(mockUser)).thenReturn(mockUser);

		assertThrows(UsernameAlreadyExistsException.class, () -> {
			userServ.registerUser(mockUser);
		});
	}

	@Test
	public void logInSuccessfully() {
		// setup (inputs, mocks, etc.)
		String username = "test";
		String password = "test";

		User mockUser = new User(username, password);
		Mockito.when(userRepo.findByUsername(username)).thenReturn(mockUser);

		// call the method that we're testing
		User returnedUser = userServ.logIn(username, password);

		// assertion (checking for expected behavior)
		assertEquals(username, returnedUser.getUsername());
	}

	@Test
	public void logInUsernameDoesntExist() {
		// setup (inputs, mocks, etc.)
		String username = "wrong";
		String password = "test";

		Mockito.when(userRepo.findByUsername(username)).thenReturn(null);

		// call the method that we're testing
		User returnedUser = userServ.logIn(username, password);

		// assertion (checking for expected behavior)
		assertNull(returnedUser);
	}

	@Test
	public void logInWrongPassword() {
		// setup (inputs, mocks, etc.)
		String username = "test";
		String password = "wrong";

		User mockUser = new User(username, "test");
		Mockito.when(userRepo.findByUsername(username)).thenReturn(mockUser);

		// call the method that we're testing
		User returnedUser = userServ.logIn(username, password);

		// assertion (checking for expected behavior)
		assertNull(returnedUser);
	}

	@Test
	public void logInNullPassword() {
		// setup (inputs, mocks, etc.)
		String username = "test";
		String password = null;

		User mockUser = new User(username, "test");
		Mockito.when(userRepo.findByUsername(username)).thenReturn(mockUser);

		// call the method that we're testing
		User returnedUser = userServ.logIn(username, password);

		// assertion (checking for expected behavior)
		assertNull(returnedUser);
	}

	@Test
	public void viewAvailablePets() {
		// setup
		Mockito.when(statusRepo.findByName("Available")).thenReturn(new Status());
		Mockito.when(petRepo.findByStatus(new Status())).thenReturn(new ArrayList<>());

		// call method
		List<Pet> returnedPets = userServ.viewAllPets();

		// assertion
		assertNotNull(returnedPets);
	}

	@Test
	public void adoptPetSuccessfully() throws AlreadyAdoptedException {
		// setup
		Pet mockPet = new Pet();
		User mockUser = new User();

		Mockito.when(petRepo.save(mockPet)).thenReturn(mockPet);
		Mockito.when(userRepo.save(mockUser)).thenReturn(mockUser);
		
		Mockito.when(statusRepo.findByName("Adopted")).thenReturn(new Status(2, "Adopted"));

		// call method
		User returnedUser = userServ.adoptPet(mockPet, mockUser);

		// assertion
		List<Pet> userPets = returnedUser.getPets();
		assertTrue(userPets.size() > 0 && "Adopted".equals(userPets.get(0).getStatus().getName()));
	}

	@Test
	public void adoptPetAlreadyAdopted() {
		Pet mockPet = new Pet();
		User mockUser = new User();

		mockPet.setStatus(new Status(2, "Adopted"));

		assertThrows(AlreadyAdoptedException.class, () -> {
			userServ.adoptPet(mockPet, mockUser);
		});
	}

	@Test
	public void adoptPetNullPet() throws AlreadyAdoptedException {
		// setup
		Pet mockPet = null;
		User mockUser = new User();

		// call method
		User returnedUser = userServ.adoptPet(mockPet, mockUser);
		
		// assertion
		assertNull(returnedUser);
	}

	@Test
	public void adoptPetNullUser() throws AlreadyAdoptedException {
		// setup
		Pet mockPet = new Pet();
		User mockUser = null;

		// call method
		User returnedUser = userServ.adoptPet(mockPet, mockUser);
		
		// assertion
		assertNull(returnedUser);
	}
	
	@Test
	public void getPetSuccess() {
		Pet mockPet = new Pet();
		int id = 1;
		
		mockPet.setId(id);
		Mockito.when(petRepo.findById(id)).thenReturn(Optional.of(mockPet));
		
		assertEquals(mockPet, userServ.getPet(id));
	}
	
	@Test
	public void getPetDoesNotExist() {
		Pet mockPet = new Pet();
		int id = 1;
		
		mockPet.setId(id);
		Mockito.when(petRepo.findById(id)).thenReturn(Optional.empty());
		
		assertNull(userServ.getPet(id));
	}
	
	@Test
	public void getUserSuccess() {
		User mockUser = new User();
		int id = 1;
		
		mockUser.setId(id);
		Mockito.when(userRepo.findById(id)).thenReturn(Optional.of(mockUser));
		
		assertEquals(mockUser, userServ.getUser(id));
	}
	
	@Test
	public void getUserDoesNotExist() {
		User mockUser = new User();
		int id = 1;
		
		mockUser.setId(id);
		Mockito.when(userRepo.findById(id)).thenReturn(Optional.empty());
		
		assertNull(userServ.getUser(id));
	}
	
	@Test
	public void updateUserSuccess() {
		User mockUser = new User();
		
		Mockito.when(userRepo.findById(0)).thenReturn(Optional.of(mockUser));
		Mockito.when(userRepo.save(mockUser)).thenReturn(mockUser);
		
		assertEquals(mockUser, userServ.updateUser(mockUser));
	}
	
	@Test
	public void updateUserDoesNotExist() {
		User mockUser = new User();
		
		Mockito.when(userRepo.findById(0)).thenReturn(Optional.empty());
		
		assertNull(userServ.updateUser(mockUser));
	}
	
	@Test
	public void updateUserSomethingWrong() {
		User mockUser = new User();
		
		Mockito.when(userRepo.findById(0))
			.thenReturn(Optional.of(mockUser))
			.thenReturn(Optional.empty());
		
		assertNull(userServ.updateUser(mockUser));
	}
}
