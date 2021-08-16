package com.ISA2020.farmacia.security;

import org.springframework.data.jpa.repository.JpaRepository;




public interface UserRepository extends JpaRepository<Credentials, Long>{
	
	Credentials findByUsername(String username);

}
