package com.malves.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.malves.minhasfinancas.model.entity.Lancamento;
import com.malves.minhasfinancas.model.enums.StatusLancamento;
import com.malves.minhasfinancas.model.enums.TipoLancamento;

@ExtendWith(SpringExtension.class) //Usado para manter o teste limpo e melhora a depuração
@ActiveProfiles("test") //Pega as configurações do arquivo application-test
@DataJpaTest //Cria uma instancia do banco de dados em memoria, ao finalizar os testes ela deleta da memoria 
@AutoConfigureTestDatabase(replace = Replace.NONE) //Para não criar uma nova configuração da base e usar a que ja foi configurada
public class LancamentoRepositoryTets {

	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLacamento() {
		Lancamento lancamento = criarLacamento();
		
		lancamento = repository.save(lancamento);
		assertThat(lancamento.getId()).isNotNull();
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		assertThat(lancamentoInexistente).isNull();
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		lancamento.setAno(2018);
		lancamento.setDescricao("Teste Atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		assertThat(lancamentoAtualizado.getAno()).isEqualTo(lancamento.getAno());
		assertThat(lancamentoAtualizado.getDescricao()).isEqualTo(lancamento.getDescricao());
		assertThat(lancamentoAtualizado.getStatus()).isEqualTo(lancamento.getStatus());	
		
	}
	
	@Test
	public void deveBuscarPorLancamentoPorId() {
		Lancamento lancamento = criarEPersistirUmLancamento();
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}
	
	public Lancamento criarEPersistirUmLancamento() {
		Lancamento lancamento = criarLacamento();
		entityManager.persist(lancamento);
		return lancamento;
	}
	
	public static Lancamento criarLacamento() {
		return Lancamento.builder()
									.ano(2019)
									.mes(1)
									.descricao("lancamento teste")
									.valor(BigDecimal.valueOf(10))
									.tipo(TipoLancamento.RECEITA)
									.status(StatusLancamento.PENDENTE)
									.dataCadastro(LocalDate.now())
									.build();
	}
	
}
