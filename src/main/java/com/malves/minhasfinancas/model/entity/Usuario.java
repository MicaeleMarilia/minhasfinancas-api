package com.malves.minhasfinancas.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //Toda entidade precisa das duas anotações
@Table(name = "usuario", schema = "financas")
@Builder //Criar uma instancia sem declarar
@Data // Equivalente ao @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode.
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	
	@Id //Todo id deve ter essa anotação
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Gera automaticamnte os ids
	@Column(name = "id") 
	//Informar que é uma coluna e passar suas definições. Se vai ser um campo unico, o tamanho...	
	private Long id;

	@Column(name = "nome")
	
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	private String senha;

}
