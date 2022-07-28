package com.revature.petapp.controllers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.revature.petapp.PetAppBootApplication;
import com.revature.petapp.services.UserService;

@SpringBootTest(classes=PetAppBootApplication.class)
public class AuthControllerTest {
	@MockBean
	private UserService userServ;
}
