package ufpb.br.apilocadora.dto.carro;

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

    private int quantPortas;

    private String tipoCombustivel;

}
