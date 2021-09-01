package com.ISA2020.farmacia.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.basic.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

	
	
	@Query(value="SELECT * from price where code= ?1 and farmacy_id = ?2 and stands_from < ?3 and stands_until > ?3 ", nativeQuery = true)
	Price findPrice(String code, String id, LocalDate date);

	
	

	
}
