package ufpb.br.apilocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufpb.br.apilocadora.dto.aluguel.AluguelDTO;
import ufpb.br.apilocadora.service.AluguelService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @GetMapping("/{id}")
    public ResponseEntity<AluguelDTO> findAluguelById (@PathVariable String id) {

        return ResponseEntity.ok().body(aluguelService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AluguelDTO>> findAllAlugueis(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "24") Integer pageSize) {

        return ResponseEntity.ok(aluguelService.listAll(page, pageSize));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAluguel(@RequestBody AluguelDTO aluguelDTO) {

        aluguelService.save(aluguelDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAluguel(@RequestParam(value = "value") String id,
                              @RequestBody AluguelDTO aluguelDTO){

        aluguelService.update(id, aluguelDTO);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAluguel(@RequestParam(value = "value") String id ){
        aluguelService.delete(id);
    }

}