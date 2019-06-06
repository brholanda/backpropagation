package br.com.rn.neurorio;

public class Sinapse {
	
	private Neuronio[] origem;
	private Neuronio[] destino;
	public Neuronio[] getOrigem() {
		return origem;
	}
	public void setOrigem(Neuronio[] origem) {
		this.origem = origem;
	}
	public Neuronio[] getDestino() {
		return destino;
	}
	public void setDestino(Neuronio[] destino) {
		this.destino = destino;
	}
	
	
}
