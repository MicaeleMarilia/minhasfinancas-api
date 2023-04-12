package com.malves.minhasfinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Trim;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malves.minhasfinancas.exception.RegraNegocioException;
import com.malves.minhasfinancas.model.entity.Lancamento;
import com.malves.minhasfinancas.model.enums.StatusLancamento;
import com.malves.minhasfinancas.model.repository.LancamentoRepository;
import com.malves.minhasfinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	
	private LancamentoRepository repository;
	
	public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId()); //Garantir que o lancamento ja tenha sido salvo e seja uma atualização 
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PENDENTE); //Status inicial é o Pendente (fixo)
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true) //Informa que é uma transação so de leitura
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		
		//Vai pegar as propiedades preenchidas dentro do lancamentoFiltro e comparar, fazando assim uma pesquisa na base
		Example example = Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase() //Ignorar se for passdo minusculo ou maiusculo
				.withStringMatcher(StringMatcher.CONTAINING)); //Como se fosse um like, filtrando por um pedaço do lancamento
				
		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
		
	}

	@Override
	public void validar(Lancamento lancamento) {
		
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma descrição válida.");
		}

		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}
		
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4 ) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário.");
		}
		
		//Validando se não está passando um numero negativo
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioException("Informe um Valor válido.");
		}
		
		if(lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de Lançamento.");
		}
	}

}
