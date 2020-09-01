package com.projeto.service;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.projeto.model.Usuario;
import com.projeto.repository.filtros.UsuarioFiltro;

public interface UsuarioService extends GenericService<Usuario, Long> {
	
	Page<Usuario> listarUsuarioPaginacao(UsuarioFiltro usuarioFiltro, Pageable pageable);
	
	Optional<Usuario> findUsuarioByEmail(String email);
	
	Optional<Usuario> loginUsuarioByEmail(String email);

	void updateLoginUsuario(Usuario usuario);

	void blockedUsuario(Usuario usuario);

	void updateFailedAccess(Usuario usuario);
	
	Usuario clearFoto(Long id);
	
	

}
