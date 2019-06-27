package br.com.rn.treinador;

import br.com.rn.Rede;

public class TreinadorRede extends Treinador {	

	@Override
	public void treinar() {
		double[][] conjuntoEntradas = { { 1, 1 }, { 1, 0 }, { 0, 1 }, { 0, 0 } };
		int[] saidasEsperadas = { 0, 0, 0, 1 };
		double[] saidas;
		double erro = 0d;

		Boolean saidasOk;
		
		Rede rede = new Rede(conjuntoEntradas[0].length, 3, 1);
		
		do {
			saidasOk = true;
			erro = 0d;
			for (int j = 0; j < conjuntoEntradas.length; j++) {
				saidas = rede.processarSaidas(conjuntoEntradas[j]);
				for (int i = 0; i < saidas.length; i++) {
					if (saidas[i] != saidasEsperadas[j]) {
						saidasOk = false;
						erro +=  saidas[i] - saidasEsperadas[j];
						System.out.println(saidas[i] + " - " + saidasEsperadas[j]);
					}
				}
			}
			if (!saidasOk) {
				rede.verificaErro(erro, fator);
				rede.ajustarPesos();
			}
		} while (!saidasOk);
		for (int j = 0; j < conjuntoEntradas.length; j++) {
			System.out.println(conjuntoEntradas[j] + " " + rede.processarSaidas(conjuntoEntradas[j]));
		}
	}

}
