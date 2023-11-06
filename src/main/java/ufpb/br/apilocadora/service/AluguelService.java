package ufpb.br.apilocadora.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufpb.br.apilocadora.domain.Aluguel;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.domain.Usuario;
import ufpb.br.apilocadora.dto.aluguel.AluguelDTO;
import ufpb.br.apilocadora.dto.aluguel.AluguelMapper;
import ufpb.br.apilocadora.dto.carro.CarroDTO;
import ufpb.br.apilocadora.dto.carro.CarroMapper;
import ufpb.br.apilocadora.repository.AluguelRepository;
import ufpb.br.apilocadora.repository.CarroRepository;
import ufpb.br.apilocadora.repository.UsuarioRepository;
import ufpb.br.apilocadora.security.TokenService;
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
    public boolean update(Long id, AluguelDTO newAluguelDTO) {
        Optional<Aluguel> aluguelOptional = aluguelRepository.findById(id);
        if (aluguelOptional.isPresent()) {
            Aluguel aluguel = aluguelOptional.get();
            BeanUtils.copyProperties(newAluguelDTO, aluguel, "id");
            aluguelRepository.save(aluguel);
            return true; // Indicar que a atualização foi bem-sucedida
        } else {
            return false; // Indicar que o aluguel não foi encontrado
        }
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
