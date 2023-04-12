package com.malves.minhasfinancas.exception;

public class RegraNegocioException extends RuntimeException{
	//Exceção em tempo de execução(Mensagens de error)
	
	private static final long serialVersionUID = 1L;

	public RegraNegocioException(String msg) {
		super(msg);
	}
}
