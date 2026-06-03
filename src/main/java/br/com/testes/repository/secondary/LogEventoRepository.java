package br.com.testes.repository.secondary;

import br.com.testes.entity.LogEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogEventoRepository extends JpaRepository<LogEvento, Long> {

    List<LogEvento> findByTipo(String tipo);
}
