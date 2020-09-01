package com.projeto.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="TAB_PERSISTENCE")
public class PersistentTokenLogin implements Serializable{

	private static final long serialVersionUID = 242535403422446440L;

	@Id
	@Column(name="PERSISTENCE_ID")
	private String series;

	@Column(name="PERSITENCE_USERNAME", nullable = false)
	private String username;
	
	@Column(name="PERSITENCE_TOKEN", nullable = false)
	private String token;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PERSITENCE_DATAUSE", nullable = false, columnDefinition = "DATE")
	private Date lastUsed;
	
	
	public PersistentTokenLogin() {
		super();
	}
	
	public PersistentTokenLogin(String series, String username, String token, Date lastUsed) {
		super();
		this.series = series;
		this.username = username;
		this.token = token;
		this.lastUsed = lastUsed;
	}
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getLastUsed() {
		return lastUsed;
	}
	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((series == null) ? 0 : series.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersistentTokenLogin other = (PersistentTokenLogin) obj;
		if (series == null) {
			if (other.series != null)
				return false;
		} else if (!series.equals(other.series))
			return false;
		return true;
	}
	
	
	
	

}
