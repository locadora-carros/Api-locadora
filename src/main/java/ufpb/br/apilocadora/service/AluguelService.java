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
    private CarroMapper carroMapper;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private CarroService carroService;


    @Transactional
    public void save(AluguelDTO aluguelDTO) {
        Carro carro = carroRepository.findByChassi(aluguelDTO.getChassi())
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Carro não encontrado! Chassi: " + aluguelDTO.getChassi() + ", Tipo: " + Carro.class.getName()));

        if (!carro.getEstaALugado()){
            throw new ObjectAlreadyExistException(
                    "Carro já está alugado" + carro.getNome() + ", Tipo: " + Carro.class.getName());
        }

        carro.setEstaALugado(true);
        carroService.update(carro.getChassi(), carroMapper.toDto(carro));

        Aluguel aluguel = aluguelMapper.toEntity(aluguelDTO, carro);

        aluguelRepository.save(aluguel);
    }

    @Transactional
    public void update(Long id, AluguelDTO newAluguelDTO) throws ObjectNotFoundException {
        Optional<Aluguel> aluguelOptional = aluguelRepository.findById(id);
        Aluguel aluguel = aluguelOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Aluguél não encontrado! id: " + id + ", Tipo: " + Carro.class.getName()));

        BeanUtils.copyProperties(newAluguelDTO, aluguel, "id");
        aluguelRepository.save(aluguel);
    }

    @Transactional
    public void delete(Long id) throws ObjectNotFoundException {
        Optional<Aluguel> aluguelOptional = aluguelRepository.findById(id);
        Aluguel aluguel = aluguelOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Aluguél não encontrado! id: " + id + ", Tipo: " + Aluguel.class.getName()));
        aluguelRepository.delete(aluguel);
    }

    public AluguelDTO findById(Long id) {
        Optional<Aluguel> aluguelOptional = aluguelRepository.findById(id);
        Aluguel aluguel = aluguelOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Aluguél não encontrado! id: " + id + ", Tipo: " + Aluguel.class.getName()));

        return aluguelMapper.toDto(aluguel);
    }

}
