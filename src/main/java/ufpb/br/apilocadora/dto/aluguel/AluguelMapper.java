package ufpb.br.apilocadora.dto.aluguel;

import org.springframework.stereotype.Component;
import ufpb.br.apilocadora.domain.Aluguel;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.domain.Usuario;

@Component
public class AluguelMapper {

    public AluguelDTO toDto(Aluguel aluguel) {
        AluguelDTO aluguelDTO = new AluguelDTO();
        aluguelDTO.setInicioAluguel(aluguel.getInicioAluguel());
        aluguelDTO.setFimAluguel(aluguel.getFimAluguel());
        aluguelDTO.setFormaPagamento(aluguel.getFormaPagamento());
        aluguelDTO.setValor(aluguel.getValor());
        aluguelDTO.setChassi(aluguel.getCarro().getChassi());
        return aluguelDTO;
    }
    
    public Aluguel toEntity(AluguelDTO aluguelDTO, Carro carro, Usuario usuario) {
        Aluguel aluguel = new Aluguel();
        aluguel.setInicioAluguel(aluguelDTO.getInicioAluguel());
        aluguel.setFimAluguel(aluguelDTO.getFimAluguel());
        aluguel.setFormaPagamento(aluguelDTO.getFormaPagamento());
        aluguel.setValor(aluguelDTO.getValor());
        aluguel.setCarro(carro);
        aluguel.setUsuario(usuario);
        return aluguel;
    }
}