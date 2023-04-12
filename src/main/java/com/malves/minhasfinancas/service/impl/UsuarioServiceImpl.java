package com.malves.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malves.minhasfinancas.exception.ErroAutenticacao;
import com.malves.minhasfinancas.exception.RegraNegocioException;
import com.malves.minhasfinancas.model.entity.Usuario;
import com.malves.minhasfinancas.model.repository.UsuarioRepository;
import com.malves.minhasfinancas.service.UsuarioService;

@Service //Infroamdo que é um serviço que precisa ser gerenciado pelo container do spring 
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired //Injeção de dependencia
	private UsuarioRepository repository; //Para executar as operações do banco de dados
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional //Vai criar uma transação no banco de dados. Depois de salvar ele vai comitar na base
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe usuário cadastrado com este email.");
			//Sempre que houver uma quebra na regra de negocio apresentar a mensagem de erro especifica.
		}
	}

	
}
