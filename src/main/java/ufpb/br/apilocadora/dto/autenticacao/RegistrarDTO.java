package ufpb.br.apilocadora.dto.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufpb.br.apilocadora.domain.enums.UsuarioRole;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrarDTO {

    private String email;

    private String password;

    private String nome;

    private UsuarioRole role;
}