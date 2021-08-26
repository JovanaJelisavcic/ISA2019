package com.ISA2020.farmacia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.basic.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

	@Query(value="SELECT AVG(rating_stars) FROM rating WHERE subject_id= ?1 ", nativeQuery=true)
	float getSubjectStars(String subjectId);

	@Query(value="SELECT * FROM rating WHERE email= ?1 and subject_id= ?2  ", nativeQuery=true)
	Optional<Rating> findThisRating(String email, String subjectId);

	
	

	
}
