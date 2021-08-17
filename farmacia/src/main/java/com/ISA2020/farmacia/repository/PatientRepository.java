package com.ISA2020.farmacia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.users.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

	
	

	
}
