package br.com.forum.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.forum.dto.DetalhesTopicoDto;
import br.com.forum.dto.TopicoDto;
import br.com.forum.dto.request.TopicoDtoRequest;
import br.com.forum.service.TopicoService;

@RestController
@RequestMapping("/topicos")
public class HelloController {

	private TopicoService topicoService;
	
	@Autowired
	public HelloController(TopicoService topicoService) {
		this.topicoService = topicoService;
	}
	
	@Cacheable(value = "findTopicos")
	@GetMapping
	public Page<TopicoDto> findAllTopicos(String nomeCurso, Pageable page) {
		return topicoService.findAllTopicos(nomeCurso, page);
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "findTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> createTopico(@RequestBody @Valid TopicoDtoRequest obj, UriComponentsBuilder uriBuilder) {
		return topicoService.createTopico(obj, uriBuilder);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "findTopicos", allEntries = true)
	public void deleteTopico(@PathVariable Long id) {
		topicoService.deleteTopico(id);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDto> topicoById(@PathVariable Long id) {
		return topicoService.topicoById(id);
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "findTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> updatetopico(@PathVariable Long id ,@RequestBody @Valid TopicoDtoRequest obj) {
		return topicoService.updateTopicoById(id, obj);
	}
}
