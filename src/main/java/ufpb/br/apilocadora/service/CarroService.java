package ufpb.br.apilocadora.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.domain.Usuario;
import ufpb.br.apilocadora.dto.carro.CarroDTO;
import ufpb.br.apilocadora.dto.carro.CarroInfoSimplesDTO;
import ufpb.br.apilocadora.dto.carro.CarroMapper;
import ufpb.br.apilocadora.repository.CarroRepository;
import ufpb.br.apilocadora.service.exception.InvalidUserException;
import ufpb.br.apilocadora.service.exception.ObjectNotFoundException;
import ufpb.br.apilocadora.service.exception.ObjectAlreadyExistException;

import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private CarroMapper carroMapper;


    public CarroDTO findByChassi(String chassi) {
        Optional<Carro> carroOptional = carroRepository.findByChassi(chassi);
        Carro carro = carroOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Carro não encontrado! Chassi: " + chassi + ", Tipo: " + Carro.class.getName()));

        return carroMapper.toDto(carro);
    }

    public Page<CarroInfoSimplesDTO> listAll(Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Carro> carrosPage = carroRepository.findAll(pageable);

        return carrosPage.map(carroMapper::carroInfoSimplesDTO);
    }

    public Page<CarroDTO> listAllByNome(String nome, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Carro> carrosPage = carroRepository.findAllByNomeContaining(nome, pageable);

        if (carrosPage.isEmpty()) {
            throw new ObjectNotFoundException(
                    "Nenhum carro encontrado pelo nome: " + nome + ", Tipo: " + Carro.class.getName()
            );
        }
        return carrosPage.map(carroMapper::toDto);
    }

    @Transactional
    public void save(CarroDTO carroDTO) {
        if (carroRepository.findByChassi(carroDTO.getChassi()).isPresent()) {
            throw new ObjectAlreadyExistException(
                    "Carro já adicionado. Chassi: " + carroDTO.getChassi() + ", Tipo: " + Carro.class.getName());
        }

        Carro carro = new Carro(carroDTO);
        carroRepository.save(carro);


    }

    @Transactional
    public void update(String chassi, CarroDTO newCarroDTO) throws ObjectNotFoundException {
        Optional<Carro> carroOptional = carroRepository.findByChassi(chassi);
        Carro carro = carroOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Carro não encontrado! Chassi: " + chassi + ", Tipo: " + Carro.class.getName()));


        BeanUtils.copyProperties(newCarroDTO, carro, "chassi");
        carroRepository.save(carro);
    }

    @Transactional
    public void delete(String chassi) throws ObjectNotFoundException {
        Optional<Carro> carroOptional = carroRepository.findByChassi(chassi);
        Carro carro = carroOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Carro não encontrado! Chassi: " + chassi + ", Tipo: " + Carro.class.getName()));

        carroRepository.delete(carro);

    }
}
