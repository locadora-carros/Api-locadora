package ufpb.br.apilocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufpb.br.apilocadora.domain.Carro;
import ufpb.br.apilocadora.dto.CarroDTO;
import ufpb.br.apilocadora.dto.CarroInfoSimplesDTO;
import ufpb.br.apilocadora.service.CarroService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping("/chassi")
    public ResponseEntity<CarroDTO> getCarroByChassi (
            @RequestParam(value = "value") String chassi) {

        return ResponseEntity.ok().body(carroService.findByChassi(chassi));
    }

    @GetMapping()
    public ResponseEntity<Page<CarroInfoSimplesDTO>> getAllCarros(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "24") Integer pageSize) {

        return ResponseEntity.ok(carroService.listAll(page, pageSize));
    }

    @GetMapping("/nome")
    public ResponseEntity<Page<CarroDTO>> getAllByNome(
            @RequestParam(value = "value") String nome,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "24") Integer pageSize) {

        return ResponseEntity.ok().body(carroService.listAllByNome(nome, page, pageSize));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCarro(@RequestBody CarroDTO carroDTO) {
        carroService.save(carroDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateCarro(@RequestParam(value = "chassi") String chassi,
                            @RequestBody CarroDTO carroDTO){
        carroService.update(chassi, carroDTO);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCarro(@RequestParam(value = "chassi") String chassi ){
        carroService.delete(chassi);
    }


}
