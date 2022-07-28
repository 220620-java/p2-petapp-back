package com.revature.petapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.petapp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByUsername(String username);
}
