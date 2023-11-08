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
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.domain.Usuario;
import ufpb.br.apilocadora.dto.aluguel.AluguelDTO;
import ufpb.br.apilocadora.dto.autenticacao.AuthenticationDTO;
import ufpb.br.apilocadora.dto.autenticacao.LoginResponseDTO;
import ufpb.br.apilocadora.dto.carro.CarroDTO;
import ufpb.br.apilocadora.dto.carro.CarroInfoSimplesDTO;
import ufpb.br.apilocadora.dto.carro.CarroMapper;
import ufpb.br.apilocadora.repository.CarroRepository;
import ufpb.br.apilocadora.repository.UsuarioRepository;
import ufpb.br.apilocadora.util.CarroCreator;
import ufpb.br.apilocadora.util.UsuarioCreator;
import ufpb.br.apilocadora.wrapper.PageableResponse;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarroControllerTest extends ContainersEnvironment {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarroMapper carroMapper;

    private static final String BASE_URL_CARRO = "/api/carros";

    private static final String BASE_URL_AUTH = "/api/auth";

    @BeforeEach
    void setUp() {
        carroRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void findCarroByChassi_retornaStatusOk_quandoTemSucesso(){
        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.getForEntity(
                BASE_URL_CARRO + "/{chassi}",
                AluguelDTO.class,
                carro.getChassi()
        );

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        Assertions.assertThat(resposta.getBody())
                .isNotNull();
    }

    @Test
    void findCarroByChassi_retornaStatuNotFound_quandoIdNaoFoiEncontrado(){
        String id = "1";

        ResponseEntity<AluguelDTO> resposta = testRestTemplate.getForEntity(
                BASE_URL_CARRO + "/{chassi}",
                AluguelDTO.class,
                id
        );

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllCarros_retornaUmaPaginaDeCarros_quandoTemSucesso(){
        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        int quantCarrossSalvos = 1;

        PageableResponse<CarroInfoSimplesDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<CarroInfoSimplesDTO>>() {
                }).getBody();

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getNumberOfElements())
                .isEqualTo(quantCarrossSalvos);
    }

    @Test
    void findAllCarros_retornaUmaPaginaDeCarrosVazia_quandoNenhumCarrosFoiCadastrado(){
        int quantCarrossSalvos = 0;

        PageableResponse<AluguelDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<AluguelDTO>>() {
                }).getBody();

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getNumberOfElements())
                .isEqualTo(quantCarrossSalvos);
    }

    @Test
    void findAllByNome_retornaUmaPaginaDeCarros_quandoTemSucesso(){
        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        int quantCarrossSalvos = 1;

        PageableResponse<CarroDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO + "/nome" + "?value=" + carro.getNome(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<CarroDTO>>() {
                }).getBody();

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getNumberOfElements())
                .isEqualTo(quantCarrossSalvos);

        Assertions.assertThat(resposta.toList().get(0).getNome())
                .isNotNull()
                .isEqualTo(carro.getNome());
    }

    @Test
    void findAllByNome_retornaStatusNotFoun_quandoNenhumCarrosFoiEncontrado(){
        String nomeDiferente = "carroTeste";

        ResponseEntity<CarroDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO + "/nome" + "?value=" + nomeDiferente,
                HttpMethod.GET,
                null,
                CarroDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void saveCarro_retornaStatusCreated_quandoTemSucesso(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        CarroDTO carroDTO = carroMapper.toDto(CarroCreator.defaultCarro());

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ login.getBody().getToken());

        ResponseEntity<CarroDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO,
                HttpMethod.POST,
                new HttpEntity<>(carroDTO, headers),
                CarroDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void saveCarro_retornaStatusForbidden_quandoUsuarioNaoTemAcesso(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioUser());

        CarroDTO carroDTO = carroMapper.toDto(CarroCreator.defaultCarro());

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ login.getBody().getToken());

        ResponseEntity<CarroDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO,
                HttpMethod.POST,
                new HttpEntity<>(carroDTO, headers),
                CarroDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void updateCarro_retornaStatusNoContent_quandoTemSucesso(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        CarroDTO carroDTO = carroMapper.toDto(carro);
        carroDTO.setNome("Carro teste");

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ login.getBody().getToken());

        ResponseEntity<CarroDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO + "?chassi=" + carro.getChassi(),
                HttpMethod.PUT,
                new HttpEntity<>(carroDTO, headers),
                CarroDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void updateCarro_retornaStatusNotFound_quandoIdNaoFoiEncontrado(){
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        CarroDTO carroDTO = carroMapper.toDto(CarroCreator.defaultCarro());

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH+"/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ login.getBody().getToken());

        ResponseEntity<CarroDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO + "?chassi=1",
                HttpMethod.PUT,
                new HttpEntity<>(carroDTO, headers),
                CarroDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteCarro_retornaStatusNoContent_quandoTemSucesso() {
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        Carro carro = carroRepository.save(CarroCreator.defaultCarro());

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH + "/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + login.getBody().getToken());

        ResponseEntity<CarroDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO + "?chassi=" + carro.getChassi(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                CarroDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteCarro_retornaStatusNotFound_quandoIdNaoFoiEncontrado() {
        Usuario usuario = usuarioRepository.save(UsuarioCreator.defaultUsuarioComSenhaEncriptada());

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(usuario.getEmail(),
                UsuarioCreator.defaultUsuarioAdmin().getPassword());

        ResponseEntity<LoginResponseDTO> login = testRestTemplate.postForEntity(
                BASE_URL_AUTH + "/login", authenticationDTO, LoginResponseDTO.class);

        Assertions.assertThat(login.getBody()).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + login.getBody().getToken());

        ResponseEntity<CarroDTO> resposta = testRestTemplate.exchange(
                BASE_URL_CARRO + "?chassi=1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                CarroDTO.class
        );

        Assertions.assertThat(resposta)
                .isNotNull();

        Assertions.assertThat(resposta.getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
