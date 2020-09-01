package com.projeto.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.config.ConfigProjeto;
import com.projeto.model.Role;
import com.projeto.model.UsuarioRole;
import com.projeto.repository.RoleRepository;
import com.projeto.repository.UsuarioRoleRepository;
import com.projeto.service.RoleService;


@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	
    @Autowired
	private RoleRepository roleRepository;
    
    @Autowired
    private UsuarioRoleRepository usuarioRoleRepository;
    
    @Override
    @Transactional(readOnly = true)
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public Role save(Role entity) {
		Role role = roleRepository.save(entity);
		this.saveUsuarioRole(role, ConfigProjeto.INCLUSAO);
		return role;
	}

	@Override
	public Role update(Role entity) { 
		Role role = roleRepository.save(entity);
		this.saveUsuarioRole(role, ConfigProjeto.ALTERACAO);
		return role;
	}

	@Override
	public Role getOne(Long id) {
		return roleRepository.getOne(id);
	}

	@Override
	public Role findRole(String role) {
		return null;
	}

	@Override
	public Role findById(Long id) {
		return roleRepository.getOne(id);
	}

	@Override
	public void deleteById(Long id) {
		 this.saveUsuarioRole( getOne(id), ConfigProjeto.EXCLUSAO );
	     roleRepository.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true )
	public List<Role> findAllRoles() {
		return roleRepository.findAllRoles();
	}

	@Override
	@Transactional
	public void saveUsuarioRole(Role role, String operacao) {
		UsuarioRole ur = new UsuarioRole();
		ur.getAuditoria().setDataOperacao(new Date());
		ur.getAuditoria().setUsuario(ConfigProjeto.pegarUsuario());
		ur.getAuditoria().setTipoOperacao(operacao);
		ur.setRole(role);
		usuarioRoleRepository.save(ur);
	}
	
	
}
