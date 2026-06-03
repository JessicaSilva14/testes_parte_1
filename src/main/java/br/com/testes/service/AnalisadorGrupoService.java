package br.com.testes.service;

import br.com.testes.entity.*;
import br.com.testes.repository.primary.PessoaRepository;
import br.com.testes.repository.secondary.LogEventoRepository;
import br.com.testes.repository.tertiary.RelatorioGrupoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Replica toda a lógica do AnalisadorGrupo original,
 * agora com persistência em 3 bancos H2 em memória:
 *
 *   Banco 1 (primary)   → pessoas_db   — CRUD de Pessoa
 *   Banco 2 (secondary) → logs_db      — Log de cada operação
 *   Banco 3 (tertiary)  → relatorios_db — Snapshot diário do grupo
 */
@Service
public class AnalisadorGrupoService {

    private final PessoaRepository      pessoaRepo;
    private final LogEventoRepository   logRepo;
    private final RelatorioGrupoRepository relatorioRepo;

    public AnalisadorGrupoService(PessoaRepository pessoaRepo,
                                  LogEventoRepository logRepo,
                                  RelatorioGrupoRepository relatorioRepo) {
        this.pessoaRepo    = pessoaRepo;
        this.logRepo       = logRepo;
        this.relatorioRepo = relatorioRepo;
    }

    // ── temApenasMulheres ────────────────────────────────────────────────────

    public boolean temApenasMulheres(List<Pessoa> lista) {
        if (lista == null || lista.isEmpty()) return false;
        return lista.stream()
                .allMatch(p -> p.getGenero() == Pessoa.Genero.FEMININO);
    }

    // ── adicionar ────────────────────────────────────────────────────────────

    @Transactional("primaryTransactionManager")
    public Pessoa adicionar(Pessoa pessoa) {
        if (pessoa == null)
            throw new IllegalArgumentException("Pessoa não pode ser nula.");
        Pessoa salva = pessoaRepo.save(pessoa);
        salvarLog("INFO", "Pessoa adicionada: " + salva.getNome());
        atualizarRelatorio();
        return salva;
    }

    // ── listarTodos ──────────────────────────────────────────────────────────

    public List<Pessoa> listarTodos() {
        return pessoaRepo.findAll();
    }

    // ── buscarPorNome ────────────────────────────────────────────────────────

    public Optional<Pessoa> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        return pessoaRepo.findByNomeIgnoreCase(nome);
    }

    // ── buscarPorGenero ──────────────────────────────────────────────────────

    public List<Pessoa> buscarPorGenero(Pessoa.Genero genero) {
        if (genero == null)
            throw new IllegalArgumentException("Gênero não pode ser nulo.");
        return pessoaRepo.findByGenero(genero);
    }

    // ── excluirPorNome ───────────────────────────────────────────────────────

    @Transactional("primaryTransactionManager")
    public boolean excluirPorNome(String nome) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        if (!pessoaRepo.existsByNomeIgnoreCase(nome)) return false;
        pessoaRepo.deleteByNomeIgnoreCase(nome);
        salvarLog("WARN", "Pessoa excluída: " + nome);
        atualizarRelatorio();
        return true;
    }

    // ── atualizarGenero ──────────────────────────────────────────────────────

    @Transactional("primaryTransactionManager")
    public boolean atualizarGenero(String nome, Pessoa.Genero novoGenero) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        if (novoGenero == null)
            throw new IllegalArgumentException("Gênero não pode ser nulo.");

        Optional<Pessoa> encontrada = pessoaRepo.findByNomeIgnoreCase(nome);
        if (encontrada.isEmpty()) return false;

        Pessoa p = encontrada.get();
        p.setGenero(novoGenero);
        pessoaRepo.save(p);
        salvarLog("INFO", "Gênero atualizado: " + nome + " → " + novoGenero);
        atualizarRelatorio();
        return true;
    }

    // ── banco secundário: log ────────────────────────────────────────────────

    @Transactional("secondaryTransactionManager")
    public void salvarLog(String tipo, String mensagem) {
        logRepo.save(new LogEvento(tipo, mensagem));
    }

    public List<LogEvento> listarLogs() {
        return logRepo.findAll();
    }

    // ── banco terciário: relatório ───────────────────────────────────────────

    @Transactional("tertiaryTransactionManager")
    public void atualizarRelatorio() {
        List<Pessoa> todos  = pessoaRepo.findAll();
        long feminino       = todos.stream()
                .filter(p -> p.getGenero() == Pessoa.Genero.FEMININO).count();
        long masculino      = todos.size() - feminino;
        boolean soMulheres  = !todos.isEmpty() && masculino == 0;

        RelatorioGrupo rel = relatorioRepo.findByData(LocalDate.now())
                .orElse(new RelatorioGrupo(LocalDate.now(), 0, 0, 0, false));

        rel.setTotalPessoas(todos.size());
        rel.setTotalFeminino((int) feminino);
        rel.setTotalMasculino((int) masculino);
        rel.setApenasMultheres(soMulheres);
        relatorioRepo.save(rel);
    }

    public List<RelatorioGrupo> listarRelatorios() {
        return relatorioRepo.findAll();
    }
}
