package ufpb.br.apilocadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import ufpb.br.apilocadora.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    UserDetails findByEmail(String email);
}
