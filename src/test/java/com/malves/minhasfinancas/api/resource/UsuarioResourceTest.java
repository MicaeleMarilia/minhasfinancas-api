package com.malves.minhasfinancas.api.resource;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malves.minhasfinancas.api.dto.UsuarioDTO;
import com.malves.minhasfinancas.exception.ErroAutenticacao;
import com.malves.minhasfinancas.exception.RegraNegocioException;
import com.malves.minhasfinancas.model.entity.Usuario;
import com.malves.minhasfinancas.service.LancamentoService;
import com.malves.minhasfinancas.service.UsuarioService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest( controllers = UsuarioResource.class ) //Definir os controller que vão subir junto com o teste
@AutoConfigureMockMvc
public class UsuarioResourceTest {

	static final String API = "/api/usuarios";
	static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc; //Mokar todas as chamadas a API
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		//cenario
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when( service.autenticar(email, senha) ).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(dto); //Tranforma qualquer objeto em uma string json
		
		//execucao e verificacao
		
		//Cria uma requisicao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(JSON) //Receber um json
													.contentType(JSON) //Enviar um json
													.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk()) //Espera que
			.andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
		
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		
		//cenario
				String email = "usuario@email.com";
				String senha = "123";
				
				UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
				Mockito.when( service.autenticar(email, senha) ).thenThrow(ErroAutenticacao.class); //Lancar o erro
				
				String json = new ObjectMapper().writeValueAsString(dto); //Tranforma qualquer objeto em uma string json
				
				//execucao e verificacao
				
				//Cria uma requisicao
				MockHttpServletRequestBuilder request = MockMvcRequestBuilders
															.post(API.concat("/autenticar"))
															.accept(JSON) //Receber um json
															.contentType(JSON) //Enviar um json
															.content(json);
				
				mvc
				.perform(request)
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception {
		
		//cenario
				String email = "usuario@email.com";
				String senha = "123";
				
				UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
				Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
				
				Mockito.when( service.salvarUsuario(Mockito.any(Usuario.class)) ).thenReturn(usuario);
				
				String json = new ObjectMapper().writeValueAsString(dto); //Tranforma qualquer objeto em uma string json
				
				//execucao e verificacao
				
				//Cria uma requisicao
				MockHttpServletRequestBuilder request = MockMvcRequestBuilders
															.post(API)
															.accept(JSON) //Receber um json
															.contentType(JSON) //Enviar um json
															.content(json);
				
				mvc
					.perform(request)
					.andExpect(MockMvcResultMatchers.status().isCreated()) //Espera que
					.andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
					.andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
					.andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
		
		//cenario
				String email = "usuario@email.com";
				String senha = "123";
				
				UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
				Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
				
				Mockito.when( service.salvarUsuario(Mockito.any(Usuario.class)) ).thenThrow(RegraNegocioException.class);
				
				String json = new ObjectMapper().writeValueAsString(dto); //Tranforma qualquer objeto em uma string json
				
				//execucao e verificacao
				
				//Cria uma requisicao
				MockHttpServletRequestBuilder request = MockMvcRequestBuilders
															.post(API)
															.accept(JSON) //Receber um json
															.contentType(JSON) //Enviar um json
															.content(json);
				
				mvc
					.perform(request)
					.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
