package com.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projeto.model.PersistentTokenLogin;

@Repository
public interface PersistenceTokenRepository extends JpaRepository<PersistentTokenLogin, String>{
	
	@Modifying
	@Query("DELETE FROM PersistentTokenLogin p WHERE p.username =:username")
	public void deletePersistenceTokenLogin(@Param("username") String username);

}
