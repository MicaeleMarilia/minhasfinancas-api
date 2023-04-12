package com.malves.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
//import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.malves.minhasfinancas.exception.ErroAutenticacao;
import com.malves.minhasfinancas.exception.RegraNegocioException;
import com.malves.minhasfinancas.model.entity.Usuario;
import com.malves.minhasfinancas.model.repository.UsuarioRepository;
import com.malves.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean //Vai chamar os metodos originais e se quiser mokar algum tem que passar qual sera o comportamento dele
	UsuarioServiceImpl service;
	
	@MockBean //Cria uma instancia mokada
	UsuarioRepository repository;

//	@Before //Informa que o metodo deve ser executado primeiro
//	private void setup() {
//
//		service = new UsuarioServiceImpl(repository);
//		//Passa para o service a instancia mokada
//	}
	
	@Test
	public void deveSalvarUmUsuario() {
		
		//Cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		//when(service) - Cria um moke do Spy (service) e depois moka o teste validar email passando null
		Usuario usuario = Usuario
					.builder()
					.id(1l)
					.nome("usuario")
					.email("usuario@email.com")
					.senha("senha")
					.build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		//vai retornar o usuario criado no cenário
				
		//Ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//Verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("usuario");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("usuario@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		
		//Cenário
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//Ação
		//service.salvarUsuario(usuario);
		org.junit.jupiter.api.Assertions
		.assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario) ) ;
		
		//Verificação
		Mockito.verify(repository, Mockito.never()).save(usuario);  
		//Verificar que não tenha chamado o metodo salvar usuario
		
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		
		//Cenário
		String email = "email@email.com";
		String senha = "senha";
		
			
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//Ação
		Usuario result = service.autenticar(email, senha);
		
		//Verificação
		Assertions.assertThat(result).isNotNull();
		

	}
	
	@Test
	public void deveLancarErroQUandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {

		//Cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//Ação
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado.");
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		
		//Cenário
		String senha = "senha"; 
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));
		//Captura a mensagem de erro que retorna na exception
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
		
	}
	
	@Test
	private void deveValidarEmail() {
		
		
		//Cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		//Quando chamar o metodo existsByEmail com o moke vai retornar false
			
		//Ação
		service.validarEmail("email@email.com");

	}
	
	@Test
	private void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
	
		//Cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		//Quando chamar o metodo existsByEmail com o moke vai retornar true
			
		//Ação
		service.validarEmail("email@email.com");
	
	}
}
