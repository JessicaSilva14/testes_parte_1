package br.com.testes.repository.tertiary;

import br.com.testes.entity.RelatorioGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RelatorioGrupoRepository extends JpaRepository<RelatorioGrupo, Long> {

    Optional<RelatorioGrupo> findByData(LocalDate data);
}
