package ufpb.br.apilocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.dto.CarroDTO;
import ufpb.br.apilocadora.service.CarroService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping("/{chassi}")
    public Carro getCarroByChassi (@PathVariable("chassi") String chassi) {
        return carroService.findByChassi(chassi);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@RequestBody CarroDTO carroDTO) {
        carroService.save(carroDTO);
    }

}
