package com.malves.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malves.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	//A interface ja prover os métodos padrões salvar, deleter, atualizar, listar.
	//Pode criar métodos costomizados
}
