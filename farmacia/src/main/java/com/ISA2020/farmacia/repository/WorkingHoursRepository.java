package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.WorkingHours;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {

	@Query(value="SELECT * from working_hours where farmacy_id=?1 ", nativeQuery = true)
	List<WorkingHours> findAllByFarmacyId(String id);

	

	

	
	

	
}
