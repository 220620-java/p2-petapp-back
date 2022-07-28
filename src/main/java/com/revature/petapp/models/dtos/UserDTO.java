package com.revature.petapp.models.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.revature.petapp.models.Pet;
import com.revature.petapp.models.Role;
import com.revature.petapp.models.User;

/**
 * Data Transfer Object to prepare a User object to be sent in an HTTP response. 
 * Looks like a User but without the password.
 * @author SierraNicholes
 *
 */
public class UserDTO {
	private int id;
	private String username;
	private Role role;
	private List<Pet> pets;
	
	public UserDTO() {
		super();
		this.id = 0;
		this.username = "";
		this.role = new Role();
		this.pets = new ArrayList<>();
	}
	
	public UserDTO(int id, String username, List<Pet> pets) {
		super();
		setId(id);
		setUsername(username);
		setRole(new Role());
		setPets(pets);
	}
	
	public UserDTO(int id, String username, Role role, List<Pet> pets) {
		super();
		setId(id);
		setUsername(username);
		setRole(role);
		setPets(pets);
	}
	
	public UserDTO(User user) {
		super();
		setId(user.getId());
		setUsername(user.getUsername());
		setRole(user.getRole());
		setPets(user.getPets());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, pets, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		return id == other.id && Objects.equals(pets, other.pets) && Objects.equals(role, other.role)
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", role=" + role + ", pets=" + pets + "]";
	}
}
