package ufpb.br.apilocadora.util;

import ufpb.br.apilocadora.domain.Carro;

public class CarroCreator {

    private static final String nome = "nome";
    private static final String chassi = "chassi";
    private static final String cor = "cor";
    private static final int quantPortas = 4;
    private static final String tipoCombustivel = "tipoCombustivel";
    private static final boolean estaALugado = false;

    public static Carro defaultCarro() {

        return new Carro(null, nome, chassi, cor, quantPortas, tipoCombustivel, estaALugado);
    }
}
