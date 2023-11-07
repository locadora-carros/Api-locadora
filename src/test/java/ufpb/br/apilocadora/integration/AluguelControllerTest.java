package ufpb.br.apilocadora.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ufpb.br.apilocadora.config.ContainersEnvironment;
import ufpb.br.apilocadora.domain.Aluguel;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.domain.Usuario;
import ufpb.br.apilocadora.dto.aluguel.AluguelDTO;
import ufpb.br.apilocadora.dto.aluguel.AluguelMapper;
import ufpb.br.apilocadora.dto.autenticacao.AuthenticationDTO;
import ufpb.br.apilocadora.dto.autenticacao.LoginResponseDTO;
import ufpb.br.apilocadora.repository.AluguelRepository;
import ufpb.br.apilocadora.repository.CarroRepository;
import ufpb.br.apilocadora.repository.UsuarioRepository;
import ufpb.br.apilocadora.util.AluguelCreator;
import ufpb.br.apilocadora.util.CarroCreator;
import ufpb.br.apilocadora.util.UsuarioCreator;
import ufpb.br.apilocadora.wrapper.PageableResponse;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AluguelControllerTest extends ContainersEnvironment {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AluguelMapper aluguelMapper;

    private static final String BASE_URL_ALUGUEL = "/api/alugueis";

    private static final String BASE_URL_AUTH = "/api/auth";

    @BeforeEach
    void setUp() {
        aluguelRepository.deleteAll();
        carroRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void findAluguelById_retornaStatusOk_quandoTemSucesso(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        Aluguel aluguel = aluguelRepository.save(AluguelCreator.defaultAluguel(carro, usuario));

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.getForEntity(
                BASE_URL_ALUGUEL + "/{id}",
                AluguelDTO.class,
                aluguel.getId()
        );

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        Assertions.assertThat(resposta.getBody())
                .isNotNull();
    }

    @Test
    void findAluguelById_retornaStatuNotFound_quandoIdNaoFoiEncontrado(){
        String id = "1";

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.getForEntity(
                BASE_URL_ALUGUEL + "/{id}",
                AluguelDTO.class,
                id
        );

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllAlugueis_retornaUmaPaginaDeAlugueis_quandoTemSucesso(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        Aluguel aluguel = aluguelRepository.save(AluguelCreator.defaultAluguel(carro, usuario));

        int quantAlugueisSalvos = 1;

        PageableResponse<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_ALUGUEL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<AluguelDTO>>() {
                }).getBody();

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getNumberOfElements())
                .isEqualTo(quantAlugueisSalvos);

        Assertions.assertThat(resposta.toList().get(0).getChassi())
                .isNotNull()
                .isEqualTo(aluguel.getCarro().getChassi());
    }

    @Test
    void findAllAlugueis_retornaUmaPaginaDeAlugueisVazia_quandoNenhumAluguelFoiCadastrado(){
        int quantAlugueisSalvos = 0;

        PageableResponse<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_ALUGUEL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<AluguelDTO>>() {
                }).getBody();

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getNumberOfElements())
                .isEqualTo(quantAlugueisSalvos);
    }

    @Test
    void saveAluguel_retornaStatusCreated_quandoTemSucesso(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        AluguelDTO aluguelDTO = aluguelMapper.toDto(AluguelCreator.defaultAluguel(carro, usuario));

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ login.getBody().getToken());

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_ALUGUEL,
                HttpMethod.POST,
                new HttpEntity<>(aluguelDTO, headers),
                AluguelDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void saveAluguel_retornaStatusForbidden_quandoUsuarioNaoTemAcesso(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioUser());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        AluguelDTO aluguelDTO = aluguelMapper.toDto(AluguelCreator.defaultAluguel(carro, usuario));

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ login.getBody().getToken());

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_ALUGUEL,
                HttpMethod.POST,
                new HttpEntity<>(aluguelDTO, headers),
                AluguelDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateAluguel_retornaStatusNoContent_quandoTemSucesso(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        Aluguel aluguel = aluguelRepository.save(AluguelCreator.defaultAluguel(carro, usuario));

        AluguelDTO aluguelDTO = aluguelMapper.toDto(aluguel);
        aluguelDTO.setValor(999.99);

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ login.getBody().getToken());

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_ALUGUEL + "?value=" + aluguel.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(aluguelDTO, headers),
                AluguelDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void updateAluguel_retornaStatusNotFound_quandoIdNaoFoiEncontrado(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        AluguelDTO aluguelDTO = aluguelMapper.toDto(AluguelCreator.defaultAluguel(carro, usuario));

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ login.getBody().getToken());

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_ALUGUEL + "?value=1",
                HttpMethod.PUT,
                new HttpEntity<>(aluguelDTO, headers),
                AluguelDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteAluguel_retornaStatusNoContent_quandoTemSucesso() {
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        Aluguel aluguel = aluguelRepository.save(AluguelCreator.defaultAluguel(carro, usuario));

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH + "/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + login.getBody().getToken());

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_ALUGUEL + "?value=" + aluguel.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                AluguelDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteAluguel_retornaStatusNotFound_quandoIdNaoFoiEncontrado() {
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH + "/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + login.getBody().getToken());

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_ALUGUEL + "?value=1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                AluguelDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
