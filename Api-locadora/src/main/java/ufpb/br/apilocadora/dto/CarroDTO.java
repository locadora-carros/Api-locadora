package ufpb.br.apilocadora.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarroDTO {

    private String nome;

    private String chassi;

    private String cor;

    private String quantPortas;

    private String tipoCombustivel;

}
