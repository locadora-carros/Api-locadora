package ufpb.br.apilocadora.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufpb.br.apilocadora.domain.Aluguel;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.domain.Usuario;
import ufpb.br.apilocadora.dto.aluguel.AluguelDTO;
import ufpb.br.apilocadora.dto.aluguel.AluguelMapper;
import ufpb.br.apilocadora.repository.AluguelRepository;
import ufpb.br.apilocadora.repository.CarroRepository;
import ufpb.br.apilocadora.repository.UsuarioRepository;
import ufpb.br.apilocadora.service.exception.ObjectAlreadyExistException;
import ufpb.br.apilocadora.service.exception.ObjectNotFoundException;

import java.util.Optional;

@Service
public class AluguelService {

    @Autowired
    private AluguelMapper aluguelMapper;

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    UsuarioRepository usuarioRepository;


    public AluguelDTO findById(String id) {
        Optional<Aluguel> aluguelOptional = aluguelRepository.findById(Long.valueOf(id));

        Aluguel aluguel = aluguelOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Aluguel não encontrado! id: " + id + ", Tipo: " + Carro.class.getName()));

        return aluguelMapper.toDto(aluguel);

    }

    public Page<AluguelDTO> listAll(Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Aluguel> alugueisPage = aluguelRepository.findAll(pageable);

        return alugueisPage.map(aluguelMapper::toDto);
    }


    @Transactional
    public void save(AluguelDTO aluguelDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Optional<Carro> optionalCarro = carroRepository.findByChassi(aluguelDTO.getChassi());

        if (optionalCarro.isPresent()) {
            Carro carro = optionalCarro.get();

            if (carro.getEstaALugado()) {
                throw new ObjectAlreadyExistException(
                        "Carro "+ carro.getNome() +" já está alugado, Tipo: " + Carro.class.getName());
            }
            carro.setEstaALugado(true);
            carroRepository.save(carro);

            Aluguel aluguel = aluguelMapper.toEntity(aluguelDTO, carro, usuario);
            aluguelRepository.save(aluguel);
        } else {
            throw new ObjectNotFoundException(
                    "Carro não encontrado! Chassi: " + aluguelDTO.getChassi() + ", Tipo: " + Carro.class.getName());
        }
    }

    @Transactional
    public void update(String id, AluguelDTO newAluguelDTO) throws ObjectNotFoundException {
        Optional<Aluguel> aluguelOptional = aluguelRepository.findById(Long.valueOf(id));
        Aluguel aluguel = aluguelOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Aluguel não encontrado! id: " + id + ", Tipo: " + Carro.class.getName()));

        BeanUtils.copyProperties(newAluguelDTO, aluguel, "id");
        aluguelRepository.save(aluguel);
    }

    @Transactional
    public void delete(String id) throws ObjectNotFoundException {
        Optional<Aluguel> aluguelOptional = aluguelRepository.findById(Long.valueOf(id));
        Aluguel aluguel = aluguelOptional.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Aluguel não encontrado! id: " + id + ", Tipo: " + Carro.class.getName()));

        aluguelRepository.delete(aluguel);

    }
}
//