package com.malves.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malves.minhasfinancas.api.dto.UsuarioDTO;
import com.malves.minhasfinancas.exception.ErroAutenticacao;
import com.malves.minhasfinancas.exception.RegraNegocioException;
import com.malves.minhasfinancas.model.entity.Usuario;
import com.malves.minhasfinancas.service.UsuarioService;

@RestController //Informa que é um Controller
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	private UsuarioService service;
	
	public UsuarioResource (UsuarioService service) {
		//Injeção de dependencia
		this.service = service;
	}
	
	@PostMapping("/autenticar")
	private ResponseEntity autenticar( @RequestBody UsuarioDTO dto) {

		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
			
		}catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto) { 
		// @RequestBody para que os dados venham com as propiedade do dto
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha())
				.build();
		
		try {
			
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
			
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
}
