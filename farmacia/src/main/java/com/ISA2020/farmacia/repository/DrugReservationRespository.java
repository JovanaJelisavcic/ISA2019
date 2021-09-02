package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.intercations.DrugReservation;

@Repository
public interface DrugReservationRespository extends JpaRepository<DrugReservation, Long> {

	//@Query(value="SELECT COUNT(RESERVATION_ID) from drug_reservation where farmacy_id= ?2 and DAY(PICK_UP)=?1 and MONTH(PICK_UP) = MONTH(CURRENT_DATE()) and show_up=true ", nativeQuery = true)
	@Query(value="SELECT COUNT(RESERVATION_ID) from drug_reservation where farmacy_id= ?2 and DAY(PICK_UP)=?1 and MONTH(PICK_UP) = MONTH(CURRENT_DATE()) ", nativeQuery = true)
	int countByDay(int i, String id);
	//@Query(value="SELECT COUNT(RESERVATION_ID) from drug_reservation where farmacy_id= ?2 and MONTH(PICK_UP)=?1 and YEAR(PICK_UP) = YEAR(CURRENT_DATE()) and show_up=true ", nativeQuery = true)
	@Query(value="SELECT COUNT(RESERVATION_ID) from drug_reservation where farmacy_id= ?2 and MONTH(PICK_UP)=?1 and YEAR(PICK_UP) = YEAR(CURRENT_DATE()) ", nativeQuery = true)
	int countThisMonth(int i, String id);
	//@Query(value="SELECT * from drug_reservation where farmacy_id= ?1 and DAY(PICK_UP)=?2 and MONTH(PICK_UP) = MONTH(CURRENT_DATE()) and show_up=true ", nativeQuery = true)
	@Query(value="SELECT * from drug_reservation where farmacy_id= ?1 and DAY(PICK_UP)=?2 and MONTH(PICK_UP) = MONTH(CURRENT_DATE())", nativeQuery = true)
	List<DrugReservation> findIncomeByDay(String id, int i);
	//@Query(value="SELECT * from drug_reservation where farmacy_id= ?1 and MONTH(PICK_UP)=?2 and YEAR(PICK_UP) = YEAR(CURRENT_DATE()) and show_up=true ", nativeQuery = true)
	@Query(value="SELECT * from drug_reservation where farmacy_id= ?1 and MONTH(PICK_UP)=?2 and YEAR(PICK_UP) = YEAR(CURRENT_DATE())", nativeQuery = true)
	List<DrugReservation> findIncomeByMonth(String id, int i);

	
	

	
}