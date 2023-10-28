package ufpb.br.apilocadora.dto.carro;

import org.springframework.stereotype.Component;
import ufpb.br.apilocadora.domain.Carro;

@Component
public class CarroMapper {

    public CarroDTO toDto(Carro carro) {
        CarroDTO carroDTO = new CarroDTO();
        carroDTO.setChassi(carro.getChassi());
        carroDTO.setNome(carro.getNome());
        carroDTO.setCor(carro.getCor());
        carroDTO.setQuantPortas(carro.getQuantPortas());
        carroDTO.setTipoCombustivel(carro.getTipoCombustivel());
        return carroDTO;
    }

    public CarroInfoSimplesDTO carroInfoSimplesDTO(Carro carro) {
        CarroInfoSimplesDTO carroInfoDTO = new CarroInfoSimplesDTO();
        carroInfoDTO.setNome(carro.getNome());
        carroInfoDTO.setCor(carro.getCor());
        carroInfoDTO.setQuantPortas(carro.getQuantPortas());
        return carroInfoDTO;
    }

    public Carro toEntity(CarroDTO carroDTO) {
        Carro carro = new Carro();
        carro.setChassi(carroDTO.getChassi());
        carro.setNome(carroDTO.getNome());
        carro.setCor(carroDTO.getCor());
        carro.setQuantPortas(carroDTO.getQuantPortas());
        carro.setTipoCombustivel(carroDTO.getTipoCombustivel());
        return carro;
    }
}