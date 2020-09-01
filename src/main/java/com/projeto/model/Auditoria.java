package com.projeto.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Embeddable
public class Auditoria {

	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name="data_operacao", nullable=true, columnDefinition = "DATE")
	private Date dataOperacao;
	
	@Column(name="tipo_operacao", nullable=true)
	private String  tipoOperacao;

	@PrePersist
	@PreUpdate
	public void onCreate() {
		this.setDataOperacao(new Date());
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getDataOperacao() {
		return dataOperacao;
	}
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	public String getTipoOperacao() {
		return tipoOperacao;
	}
	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	
}
