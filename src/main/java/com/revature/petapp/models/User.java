package com.revature.petapp.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="person")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String username;
	@Column(name="passwd")
	private String password;
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	@OneToMany
	@JoinColumn(name="owner_id")
	private List<Pet> pets;
	
	public User() {
		super();
		this.id = 0;
		this.username = "";
		this.password = "";
		this.role = new Role();
		this.pets = new ArrayList<>();
	}
	
	public User(String username, String password) {
		super();
		this.id = 0;
		this.username = username;
		this.password = password;
		this.role = new Role();
		this.pets = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		return Objects.hash(id, password, pets, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id == other.id && Objects.equals(password, other.password) && Objects.equals(pets, other.pets)
				&& Objects.equals(role, other.role) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", pets="
				+ pets + "]";
	}
}
