package org.generation.blogPessoal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_postagem")
public class PostagemModel {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Id Long idPostagem;

	@Size(min = 1, max = 70)
	private @NotBlank String tituloPostagem;

	@Size(min = 1, max = 360)
	private String textoPostagem;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@JsonIgnoreProperties({"minhasPostagens"})
	private UsuarioModel usuarioCriador;
	
	@ManyToOne
	@JoinColumn(name = "tema_id")
	@JsonIgnoreProperties({"postagens"})
	private TemaModel temaRelacionado;

	public Long getIdPostagem() {
		return idPostagem;
	}

	public void setIdPostagem(Long idPostagem) {
		this.idPostagem = idPostagem;
	}

	public String getTituloPostagem() {
		return tituloPostagem;
	}

	public void setTituloPostagem(String tituloPostagem) {
		this.tituloPostagem = tituloPostagem;
	}

	public String getTextoPostagem() {
		return textoPostagem;
	}

	public void setTextoPostagem(String textoPostagem) {
		this.textoPostagem = textoPostagem;
	}

	public UsuarioModel getUsuarioCriador() {
		return usuarioCriador;
	}

	public void setUsuarioCriador(UsuarioModel usuarioCriador) {
		this.usuarioCriador = usuarioCriador;
	}

	public TemaModel getTemaRelacionado() {
		return temaRelacionado;
	}

	public void setTemaRelacionado(TemaModel temaRelacionado) {
		this.temaRelacionado = temaRelacionado;
	}


	
}