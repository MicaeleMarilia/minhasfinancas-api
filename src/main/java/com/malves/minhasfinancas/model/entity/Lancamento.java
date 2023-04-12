package com.malves.minhasfinancas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
//import org.springframework.data.convert.Jsr310Converters;

import com.malves.minhasfinancas.model.enums.StatusLancamento;
import com.malves.minhasfinancas.model.enums.TipoLancamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "lancamento", schema = "financas")
@Data
@Builder
public class Lancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "mes")
	private Integer mes;
	
	@Column(name = "ano")
	private Integer ano;
	
	@ManyToOne //A tabela Lancamentos terá muitos lancamentos para um usuario
	//Relação com a tabela do Usuario. ManyToOne - Muitos para um / ManyToMany - Muitos para muitos...
	@JoinColumn(name = "id_usuario") //Informar que é um campo com relação de outra tabela
	private Usuario usuario;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "data_cadastro")
	//@Convert(converter = Jsr310Converters.LocalDateTimeToDateConverter.class) //Converter a data para o tipo Date
	private LocalDate dataCadastro;
	
	@Column(name = "tipo")
	@Enumerated(value = EnumType.STRING) 
	//Infroma que é uma constante e o .STRING é para apresentar na base o texto e não o id da constante (.ORDINAL)
	private TipoLancamento tipo;
	
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private StatusLancamento status;

}
