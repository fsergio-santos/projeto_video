package com.projeto.repository.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.projeto.model.Permission;
import com.projeto.model.Usuario;
import com.projeto.repository.filtros.UsuarioFiltro;

public interface UsuarioQuery {
	
	Optional<Usuario> findUsuarioByEmail(String email); /// validar o e-mail cadastrado - único
	
	Optional<Usuario> loginUsuarioByEmail(String email); // para fazer o login...
	
	List<Permission> findRolePermissaoByUsuarioId(Long id);
	
	void detach(Usuario usuario);
	
	Page<Usuario> listarUsuarioPaginacao(UsuarioFiltro filtro, Pageable pageable);
	
}
