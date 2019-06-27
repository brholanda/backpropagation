package br.com.rn.validador;

public class Sigmoidal implements Validador {

	@Override
	public Double validar(Double somatorio) {
		return 1d/(1d+Math.exp(-somatorio));
	}

}
