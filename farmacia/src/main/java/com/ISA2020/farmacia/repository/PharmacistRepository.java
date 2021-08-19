package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.users.Pharmacist;

@Repository
public interface PharmacistRepository extends JpaRepository<Pharmacist, String> {

	@Query(value="SELECT * from pharmacist f where farmacy_id=?1 ", nativeQuery = true)
	List<Pharmacist> findByFarmacyId(String id);

	List<Pharmacist> findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(String string, String string2);

	
	

	
}