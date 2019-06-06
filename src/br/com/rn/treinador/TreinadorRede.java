package br.com.rn.treinador;

import br.com.rn.Rede;

public class TreinadorRede extends Treinador {	

	@Override
	public void treinar() {
		double[] conjuntoEntradas = { 0.5, 0.5, 0.5 };
		double[] saidasEsperadas = { 0.8, 0.2 };
		double[] saidas;
		double erro = 0d;

		Boolean saidasOk;
		
		Rede rede = new Rede(conjuntoEntradas.length, 3, 2);
		
		do {
			saidasOk = true;
			saidas = rede.processarSaidas(conjuntoEntradas);
			for (int i = 0; i < saidas.length; i++) {
				if (saidas[i] != saidasEsperadas[i]) {
					saidasOk = false;
					erro = 1/2 * (Math.pow(saidasEsperadas[i] - saidas[i],2));
					rede.verificaErro(erro, fator);
				}
			}
			if (!saidasOk) {
				rede.ajustarPesos();
			}
		} while (!saidasOk);
	}

}
