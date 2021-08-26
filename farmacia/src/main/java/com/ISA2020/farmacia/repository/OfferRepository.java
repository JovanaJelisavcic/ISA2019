package com.ISA2020.farmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.intercations.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

	@Query(value="SELECT * FROM OFFER WHERE orderId=?1 ", nativeQuery=true)
	List<Offer> findAllByOrderId(Long id);

	
	

	
}
