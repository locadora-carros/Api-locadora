package ufpb.br.apilocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ufpb.br.apilocadora.dto.aluguel.AluguelDTO;
import ufpb.br.apilocadora.service.AluguelService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {
    
    @Autowired
    private AluguelService aluguelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAluguel(@RequestBody AluguelDTO aluguelDTO) {
        aluguelService.save(aluguelDTO);
    }
}
