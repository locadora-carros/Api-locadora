package ufpb.br.apilocadora.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufpb.br.apilocadora.domain.Aluguel;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.dto.aluguel.AluguelDTO;
import ufpb.br.apilocadora.dto.aluguel.AluguelMapper;
import ufpb.br.apilocadora.dto.carro.CarroDTO;
import ufpb.br.apilocadora.dto.carro.CarroMapper;
import ufpb.br.apilocadora.repository.AluguelRepository;
import ufpb.br.apilocadora.repository.CarroRepository;
import ufpb.br.apilocadora.service.exception.ObjectAlreadyExistException;
import ufpb.br.apilocadora.service.exception.ObjectNotFoundException;

import java.util.Optional;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private AluguelMapper aluguelMapper;

    @Autowired
    private CarroRepository carroRepository;


    @Transactional
    public void save(AluguelDTO aluguelDTO) {
        Optional<Carro> optionalCarro = carroRepository.findByChassi(aluguelDTO.getChassi());

        if (optionalCarro.isPresent()) {
            Carro carro = optionalCarro.get();

            if (carro.getEstaALugado()) {
                throw new ObjectAlreadyExistException(
                        "Carro já está alugado" + carro.getNome() + ", Tipo: " + Carro.class.getName());
            }
            carro.setEstaALugado(true);
            carroRepository.save(carro);

            Aluguel aluguel = aluguelMapper.toEntity(aluguelDTO, carro);
            aluguelRepository.save(aluguel);
        } else {
            throw new ObjectNotFoundException(
                    "Carro não encontrado! Chassi: " + aluguelDTO.getChassi() + ", Tipo: " + Carro.class.getName());
        }
    }
}
