package br.com.testes.repository.primary;

import br.com.testes.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByNomeIgnoreCase(String nome);

    List<Pessoa> findByGenero(Pessoa.Genero genero);

    boolean existsByNomeIgnoreCase(String nome);

    void deleteByNomeIgnoreCase(String nome);
}
