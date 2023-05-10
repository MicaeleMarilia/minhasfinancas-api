package com.malves.minhasfinancas.service;

import java.util.Optional;

import com.malves.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha); //Validar o login
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email); //Validar o email caso ja exista
	
	Optional<Usuario> obterPorId(Long id);

}
