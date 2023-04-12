package com.malves.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malves.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByEmail(String email); 
	//Optional pq pode retornar o email ou não. Dessa forma retorna toda estrutura do Usuario
	boolean existsByEmail(String email); //Faz a pesquisa se existe ou não na base e retorna com true ou false
	
}
