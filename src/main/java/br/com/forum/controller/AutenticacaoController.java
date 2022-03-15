package br.com.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.forum.dto.TokenDto;
import br.com.forum.dto.request.UsuarioDtoRequest;
import br.com.forum.service.autentication.TokenService;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity autenticar(@RequestBody @Valid UsuarioDtoRequest form) {
		UsernamePasswordAuthenticationToken dadosLogin = new UsernamePasswordAuthenticationToken(form.getEmail(), form.getSenha());
		
		try {
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
		}
		catch(AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
