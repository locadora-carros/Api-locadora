package ufpb.br.apilocadora.domain;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufpb.br.apilocadora.dto.carro.CarroDTO;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "carros")
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String chassi;

    private String cor;

    private int quantPortas;

    private String tipoCombustivel;

    private Boolean estaALugado;


    public Carro (CarroDTO carroDTO){
        this.nome = carroDTO.getNome();
        this.chassi = carroDTO.getChassi();
        this.cor = carroDTO.getCor();
        this.quantPortas = carroDTO.getQuantPortas();
        this.tipoCombustivel = carroDTO.getTipoCombustivel();
        this.estaALugado = false;
    }

}


