package com.revature.petapp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.revature.petapp.PetAppBootApplication;
import com.revature.petapp.data.PetRepository;
import com.revature.petapp.models.Pet;

@SpringBootTest(classes=PetAppBootApplication.class)
public class AdminServiceTest {
	@MockBean
	private PetRepository petRepo;
	
	@Autowired
	private AdminService adminServ;
	
	@Test
	public void addPetSuccess() {
		Pet mockPet = new Pet();
		Pet mockPetWithId = new Pet();
		mockPetWithId.setId(10);
		
		Mockito.when(petRepo.save(mockPet)).thenReturn(mockPetWithId);
		
		assertEquals(mockPetWithId, adminServ.addPet(mockPet));
	}
	
	@Test
	public void addPetSomethingWrong() {
		Pet mockPet = new Pet();
		
		Mockito.when(petRepo.save(mockPet)).thenReturn(mockPet);
		
		assertNull(adminServ.addPet(mockPet));
	}
	
	@Test
	public void editPetSuccess() {
		Pet mockPet = new Pet();
		
		Mockito.when(petRepo.findById(0)).thenReturn(Optional.of(mockPet));
		Mockito.when(petRepo.save(mockPet)).thenReturn(mockPet);
		
		assertEquals(mockPet, adminServ.editPet(mockPet));
	}
	
	@Test
	public void editPetDoesNotExist() {
		Pet mockPet = new Pet();
		
		Mockito.when(petRepo.findById(0)).thenReturn(Optional.empty());
		
		assertNull(adminServ.editPet(mockPet));
	}
	
	@Test
	public void editPetSomethingWrong() {
		Pet mockPet = new Pet();
		
		Mockito.when(petRepo.findById(0))
			.thenReturn(Optional.of(mockPet))
			.thenReturn(Optional.empty());
		
		assertNull(adminServ.editPet(mockPet));
	}
}
