package com.revature.petapp.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.petapp.exceptions.UsernameAlreadyExistsException;
import com.revature.petapp.models.User;
import com.revature.petapp.models.dtos.UserDTO;
import com.revature.petapp.services.UserService;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
	@MockBean
	private UserService userServ;

	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Test
	public void getUserByIdExists() throws Exception {
		User mockUser = new User();
		
		Mockito.when(userServ.getUser(1)).thenReturn(mockUser);
		
		mockMvc.perform(get("/users/1")) // send a GET to users/1
			.andExpect(status().isOk()) // expect 200 status code
			.andExpect(content().json(jsonMapper.writeValueAsString(new UserDTO(mockUser))));
	}
	
	@Test
	public void getUserByIdNotFound() throws Exception {
		Mockito.when(userServ.getUser(1)).thenReturn(null);
		
		mockMvc.perform(get("/users/1")) // send a GET to users/1
			.andExpect(status().isNotFound()); // expect 404 status code
	}
	
	@Test
	public void registerUserSuccess() throws JsonProcessingException, Exception {
		User mockUser = new User();
		User mockUserWithId = new User();
		mockUserWithId.setId(1);
		
		Mockito.when(userServ.registerUser(mockUser)).thenReturn(mockUserWithId);
		
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockUser)))
			.andExpect(status().isCreated())
			.andExpect(content().json(jsonMapper.writeValueAsString(new UserDTO(mockUserWithId))));
	}
	
	@Test
	public void registerUserAlreadyExists() throws JsonProcessingException, Exception {
		User mockUser = new User();
		
		Mockito.when(userServ.registerUser(mockUser)).thenThrow(UsernameAlreadyExistsException.class);
		
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockUser)))
			.andExpect(status().isConflict());
	}
	
	@Test
	public void updateUserSuccess() throws JsonProcessingException, Exception {
		User mockUser = new User();
		mockUser.setId(1);
		
		Mockito.when(userServ.updateUser(mockUser)).thenReturn(mockUser);
		
		mockMvc.perform(put("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockUser)))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonMapper.writeValueAsString(new UserDTO(mockUser))));
	}
	
	@Test
	public void updateUserSomethingWrong() throws JsonProcessingException, Exception {
		User mockUser = new User();
		mockUser.setId(1);
		
		Mockito.when(userServ.updateUser(mockUser)).thenReturn(null);
		
		mockMvc.perform(put("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockUser)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateUserNullUser() throws JsonProcessingException, Exception {
		mockMvc.perform(put("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(null)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateUserIdDoesNotMatch() throws JsonProcessingException, Exception {
		User mockUser = new User();
		
		mockMvc.perform(put("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(mockUser)))
			.andExpect(status().isConflict());
	}
}
