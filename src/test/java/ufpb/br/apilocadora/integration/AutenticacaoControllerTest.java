package ufpb.br.apilocadora.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import ufpb.br.apilocadora.config.ContainersEnvironment;
import ufpb.br.apilocadora.domain.Usuario;
import ufpb.br.apilocadora.domain.enums.UsuarioRole;
import ufpb.br.apilocadora.dto.autenticacao.AuthenticationDTO;
import ufpb.br.apilocadora.dto.autenticacao.LoginResponseDTO;
import ufpb.br.apilocadora.dto.autenticacao.RegistrarDTO;
import ufpb.br.apilocadora.repository.UsuarioRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutenticacaoControllerTest extends ContainersEnvironment {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String BASE_URL = "/api/auth";

    @BeforeEach
    void setUp(){
        usuarioRepository.deleteAll();
    }

    @Test
    void login_retornaStatusOk_quandoLogadoComSucesso(){
        String senhaSemCriptografia = "12345678";
        String senhaEncriptada = new BCryptPasswordEncoder().encode(senhaSemCriptografia);

        UsuarioRole role = UsuarioRole.ADMIN;

        Usuario usuario = new Usuario("teste@teste.com", senhaEncriptada, "teste",
                "testando", role);

        usuarioRepository.save(usuario);

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(), senhaSemCriptografia);

        ResponseEntity<LoginResponseDTO> response = testRestTemplate.postForEntity(
                BASE_URL+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    void login_retornaStatusNotFound_quandoNaoEncontraUsuario(){
        UsuarioRole role = UsuarioRole.ADMIN;

        Usuario usuario = new Usuario("teste@teste.com", "12345678", "teste",
                "testando", role);

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(), usuario.getPassword());

        ResponseEntity<LoginResponseDTO> response = testRestTemplate.postForEntity(
                BASE_URL+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void registrar_retornaStatusOk_quandoLogadoComSucesso(){
        String senhaSemCriptografia = "12345678";
        UsuarioRole role = UsuarioRole.ADMIN;

        RegistrarDTO registrarDTO = new RegistrarDTO(
                "teste@teste.com", senhaSemCriptografia, senhaSemCriptografia, "teste",
                "testando", role);

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                BASE_URL+"/registrar", registrarDTO, Void.class);

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void registrar_retornaStatusBadRequest_quandoAsSenhasNaoSaoIguais(){
        String senha = "12345678";
        String senhaDiferente = "123";
        UsuarioRole role = UsuarioRole.ADMIN;

        RegistrarDTO registrarDTO = new RegistrarDTO(
                "teste@teste.com", senha, senhaDiferente, "teste",
                "testando", role);

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                BASE_URL+"/registrar", registrarDTO, Void.class);

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void registrar_retornaStatusBadRequest_quandoUsuarioJaEstaCadastrado(){
        String senhaSemCriptografia = "12345678";
        String senhaEncriptada = new BCryptPasswordEncoder().encode(senhaSemCriptografia);
        UsuarioRole role = UsuarioRole.ADMIN;

        Usuario usuario = new Usuario("teste@teste.com", senhaEncriptada, "teste",
                "testando",role);

        usuarioRepository.save(usuario);

        RegistrarDTO RegistrarDTO = new RegistrarDTO(
                usuario.getEmail(), senhaSemCriptografia, senhaSemCriptografia, usuario.getNome(),
                usuario.getSobreNome(), usuario.getRole());

        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                BASE_URL+"/registrar", RegistrarDTO, Void.class);

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}