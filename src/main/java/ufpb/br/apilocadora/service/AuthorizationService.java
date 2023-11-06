package ufpb.br.apilocadora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ufpb.br.apilocadora.repository.UsuarioRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UsuarioRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return usuarioRepository.findByEmail(email);
    }

    public LoginResponseDTO logar(AuthenticationDTO authenticationDTO){
        if(usuarioRepository.findByEmail(authenticationDTO.getEmail()) == null){
            throw new ObjectNotFoundException("Usuario não encontrado");
        }

        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

    @Transactional
    public void save(RegistrarDTO registrarDTO) {
        if(usuarioRepository.findByEmail(registrarDTO.getEmail()) != null){
            throw new ObjectAlreadyExistException("Usuario já registrado");
        }

//        if(!registrarDTO.getPassword().equals(registrarDTO.getConfirmPassword())){
//            throw new DifferentPassawordException("As senhas precisam ser iguais!");
//        }

        String senhaEncriptada = new BCryptPasswordEncoder().encode(registrarDTO.getPassword());
        Usuario newUsuario = new Usuario(
                registrarDTO.getEmail(), senhaEncriptada, registrarDTO.getNome(),
                registrarDTO.getSobreNome(), registrarDTO.getRole());

        usuarioRepository.save(newUsuario);

    }
}

