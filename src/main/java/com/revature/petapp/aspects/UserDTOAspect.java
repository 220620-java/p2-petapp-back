package com.revature.petapp.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.revature.petapp.models.User;
import com.revature.petapp.models.dtos.UserDTO;

@Aspect
@Component
public class UserDTOAspect {
	@Around("controllerMethodsReturningUser()")
	public Object userToUserDTO(ProceedingJoinPoint joinpoint) throws Throwable {
		ResponseEntity<User> resp = (ResponseEntity<User>) joinpoint.proceed();
		
		User user = resp.getBody();
		UserDTO userDto = new UserDTO(user);
		
		return ResponseEntity.status(resp.getStatusCode()).body(userDto);
	}
	
	@Pointcut("execution(org.springframework.http.ResponseEntity<com.revature.petapp.models.User> com.revature.petapp.controllers..*(..))")
	public void controllerMethodsReturningUser() {}
}
