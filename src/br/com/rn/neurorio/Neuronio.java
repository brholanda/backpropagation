package br.com.rn.neurorio;

import br.com.rn.validador.Validador;

public class Neuronio{
	
	private Validador validador;
	private Sinapse[] sinapsesOrigem;
	private Sinapse[] sinapsesDestino;

	public Neuronio() {}
	
	public Neuronio(int length, Validador validador) {
		this.validador = validador;
	}
	
	public Double ativar(double... dentritos) {
		double somatorio = 0;
		if (null != sinapsesOrigem) {
			somatorio = somar(dentritos);
		} else {
			for(int i = 0; i < dentritos.length; i++) {
				somatorio += dentritos[i];
			}
		}
		return validador.validar(somatorio);
	}
	
	private Double somar(double[] dentritos) {
		double resultado = 0;
		for(int i = 0; i < dentritos.length; i++) {
			resultado += dentritos[i] * sinapsesOrigem[i].getPeso();
		}
		return resultado;
	}

	public Sinapse[] getSinapsesOrigem() {
		return sinapsesOrigem;
	}

	public void setSinapsesOrigem(Sinapse[] sinapsesOrigem) {
		this.sinapsesOrigem = sinapsesOrigem;
	}

	public Sinapse[] getSinapsesDestino() {
		return sinapsesDestino;
	}

	public void setSinapsesDestino(Sinapse[] sinapsesDestino) {
		this.sinapsesDestino = sinapsesDestino;
	}

}
