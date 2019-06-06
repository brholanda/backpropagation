package br.com.rn;

import java.util.HashMap;
import java.util.Map;

import br.com.rn.neurorio.Neuronio;
import br.com.rn.neurorio.Sinapse;
import br.com.rn.validador.Sigmoidal;

public class Rede {
	
	private Neuronio[] neuronios;
	private int[] neuroniosCamadas;
	private Map<Neuronio, Double> memo;

	public Rede(int... neuroniosCamadas) {
		this.neuroniosCamadas = neuroniosCamadas;
		memo = new HashMap<>();
		
		int qtdNeuronios = 0;
		for (int i = 0; i < neuroniosCamadas.length; i ++) {
			qtdNeuronios += neuroniosCamadas[i];
		}
		
		neuronios = new Neuronio[qtdNeuronios];
		Neuronio neuronio;
		Sinapse sinapse;
		int ultimoIndice = 0;
		for (int i = 0; i < neuroniosCamadas.length + 1; i ++) {
			sinapse = new Sinapse();
			for (int k = ultimoIndice; k < neuroniosCamadas[i]; k ++) {
				if (i != 0) {
					neuronio = new Neuronio(neuroniosCamadas[i - 1], new Sigmoidal());
					Neuronio[] origem = new Neuronio[neuroniosCamadas[i-1]];
					Neuronio[] destino = new Neuronio[neuroniosCamadas[i]];
					for (int j = ultimoIndice - neuroniosCamadas[i-1]; j < neuroniosCamadas[i-1]; j ++) {
						origem[i] = neuronios[j];
						destino[i] = neuronio;
					}
					sinapse.setOrigem(origem);
					sinapse.setDestino(destino);
				} else {
					neuronio = new Neuronio(1, new Sigmoidal());
				}
				neuronio.setSinapse(sinapse);
				neuronios[i] = neuronio;
			}
			ultimoIndice += neuroniosCamadas[i];
		}
	}

	public double[] processarSaidas(double[] conjuntoEntradas) {
		for(int i = 0; i < neuroniosCamadas[0]; i++) {
			memo.put(neuronios[i], neuronios[i].saidaAxionio(conjuntoEntradas[i]));
		}
		int indiceSaidas = neuroniosCamadas.length;
		int qtdNeuroniosSaida = neuroniosCamadas[indiceSaidas];
		double[] saidas = new double[qtdNeuroniosSaida];
		for(int i = neuronios.length - qtdNeuroniosSaida; i < neuronios.length; i++) {
			saidas[i] = processarSaida(neuronios[i]);
		}
		return saidas;
	}

	private double processarSaida(Neuronio neuronio) {
		if (null != memo.get(neuronio)) {
			return memo.get(neuronio);
		}
		Sinapse sinapse = neuronio.getSinapse();
		double[] saidasOrigem = new double[sinapse.getOrigem().length];
		for(int i = 0; i < saidasOrigem.length; i++) {
			saidasOrigem[i] = processarSaida(neuronios[i]);
		}
		double saida = neuronio.saidaAxionio(saidasOrigem);
		memo.put(neuronio, saida);
		return saida;
	}

	public void verificaErro(double erro, Double fator) {
		// TODO Auto-generated method stub
		
	}

	public void ajustarPesos() {
		// TODO Auto-generated method stub
		
	}

}
