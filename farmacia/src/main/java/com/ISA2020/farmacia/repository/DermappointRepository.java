package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.intercations.DermAppointment;

@Repository
public interface DermappointRepository extends JpaRepository<DermAppointment, Long> {

	@Query(value="SELECT * from derm_appointment where farmacy_id= ?1 ", nativeQuery = true)
	List<DermAppointment> findByFarmacyId(String id);

	

}
