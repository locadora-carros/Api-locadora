package ufpb.br.apilocadora.util;

import ufpb.br.apilocadora.domain.Aluguel;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.domain.Usuario;

import java.time.LocalDateTime;

public class AluguelCreator {

    private static final LocalDateTime inicioAluguel = LocalDateTime.parse("2023-12-31T00:00");
    private static final LocalDateTime fimAluguel = LocalDateTime.parse("2024-01-01T00:00");
    private static final String formaPagamento = "formaPagamento";
    private static final Double valor = 1000.50;

    public static Aluguel defaultAluguel(Carro carro, Usuario usuario) {

        return new Aluguel(null, inicioAluguel, fimAluguel, formaPagamento, valor, carro, usuario);
    }
}
