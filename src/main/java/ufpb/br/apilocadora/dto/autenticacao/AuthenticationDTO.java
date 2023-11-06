package ufpb.br.apilocadora.dto.autenticacao;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationDTO{

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
