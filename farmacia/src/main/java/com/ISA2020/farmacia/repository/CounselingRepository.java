package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.intercations.Counseling;

@Repository
public interface CounselingRepository extends JpaRepository<Counseling, Long> {

	//@Query(value="SELECT COUNT(COUNSELING_ID) from COUNSELING where DAY(DATE_TIME) = ?1 and MONTH(DATE_TIME) = MONTH(CURRENT_DATE()) and email in (?2) and show_up=true ", nativeQuery = true)
	@Query(value="SELECT COUNT(COUNSELING_ID) from COUNSELING where DAY(DATE_TIME) = ?1 and MONTH(DATE_TIME) = MONTH(CURRENT_DATE()) and email in (?2) ", nativeQuery = true)
	int countThisDay( int day, List<String> emails);
	//@Query(value="SELECT COUNT(COUNSELING_ID) from COUNSELING where MONTH(DATE_TIME) = ?1 and YEAR(DATE_TIME) = YEAR(CURRENT_DATE()) and email in (?2) and show_up=true ", nativeQuery = true)
	@Query(value="SELECT COUNT(COUNSELING_ID) from COUNSELING where MONTH(DATE_TIME) = ?1 and YEAR(DATE_TIME) = YEAR(CURRENT_DATE()) and email in (?2)", nativeQuery = true)
	int countThisMonth(int i, List<String> emails);
	//@Query(value="SELECT COALESCE(sum(price),0) from COUNSELING where DAY(DATE_TIME) = ?1 and MONTH(DATE_TIME) = MONTH(CURRENT_DATE()) and email in (?2) and show_up=true ", nativeQuery = true)
	@Query(value="SELECT COALESCE(sum(price),0) from COUNSELING where DAY(DATE_TIME) = ?1 and MONTH(DATE_TIME) = MONTH(CURRENT_DATE()) and email in (?2)", nativeQuery = true)
	float incomeByDay(int i, List<String> emails);
	//@Query(value="SELECT COALESCE(sum(price),0) from COUNSELING where MONTH(DATE_TIME) = ?1 and YEAR(DATE_TIME) = YEAR(CURRENT_DATE()) and email in (?2) and show_up=true ", nativeQuery = true)
	@Query(value="SELECT COALESCE(sum(price),0) from COUNSELING where MONTH(DATE_TIME) = ?1 and YEAR(DATE_TIME) = YEAR(CURRENT_DATE()) and email in (?2) ", nativeQuery = true)
	float incomeByMonth(int i, List<String> emails);

	
	

	
}
