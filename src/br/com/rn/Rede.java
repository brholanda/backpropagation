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
	private Map<Sinapse, Double> mapaMudancas;
	private int qtdNeuroniosSaida;

	public Rede(int... neuroniosCamadas) {
		this.neuroniosCamadas = neuroniosCamadas;
		
		int indiceSaidas = neuroniosCamadas.length - 1;
		qtdNeuroniosSaida = neuroniosCamadas[indiceSaidas];
		
		int qtdNeuronios = 0;
		for (int i = 0; i < neuroniosCamadas.length; i ++) {
			qtdNeuronios += neuroniosCamadas[i];
		}
		neuronios = new Neuronio[qtdNeuronios];
		
		Neuronio neuronio;
		int ultimoIndice;
		for(ultimoIndice = 0; ultimoIndice < neuroniosCamadas[0]; ultimoIndice ++) {
			neuronio = new Neuronio(1, new Sigmoidal());
			neuronios[ultimoIndice] = neuronio;
		}
		
		Sinapse sinapse;
		Sinapse[] sinapsesOrigem, sinapsesDestino;
		for (int i = 1; i < neuroniosCamadas.length; i ++) {
			sinapse = new Sinapse();
			for (int k = 0; k < neuroniosCamadas[i]; k ++) {
				neuronio = new Neuronio(neuroniosCamadas[i - 1], new Sigmoidal());
				sinapsesOrigem = new Sinapse[neuroniosCamadas[i-1]];
				for (int indiceOrigem = 0, j = ultimoIndice - neuroniosCamadas[i-1]; j < ultimoIndice; j ++, indiceOrigem++) {
					if (k == 0) {
						sinapsesDestino = new Sinapse[neuroniosCamadas[i]];
						neuronios[j].setSinapsesDestino(sinapsesDestino);
					} else {
						sinapsesDestino = neuronios[j].getSinapsesDestino();
					}
					sinapse = new Sinapse();
					sinapse.setOrigem(neuronios[j]);
					sinapse.setDestino(neuronio);
					sinapsesDestino[k] = sinapse;
					sinapsesOrigem[indiceOrigem] = sinapse;
					neuronio.setSinapsesOrigem(sinapsesOrigem);
				}
				neuronios[ultimoIndice + k] = neuronio;
			}
			ultimoIndice += neuroniosCamadas[i];
		}
		
		for (int i = 1; i < neuronios.length; i ++) {
			for (int j = 0; i < neuronios[i].getSinapsesOrigem().length; j ++) {
				mapaMudancas.put(neuronios[i].getSinapsesOrigem()[j], 0d);
			}
		}
	}

	public double[] processarSaidas(double[] conjuntoEntradas) {
		memo = new HashMap<>();
		for(int i = 0; i < neuroniosCamadas[0]; i++) {
			memo.put(neuronios[i], neuronios[i].saidaAxionio(conjuntoEntradas[i]));
		}
		
		double[] saidas = new double[qtdNeuroniosSaida];
		for(int indiceSaida = 0, i = neuronios.length - qtdNeuroniosSaida; i < neuronios.length; i++, indiceSaida++) {
			saidas[indiceSaida] = processarSaida(neuronios[i]);
		}
		return saidas;
	}

	private double processarSaida(Neuronio neuronio) {
		if (null != memo.get(neuronio)) {
			return memo.get(neuronio);
		}
		Sinapse[] sinapsesOrigem = neuronio.getSinapsesOrigem();
		double[] saidasOrigem = new double[sinapsesOrigem.length];
		for(int i = 0; i < saidasOrigem.length; i++) {
			saidasOrigem[i] = processarSaida(sinapsesOrigem[i].getOrigem());
		}
		double saida = neuronio.saidaAxionio(saidasOrigem);
		memo.put(neuronio, saida);
		return saida;
	}

	public void verificaErro(double erro, Double fator) {
		double saidaNeuronio;
		Neuronio neuronio;
		Sinapse sinapse;
		double beta;
		for(int i = neuronios.length - 1; i > neuroniosCamadas[0] - 1; i--) {
			neuronio = neuronios[i];
			saidaNeuronio = memo.get(neuronio);
			if (i > neuronios.length - qtdNeuroniosSaida) {
				for (int j = 0; i < neuronio.getSinapsesOrigem().length; j ++) {
					beta = saidaNeuronio * (1-saidaNeuronio) * -erro;
					sinapse = neuronio.getSinapsesOrigem()[j];
					mapaMudancas.put(sinapse, - fator * memo.get(sinapse.getOrigem()) * beta);
				}
			} else {
				double somatorio = 0d;
				for (int j = 0; i < neuronio.getSinapsesDestino().length; j ++) {
					somatorio += memo.get(neuronio) * neuronio.getSinapsesDestino()[j].getPeso();
				}
				for (int j = 0; i < neuronio.getSinapsesOrigem().length; j ++) {
					beta = saidaNeuronio * (1-saidaNeuronio) * -somatorio;
					sinapse = neuronio.getSinapsesOrigem()[j];
					mapaMudancas.put(sinapse, - fator * memo.get(sinapse.getOrigem()) * beta);
				}
			}
		}
		
	}

	public void ajustarPesos() {
		Sinapse sinapse;
		for (int i = neuroniosCamadas[0]; i < neuronios.length; i++) {
			for (int j = 0; i < neuronios[i].getSinapsesOrigem().length; j ++) {
				sinapse = neuronios[i].getSinapsesOrigem()[j];
				sinapse.setPeso(mapaMudancas.get(sinapse));
			}
		}
	}

}
