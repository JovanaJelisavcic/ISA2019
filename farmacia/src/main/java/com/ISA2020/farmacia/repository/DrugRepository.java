package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.basic.Drug;

@Repository
public interface DrugRepository extends JpaRepository<Drug, String> {

	@Query(value="SELECT * from DRUG LIMIT 5 ", nativeQuery = true)
	List<Drug> getFiveRandom();

	List<Drug> findByNameLikeIgnoreCase(String string);

	List<Drug> findByNameLikeIgnoreCaseOrCodeLikeIgnoreCase(String string, String string2);
	

	
}
