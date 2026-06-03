package br.com.testes;

import br.com.testes.entity.Pessoa;
import br.com.testes.service.AnalisadorGrupoService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnalisadorGrupoTest {

    @Autowired
    private AnalisadorGrupoService analisador;

    @BeforeEach
    void setup() {
        // Limpa o banco antes de cada teste
        analisador.listarTodos()
                  .forEach(p -> analisador.excluirPorNome(p.getNome()));

        analisador.adicionar(new Pessoa("Maria",   Pessoa.Genero.FEMININO));
        analisador.adicionar(new Pessoa("Ana",     Pessoa.Genero.FEMININO));
        analisador.adicionar(new Pessoa("João",    Pessoa.Genero.MASCULINO));
        analisador.adicionar(new Pessoa("Jessica", Pessoa.Genero.FEMININO));
    }

    // ══════════════════════════════════════════════════════════
    // temApenasMulheres
    // ══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("temApenasMulheres")
    class TemApenasMulheresTest {

        @Test
        @DisplayName("retorna true quando a lista contém apenas mulheres")
        void deveRetornarTrueQuandoListaContemApenasMulheres() {
            List<Pessoa> apenasMulheres = List.of(
                    new Pessoa("Maria",   Pessoa.Genero.FEMININO),
                    new Pessoa("Ana",     Pessoa.Genero.FEMININO),
                    new Pessoa("Jessica", Pessoa.Genero.FEMININO)
            );
            assertTrue(analisador.temApenasMulheres(apenasMulheres),
                    "A lista deveria conter apenas mulheres.");
        }

        @Test
        @DisplayName("retorna false quando a lista contém homens e mulheres")
        void deveRetornarFalseQuandoListaContemHomensEMulheres() {
            List<Pessoa> grupoMisto = List.of(
                    new Pessoa("Maria", Pessoa.Genero.FEMININO),
                    new Pessoa("João",  Pessoa.Genero.MASCULINO),
                    new Pessoa("Ana",   Pessoa.Genero.FEMININO)
            );
            assertFalse(analisador.temApenasMulheres(grupoMisto),
                    "A lista não deveria passar, pois contém homens.");
        }

        @Test
        @DisplayName("retorna false para lista nula")
        void deveRetornarFalseParaListaNula() {
            assertFalse(analisador.temApenasMulheres(null));
        }

        @Test
        @DisplayName("retorna false para lista vazia")
        void deveRetornarFalseParaListaVazia() {
            assertFalse(analisador.temApenasMulheres(List.of()));
        }
    }

    // ══════════════════════════════════════════════════════════
    // buscarPorNome
    // ══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("buscarPorNome")
    class BuscarPorNomeTest {

        @Test
        @DisplayName("encontra pessoa existente pelo nome")
        void deveEncontrarPessoaExistente() {
            Optional<Pessoa> resultado = analisador.buscarPorNome("Maria");
            assertTrue(resultado.isPresent(), "Deveria encontrar 'Maria'.");
            assertEquals("Maria", resultado.get().getNome());
            assertEquals(Pessoa.Genero.FEMININO, resultado.get().getGenero());
        }

        @Test
        @DisplayName("busca é case-insensitive")
        void deveBuscarSemDistincaoDeMaiusculas() {
            Optional<Pessoa> resultado = analisador.buscarPorNome("JOÃO");
            assertTrue(resultado.isPresent(),
                    "Deveria encontrar 'João' independente de caixa.");
        }

        @Test
        @DisplayName("retorna Optional vazio para nome inexistente")
        void deveRetornarVazioParaNomeInexistente() {
            Optional<Pessoa> resultado = analisador.buscarPorNome("Carlos");
            assertTrue(resultado.isEmpty(), "Não deveria encontrar 'Carlos'.");
        }

        @Test
        @DisplayName("lança exceção para nome nulo")
        void deveLancarExcecaoParaNomeNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> analisador.buscarPorNome(null));
        }
    }

    // ══════════════════════════════════════════════════════════
    // buscarPorGenero
    // ══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("buscarPorGenero")
    class BuscarPorGeneroTest {

        @Test
        @DisplayName("retorna apenas pessoas do gênero feminino")
        void deveRetornarApenasMultheres() {
            List<Pessoa> mulheres = analisador.buscarPorGenero(Pessoa.Genero.FEMININO);
            assertEquals(3, mulheres.size());
            assertTrue(mulheres.stream()
                    .allMatch(p -> p.getGenero() == Pessoa.Genero.FEMININO));
        }

        @Test
        @DisplayName("retorna apenas pessoas do gênero masculino")
        void deveRetornarApenasHomens() {
            List<Pessoa> homens = analisador.buscarPorGenero(Pessoa.Genero.MASCULINO);
            assertEquals(1, homens.size());
            assertEquals("João", homens.get(0).getNome());
        }

        @Test
        @DisplayName("lança exceção para gênero nulo")
        void deveLancarExcecaoParaGeneroNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> analisador.buscarPorGenero(null));
        }
    }

    // ══════════════════════════════════════════════════════════
    // excluirPorNome
    // ══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("excluirPorNome")
    class ExcluirPorNomeTest {

        @Test
        @DisplayName("remove pessoa existente e retorna true")
        void deveRemoverPessoaExistente() {
            boolean removido = analisador.excluirPorNome("Ana");
            assertTrue(removido, "Deveria retornar true ao remover 'Ana'.");
            assertTrue(analisador.buscarPorNome("Ana").isEmpty(),
                    "'Ana' não deveria mais existir.");
        }

        @Test
        @DisplayName("retorna false para nome inexistente")
        void deveRetornarFalseParaNomeInexistente() {
            boolean removido = analisador.excluirPorNome("Carlos");
            assertFalse(removido, "Deveria retornar false para 'Carlos'.");
            assertEquals(4, analisador.listarTodos().size(),
                    "Tamanho da lista não deveria mudar.");
        }

        @Test
        @DisplayName("exclusão é case-insensitive")
        void deveExcluirSemDistincaoDeMaiusculas() {
            boolean removido = analisador.excluirPorNome("MARIA");
            assertTrue(removido);
            assertEquals(3, analisador.listarTodos().size());
        }

        @Test
        @DisplayName("lança exceção para nome nulo")
        void deveLancarExcecaoParaNomeNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> analisador.excluirPorNome(null));
        }
    }

    // ══════════════════════════════════════════════════════════
    // atualizarGenero
    // ══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("atualizarGenero")
    class AtualizarGeneroTest {

        @Test
        @DisplayName("atualiza gênero de pessoa existente e retorna true")
        void deveAtualizarGeneroDeExistente() {
            boolean atualizado = analisador.atualizarGenero("João", Pessoa.Genero.FEMININO);
            assertTrue(atualizado, "Deveria retornar true ao atualizar 'João'.");

            Optional<Pessoa> joao = analisador.buscarPorNome("João");
            assertTrue(joao.isPresent());
            assertEquals(Pessoa.Genero.FEMININO, joao.get().getGenero(),
                    "Gênero de 'João' deveria ser FEMININO.");
        }

        @Test
        @DisplayName("retorna false para nome inexistente")
        void deveRetornarFalseParaNomeInexistente() {
            boolean atualizado = analisador.atualizarGenero("Carlos", Pessoa.Genero.MASCULINO);
            assertFalse(atualizado, "Deveria retornar false para 'Carlos'.");
        }

        @Test
        @DisplayName("lança exceção para nome nulo")
        void deveLancarExcecaoParaNomeNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> analisador.atualizarGenero(null, Pessoa.Genero.MASCULINO));
        }

        @Test
        @DisplayName("lança exceção para gênero nulo")
        void deveLancarExcecaoParaGeneroNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> analisador.atualizarGenero("Maria", null));
        }

        @Test
        @DisplayName("lista permanece com mesmo tamanho após atualização")
        void listaNaoDeveAlterarTamanhoAposAtualizacao() {
            analisador.atualizarGenero("Maria", Pessoa.Genero.MASCULINO);
            assertEquals(4, analisador.listarTodos().size(),
                    "Tamanho da lista não deve mudar após atualização.");
        }
    }

    // ══════════════════════════════════════════════════════════
    // Testes dos 3 bancos H2
    // ══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Banco 2 - Logs (secundário)")
    class LogsTest {

        @Test
        @DisplayName("operações gravam logs no banco secundário")
        void deveGravarLogsNoBancoSecundario() {
            long antes = analisador.listarLogs().size();
            analisador.adicionar(new Pessoa("Novo", Pessoa.Genero.MASCULINO));
            assertTrue(analisador.listarLogs().size() > antes,
                    "Deveria ter gravado ao menos 1 log.");
        }
    }

    @Nested
    @DisplayName("Banco 3 - Relatórios (terciário)")
    class RelatoriosTest {

        @Test
        @DisplayName("snapshot é gerado após operação")
        void deveGerarSnapshotNoBancoTerciario() {
            analisador.adicionar(new Pessoa("Extra", Pessoa.Genero.FEMININO));
            assertFalse(analisador.listarRelatorios().isEmpty(),
                    "Deveria existir ao menos um relatório.");
        }

        @Test
        @DisplayName("relatório reflete contagem correta de pessoas")
        void relatorioDeveReflitirContagemCorreta() {
            // setup já adicionou 4 pessoas; adiciona mais 1
            analisador.adicionar(new Pessoa("Extra", Pessoa.Genero.FEMININO));
            int total = analisador.listarRelatorios().get(0).getTotalPessoas();
            assertEquals(5, total, "Relatório deveria registrar 5 pessoas.");
        }
    }
}
