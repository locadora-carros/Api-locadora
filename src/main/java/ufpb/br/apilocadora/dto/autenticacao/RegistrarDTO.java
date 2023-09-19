package ufpb.br.apilocadora.dto.autenticacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufpb.br.apilocadora.domain.UserRole;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrarDTO {

    private String login;

    private String password;

    private String email;

    private UserRole role;
}