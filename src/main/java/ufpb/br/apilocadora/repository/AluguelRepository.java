package ufpb.br.apilocadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufpb.br.apilocadora.domain.Aluguel;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel,Long> {
}
