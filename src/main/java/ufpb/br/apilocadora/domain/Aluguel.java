package ufpb.br.apilocadora.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alugueis")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime inicioAluguel;

    private LocalDateTime fimAluguel;

    private String formaPagamento;

    private Double valor;

    @ManyToOne
    private Carro carro;

    @JoinColumn(name = "usuario_id")
    @ManyToOne
    private Usuario usuario;
}
