package br.com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EmailUser {
 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)// tem que ser obrigatório 
	private String email;


	// é obrigatório por isso false
 	@ManyToOne(optional = false, fetch = FetchType.EAGER) // muitos para um uma pessoa pode tem mais de um celular
	private UsuarioPessoa usuarioPessoa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
  

	@Override
	public String toString() {
		return "EmailUser [id=" + id + ", email=" + email + ", usuarioPessoa=" + usuarioPessoa + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	
	

}
