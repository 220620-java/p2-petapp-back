package com.revature.petapp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.petapp.models.Species;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Integer> {

}
