package br.com.rn.neurorio;

import java.util.Random;

import br.com.rn.validador.Validador;

public class Neuronio{
	
	private double[] wDentritos;
	private Validador validador;
	private Sinapse sinapse;

	public Neuronio() {}
	
	public Neuronio(int length, Validador validador) {
		Random rand = new Random();
		this.validador = validador;
		this.wDentritos = new double[length];
		for(int i = 0; i < wDentritos.length; i++) {
			wDentritos[i] = (double) rand.nextInt(10);
		}
	}
	
	public void verificaErro(int erro, double fator, double[] entradas) {
		for(int i = 0; i < this.wDentritos.length; i++) {
			this.wDentritos[i] = this.wDentritos[i] + erro * fator * entradas[i]; 
		}
	}
	
	public Double saidaAxionio(double... dentritos) {
		double somatorio = somar(dentritos);
		return validador.validar(somatorio);
	}
	
	private Double somar(double[] dentritos) {
		double resultado = 0;
		for(int i = 0; i < dentritos.length; i++) {
			resultado += dentritos[i] * wDentritos[i];
		}
		return resultado;
	}

	public Sinapse getSinapse() {
		return sinapse;
	}

	public void setSinapse(Sinapse sinapse) {
		this.sinapse = sinapse;
	}
	
	
}
