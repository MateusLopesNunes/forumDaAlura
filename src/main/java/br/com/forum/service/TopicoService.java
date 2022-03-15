package br.com.forum.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.forum.dto.DetalhesTopicoDto;
import br.com.forum.dto.TopicoDto;
import br.com.forum.dto.request.TopicoDtoRequest;
import br.com.forum.model.Topico;
import br.com.forum.repository.CursoRepository;
import br.com.forum.repository.TopicoRepository;

@Service
public class TopicoService {
	
	private TopicoRepository topicoRepository;
	private CursoRepository cursoRepository;
	
	@Autowired
	public TopicoService(TopicoRepository topicoRepository, CursoRepository cursoRepository) {
		this.topicoRepository = topicoRepository;
		this.cursoRepository = cursoRepository;
	}

	public Page<TopicoDto> findAllTopicos(@RequestParam(required = false) String nomeCurso, Pageable page) {
		
		if (nomeCurso == null) {
			//sem parametro
			Page<Topico> topicos = topicoRepository.findAll(page);
			return topicos.map(TopicoDto::new);
		}
		else {
			//com paramentro
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, page);
			return topicos.map(TopicoDto::new);
		}
	}
	
	public ResponseEntity<TopicoDto> createTopico(TopicoDtoRequest obj, UriComponentsBuilder uriBuilder) {
		Topico topico = obj.dtoToModel(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	public Optional<Topico> verifyExists(Long id) {
		return topicoRepository.findById(id);
	}
	
	public ResponseEntity deleteTopico(Long id) {
		Optional<Topico> topico = verifyExists(id);
		topicoRepository.deleteById(id);
		
		if (topico.isPresent()) {
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	public ResponseEntity<DetalhesTopicoDto> topicoById(Long id) {
		Optional<Topico> topico = verifyExists(id);
		
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	public ResponseEntity<TopicoDto> updateTopicoById(Long id, TopicoDtoRequest obj) {
		Optional<Topico> topico = verifyExists(id);
		topico.get().setTitulo(obj.getTitulo());
		topico.get().setMensagem(obj.getMensagem());
		
		if (topico.isPresent()) {
			Topico topicoSave = topicoRepository.save(topico.get());
			return ResponseEntity.ok(new TopicoDto(topicoSave));
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
}
