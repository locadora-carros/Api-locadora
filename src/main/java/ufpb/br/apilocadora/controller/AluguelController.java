package ufpb.br.apilocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ufpb.br.apilocadora.domain.Aluguel;
import ufpb.br.apilocadora.dto.aluguel.AluguelDTO;
import ufpb.br.apilocadora.dto.carro.CarroDTO;
import ufpb.br.apilocadora.service.AluguelService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    //Create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAluguel(@RequestBody AluguelDTO aluguelDTO) {
        aluguelService.save(aluguelDTO);
    }

    //Read
    @GetMapping("/{id}")
    public ResponseEntity<AluguelDTO> getAluguel(@PathVariable Long id){
        AluguelDTO aluguelDTO = aluguelService.findById(id);
        if(aluguelDTO != null){
            return ResponseEntity.ok(aluguelDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAluguel(@PathVariable Long id, @RequestBody AluguelDTO aluguelDTO){
        boolean updated = aluguelService.update(id, aluguelDTO);
        if(updated){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            aluguelService.delete(id);
            return ResponseEntity.ok().build();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }






}
