package com.malves.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.malves.minhasfinancas.model.entity.Usuario;

//@SpringBootTest //Oferece suporte à configuração automática para testes
@ExtendWith(SpringExtension.class) //Usado para manter o teste limpo e melhora a depuração
@ActiveProfiles("test") //Pega as configurações do arquivo application-test
@DataJpaTest //Cria uma instancia do banco de dados em memoria, ao finalizar os testes ela deleta da memoria 
@AutoConfigureTestDatabase(replace = Replace.NONE) //Para não criar uma nova configuração da base e usar a que ja foi configurada
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager; //Responsavel por criar as operações no banco, apenas para testes
	
	@Test //Todo Teste precisa dessa anotação
	public void deveVerificarAExixtenciaDeUmEmail() {
		//Cenário
		Usuario usuario = criarUsuario(); 
		//Criou uma instancia de Usuario
		entityManager.persist(usuario); //Salva o usuario criada acima
		
		//Ação/Execução
		boolean result = repository.existsByEmail("usuario@email.com"); //Pequisa na base pelo email criado
		
		//Verificação
		Assertions.assertThat(result).isTrue(); //Verifica se o email existe
		
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		//Cenário
		//repository.deleteAll(); //Deleta todos registros da tebela
		
		//Ação
		boolean result = repository.existsByEmail("usuario@email.com"); //Pequisa na base pelo email
		
		//Verificação
		Assertions.assertThat(result).isFalse(); //Verifica se o email não existe
		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		
		//Cenário
		Usuario usuario = criarUsuario();
		
		//Ação
		Usuario usuarioSalvo = repository.save(usuario);
		
		//Verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		//Cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//Verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(result.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		
		//Verificação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}
	
	public static Usuario criarUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
}
