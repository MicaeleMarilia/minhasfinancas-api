package com.malves.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//Classe para criar objetos simples e transformar ele em uma entidade simples

@Getter
@Setter
@Builder
public class UsuarioDTO {
	
	private String email;
	private String nome;
	private String senha;

}
