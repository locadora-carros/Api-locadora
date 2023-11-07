package ufpb.br.apilocadora.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ufpb.br.apilocadora.domain.Aluguel;
import ufpb.br.apilocadora.domain.Usuario;
import ufpb.br.apilocadora.domain.enums.UsuarioRole;

import java.util.ArrayList;
import java.util.List;

public class UsuarioCreator {

    private static final String email = "teste@teste.com";
    private static final String senha = "12345678";
    private static final String nome = "teste";
    private static final String sobreNome = "testando";
    private static final UsuarioRole admin = UsuarioRole.ADMIN;
    private static final UsuarioRole user = UsuarioRole.USER;
    private static final List<Aluguel> alugueis = new ArrayList<>();


    public static Usuario defaultUsuarioAdmin() {

        return new Usuario(null, email, senha, nome, sobreNome, admin, alugueis);
    }

    public static Usuario defaultUsuarioUser() {
        String senhaEncriptada = new BCryptPasswordEncoder().encode(senha);

        return new Usuario(null, email, senhaEncriptada, nome, sobreNome, user, alugueis);
    }

    public static Usuario defaultUsuarioComSenhaEncriptada() {
        String senhaEncriptada = new BCryptPasswordEncoder().encode(senha);

        return new Usuario(null, email, senhaEncriptada, nome, sobreNome, admin, alugueis);
    }
}
