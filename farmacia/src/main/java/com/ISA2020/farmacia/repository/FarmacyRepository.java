package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.Farmacy;

@Repository
public interface FarmacyRepository extends JpaRepository<Farmacy, String> {

	@Query(value="SELECT * from FARMACY f order by stars DESC LIMIT 5 ", nativeQuery = true)
	List<Farmacy> getFiveHighestRated();

	
	List<Farmacy> findByAdressLikeIgnoreCaseOrNameLikeIgnoreCase(String name, String name2);

	
	

	
}
