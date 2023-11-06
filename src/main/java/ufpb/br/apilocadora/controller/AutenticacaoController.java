package ufpb.br.apilocadora.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ufpb.br.apilocadora.dto.autenticacao.AuthenticationDTO;
import ufpb.br.apilocadora.dto.autenticacao.LoginResponseDTO;
import ufpb.br.apilocadora.dto.autenticacao.RegistrarDTO;
import ufpb.br.apilocadora.service.AuthorizationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(@RequestBody @Valid AuthenticationDTO authenticationDTO){

        return authorizationService.logar(authenticationDTO);
    }

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrar(@RequestBody @Valid RegistrarDTO RegistrarDTO) {

        authorizationService.save(RegistrarDTO);
    }
}