package com.revature.petapp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity // tells the ORM that this exists in the database
// @Table // is implicit; you only need it if the name is different from the class name
public class Pet { // PetSpecies = pet_species
	@Id // specifies that this field is the primary key
	@GeneratedValue(strategy = GenerationType.AUTO) // specifies that the db generates this value
	private int id;
	private String name;
	private int age;
	@ManyToOne
	@JoinColumn(name = "species_id")
	private Species species;
	private String description;
	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;
	@ManyToMany
	@JoinTable(name="pet_need",
		joinColumns = @JoinColumn(name="pet_id"),
		inverseJoinColumns = @JoinColumn(name="need_id"))
	private List<SpecialNeed> needs;
	
	public Pet() {
		super();
		this.id = 0;
		this.name = "";
		this.age = 0;
		this.species = new Species();
		this.description = "";
		this.status = new Status();
		this.needs = new ArrayList<>();
	}
	
	public Pet(String name, int age, Species species, String description) {
		super();
		this.id = 0;
		this.name = name;
		this.age = age;
		this.species = species;
		this.description = description;
		this.status = new Status();
		this.needs = new ArrayList<>();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<SpecialNeed> getNeeds() {
		return needs;
	}

	public void setNeeds(List<SpecialNeed> needs) {
		this.needs = needs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(age, description, id, name, needs, species, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pet other = (Pet) obj;
		return age == other.age && Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(name, other.name) && Objects.equals(needs, other.needs)
				&& Objects.equals(species, other.species) && Objects.equals(status, other.status);
	}

	@Override
	public String toString() {
		return "Pet [id=" + id + ", name=" + name + ", age=" + age + ", species=" + species + ", description="
				+ description + ", status=" + status + ", needs=" + needs + "]";
	}
}
