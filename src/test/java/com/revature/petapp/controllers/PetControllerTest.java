package com.revature.petapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.petapp.exceptions.AlreadyAdoptedException;
import com.revature.petapp.models.Pet;
import com.revature.petapp.models.User;
import com.revature.petapp.services.AdminService;
import com.revature.petapp.services.UserService;

@WebMvcTest(controllers=PetController.class)
public class PetControllerTest {
	@MockBean
	private UserService userServ;
	@MockBean
	private AdminService adminServ;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Test
	public void addPetSuccess() throws JsonProcessingException, Exception {
		Pet mockPet = new Pet();
		Pet mockPetWithId = new Pet();
		mockPetWithId.setId(1);
		
		Mockito.when(adminServ.addPet(mockPet)).thenReturn(mockPetWithId);
		
		mockMvc.perform(post("/pets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockPet)))
			.andExpect(status().isCreated())
			.andExpect(content().json(jsonMapper.writeValueAsString(mockPetWithId)));
	}
	
	@Test
	public void getPetByIdExists() throws JsonProcessingException, Exception {
		Pet mockPet = new Pet();
		Mockito.when(userServ.getPet(1)).thenReturn(mockPet);
		
		mockMvc.perform(get("/pets/1"))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonMapper.writeValueAsString(mockPet)));
	}
	
	@Test
	public void getPetByIdNotFound() throws Exception {
		Mockito.when(userServ.getPet(1)).thenReturn(null);
		
		mockMvc.perform(get("/pets/1"))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void updatePetSuccess() throws JsonProcessingException, Exception {
		Pet mockPet = new Pet();
		mockPet.setId(1);
		Mockito.when(adminServ.editPet(mockPet)).thenReturn(mockPet);
		
		mockMvc.perform(put("/pets/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockPet)))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonMapper.writeValueAsString(mockPet)));
	}
	
	@Test
	public void updatePetNullPet() throws JsonProcessingException, Exception {
		mockMvc.perform(put("/pets/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(null)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updatePetIdDoesNotMatch() throws JsonProcessingException, Exception {
		Pet mockPet = new Pet();
		
		mockMvc.perform(put("/pets/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockPet)))
			.andExpect(status().isConflict());
	}
	
	@Test
	public void viewAllPetsSuccess() throws JsonProcessingException, Exception {
		List<Pet> mockList = new ArrayList<>();
		
		Mockito.when(userServ.viewAllPets()).thenReturn(mockList);
		
		mockMvc.perform(get("/pets"))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonMapper.writeValueAsString(mockList)));
	}
	
	@Test
	public void adoptPetSuccess() throws JsonProcessingException, Exception {
		Pet mockPet = new Pet();
		mockPet.setId(1);
		User mockUser = new User();
		mockUser.setId(1);
		
		Mockito.when(userServ.getUser(1)).thenReturn(mockUser);
		Mockito.when(userServ.adoptPet(mockPet, mockUser)).thenReturn(mockUser);
		
		mockMvc.perform(put("/pets/1/adopt/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockPet)))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonMapper.writeValueAsString(mockUser)));
	}
	
	@Test
	public void adoptPetNullPet() throws JsonProcessingException, Exception {
		Pet mockPet = null;
		User mockUser = new User();
		mockUser.setId(1);
		
		Mockito.when(userServ.getUser(1)).thenReturn(mockUser);
		
		mockMvc.perform(put("/pets/1/adopt/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockPet)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void adoptPetUserDoesNotExist() throws JsonProcessingException, Exception {
		Pet mockPet = new Pet();
		
		Mockito.when(userServ.getUser(1)).thenReturn(null);
		
		mockMvc.perform(put("/pets/1/adopt/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockPet)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void adoptPetIdDoesNotMatch() throws JsonProcessingException, Exception {
		Pet mockPet = new Pet();
		mockPet.setId(0);
		User mockUser = new User();
		mockUser.setId(1);
		
		Mockito.when(userServ.getUser(1)).thenReturn(mockUser);
		
		mockMvc.perform(put("/pets/1/adopt/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockPet)))
			.andExpect(status().isConflict());
	}
	
	@Test
	public void adoptPetAlreadyAdopted() throws JsonProcessingException, Exception {
		Pet mockPet = new Pet();
		mockPet.setId(1);
		User mockUser = new User();
		mockUser.setId(1);
		
		Mockito.when(userServ.getUser(1)).thenReturn(mockUser);
		Mockito.when(userServ.adoptPet(mockPet, mockUser)).thenThrow(AlreadyAdoptedException.class);
		
		mockMvc.perform(put("/pets/1/adopt/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockPet)))
			.andExpect(status().isConflict());
	}
}
