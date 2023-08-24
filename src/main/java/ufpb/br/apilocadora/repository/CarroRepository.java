package ufpb.br.apilocadora.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufpb.br.apilocadora.domain.Carro;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarroRepository extends JpaRepository<Carro,Long> {
    Optional<Carro> findByChassi(String chassi);

    Page<Carro> findAllByNomeContaining(String nome, Pageable pageable);
}
