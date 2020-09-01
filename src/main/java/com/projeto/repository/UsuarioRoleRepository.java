package com.projeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.model.UsuarioRole;

@Repository
public interface UsuarioRoleRepository extends JpaRepository<UsuarioRole, Long> {

}
