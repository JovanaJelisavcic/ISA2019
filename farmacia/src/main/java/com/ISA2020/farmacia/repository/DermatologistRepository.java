package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.users.Dermatologist;

@Repository
public interface DermatologistRepository extends JpaRepository<Dermatologist, String> {

	List<Dermatologist> findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(String string, String string2);

	

	

	
	

	
}
