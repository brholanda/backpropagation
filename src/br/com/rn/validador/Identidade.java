package br.com.rn.validador;

public class Identidade implements Validador {

	@Override
	public Double validar(Double valor) {
		return valor;
	}


}
