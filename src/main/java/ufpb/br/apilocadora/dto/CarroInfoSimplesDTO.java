package ufpb.br.apilocadora.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarroInfoSimplesDTO {

    private String nome;

    private String cor;

    private int quantPortas;

}