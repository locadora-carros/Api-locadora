package ufpb.br.apilocadora.dto.aluguel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AluguelDTO {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime inicioAluguel;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime fimAluguel;

    private String formaPagamento;

    private Double valor;

    private String chassi;
}
