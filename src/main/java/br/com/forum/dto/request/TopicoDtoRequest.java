package br.com.forum.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import br.com.forum.model.Curso;
import br.com.forum.model.Topico;
import br.com.forum.repository.CursoRepository;

public class TopicoDtoRequest {

	@NotBlank @Length(min = 5)
	private String titulo;
	
	@NotBlank @Length(min = 10)
	private String mensagem;

	private String nomeCurso;
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getNomeCurso() {
		return nomeCurso;
	}
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	
	public Topico dtoToModel(CursoRepository cursoRepository) {
		Curso curso = cursoRepository.findByNome(nomeCurso);
		return new Topico(titulo, mensagem, curso);
	}
}
