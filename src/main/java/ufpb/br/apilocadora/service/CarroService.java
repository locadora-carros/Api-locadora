package ufpb.br.apilocadora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.dto.CarroDTO;
import ufpb.br.apilocadora.repository.CarroRepository;
import ufpb.br.apilocadora.service.exception.ObjectNotFoundException;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public Carro findByChassi (String chassi){
        return carroRepository.findByChassi(chassi)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Carro não encontrado! Id: " + chassi + ", Tipo: " + Carro.class.getName()));
    }

    public void save (CarroDTO carroDTO){

        Carro carro = new Carro(carroDTO);
        carroRepository.save(carro);
    }
}
