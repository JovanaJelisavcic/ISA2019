package com.ISA2020.farmacia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ISA2020.farmacia.entity.users.SysAdmin;

@Repository
public interface SysAdminRepository extends JpaRepository<SysAdmin, String> {

	
	

	
}
