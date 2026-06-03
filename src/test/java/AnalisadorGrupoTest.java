import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class AnalisadorGrupoTest {

    private AnalisadorGrupo analisador;

    @BeforeEach
    public void setup() {
        analisador = new AnalisadorGrupo();
        analisador.adicionar(new Pessoa("Maria",   Pessoa.Genero.FEMININO));
        analisador.adicionar(new Pessoa("Ana",     Pessoa.Genero.FEMININO));
        analisador.adicionar(new Pessoa("João",    Pessoa.Genero.MASCULINO));
        analisador.adicionar(new Pessoa("Jessica", Pessoa.Genero.FEMININO));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // temApenasMulheres (testes originais)
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("temApenasMulheres")
    class TemApenasMulheresTest {

        @Test
        @DisplayName("retorna true quando a lista contém apenas mulheres")
        public void deveRetornarTrueQuandoListaContemApenasMulheres() {
            List<Pessoa> apenasMulheres = List.of(
                    new Pessoa("Maria",   Pessoa.Genero.FEMININO),
                    new Pessoa("Ana",     Pessoa.Genero.FEMININO),
                    new Pessoa("Jessica", Pessoa.Genero.FEMININO)
            );

            Assertions.assertTrue(
                    analisador.temApenasMulheres(apenasMulheres),
                    "A lista deveria conter apenas mulheres."
            );
        }

        @Test
        @DisplayName("retorna false quando a lista contém homens e mulheres")
        public void deveRetornarFalseQuandoListaContemHomensEMulheres() {
            List<Pessoa> grupoMisto = List.of(
                    new Pessoa("Maria", Pessoa.Genero.FEMININO),
                    new Pessoa("João",  Pessoa.Genero.MASCULINO),
                    new Pessoa("Ana",   Pessoa.Genero.FEMININO)
            );

            Assertions.assertFalse(
                    analisador.temApenasMulheres(grupoMisto),
                    "A lista não deveria passar, pois contém homens."
            );
        }

        @Test
        @DisplayName("retorna false para lista nula")
        public void deveRetornarFalseParaListaNula() {
            Assertions.assertFalse(analisador.temApenasMulheres(null));
        }

        @Test
        @DisplayName("retorna false para lista vazia")
        public void deveRetornarFalseParaListaVazia() {
            Assertions.assertFalse(analisador.temApenasMulheres(List.of()));
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // BUSCAR
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("buscarPorNome")
    class BuscarPorNomeTest {

        @Test
        @DisplayName("encontra pessoa existente pelo nome")
        public void deveEncontrarPessoaExistente() {
            Optional<Pessoa> resultado = analisador.buscarPorNome("Maria");

            Assertions.assertTrue(resultado.isPresent(), "Deveria encontrar 'Maria'.");
            Assertions.assertEquals("Maria", resultado.get().getNome());
            Assertions.assertEquals(Pessoa.Genero.FEMININO, resultado.get().getGenero());
        }

        @Test
        @DisplayName("busca é case-insensitive")
        public void deveBuscarSemDistincaoDeMaiusculas() {
            Optional<Pessoa> resultado = analisador.buscarPorNome("JOÃO");

            Assertions.assertTrue(resultado.isPresent(), "Deveria encontrar 'João' independente de caixa.");
        }

        @Test
        @DisplayName("retorna Optional vazio para nome inexistente")
        public void deveRetornarVazioParaNomeInexistente() {
            Optional<Pessoa> resultado = analisador.buscarPorNome("Carlos");

            Assertions.assertTrue(resultado.isEmpty(), "Não deveria encontrar 'Carlos'.");
        }

        @Test
        @DisplayName("lança exceção para nome nulo")
        public void deveLancarExcecaoParaNomeNulo() {
            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> analisador.buscarPorNome(null)
            );
        }
    }

    @Nested
    @DisplayName("buscarPorGenero")
    class BuscarPorGeneroTest {

        @Test
        @DisplayName("retorna apenas pessoas do gênero feminino")
        public void deveRetornarApenasMultheres() {
            List<Pessoa> mulheres = analisador.buscarPorGenero(Pessoa.Genero.FEMININO);

            Assertions.assertEquals(3, mulheres.size());
            Assertions.assertTrue(mulheres.stream()
                    .allMatch(p -> p.getGenero() == Pessoa.Genero.FEMININO));
        }

        @Test
        @DisplayName("retorna apenas pessoas do gênero masculino")
        public void deveRetornarApenasHomens() {
            List<Pessoa> homens = analisador.buscarPorGenero(Pessoa.Genero.MASCULINO);

            Assertions.assertEquals(1, homens.size());
            Assertions.assertEquals("João", homens.get(0).getNome());
        }

        @Test
        @DisplayName("lança exceção para gênero nulo")
        public void deveLancarExcecaoParaGeneroNulo() {
            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> analisador.buscarPorGenero(null)
            );
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // EXCLUIR
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("excluirPorNome")
    class ExcluirPorNomeTest {

        @Test
        @DisplayName("remove pessoa existente e retorna true")
        public void deveRemoverPessoaExistente() {
            boolean removido = analisador.excluirPorNome("Ana");

            Assertions.assertTrue(removido, "Deveria retornar true ao remover 'Ana'.");
            Assertions.assertTrue(
                    analisador.buscarPorNome("Ana").isEmpty(),
                    "'Ana' não deveria mais existir na lista."
            );
        }

        @Test
        @DisplayName("retorna false para nome inexistente")
        public void deveRetornarFalseParaNomeInexistente() {
            boolean removido = analisador.excluirPorNome("Carlos");

            Assertions.assertFalse(removido, "Deveria retornar false ao tentar remover 'Carlos'.");
            Assertions.assertEquals(4, analisador.listarTodos().size(), "Tamanho da lista não deveria mudar.");
        }

        @Test
        @DisplayName("exclusão é case-insensitive")
        public void deveExcluirSemDistincaoDeMaiusculas() {
            boolean removido = analisador.excluirPorNome("MARIA");

            Assertions.assertTrue(removido);
            Assertions.assertEquals(3, analisador.listarTodos().size());
        }

        @Test
        @DisplayName("lança exceção para nome nulo")
        public void deveLancarExcecaoParaNomeNulo() {
            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> analisador.excluirPorNome(null)
            );
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ATUALIZAR
    // ══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("atualizarGenero")
    class AtualizarGeneroTest {

        @Test
        @DisplayName("atualiza gênero de pessoa existente e retorna true")
        public void deveAtualizarGeneroDeExistente() {
            boolean atualizado = analisador.atualizarGenero("João", Pessoa.Genero.FEMININO);

            Assertions.assertTrue(atualizado, "Deveria retornar true ao atualizar 'João'.");

            Optional<Pessoa> joao = analisador.buscarPorNome("João");
            Assertions.assertTrue(joao.isPresent());
            Assertions.assertEquals(Pessoa.Genero.FEMININO, joao.get().getGenero(),
                    "Gênero de 'João' deveria ter sido atualizado para FEMININO.");
        }

        @Test
        @DisplayName("retorna false para nome inexistente")
        public void deveRetornarFalseParaNomeInexistente() {
            boolean atualizado = analisador.atualizarGenero("Carlos", Pessoa.Genero.MASCULINO);

            Assertions.assertFalse(atualizado, "Deveria retornar false para 'Carlos' inexistente.");
        }

        @Test
        @DisplayName("lança exceção para nome nulo")
        public void deveLancarExcecaoParaNomeNulo() {
            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> analisador.atualizarGenero(null, Pessoa.Genero.MASCULINO)
            );
        }

        @Test
        @DisplayName("lança exceção para gênero nulo")
        public void deveLancarExcecaoParaGeneroNulo() {
            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> analisador.atualizarGenero("Maria", null)
            );
        }

        @Test
        @DisplayName("lista permanece com mesmo tamanho após atualização")
        public void listaNaoDeveAlterarTamanhoAposAtualizacao() {
            analisador.atualizarGenero("Maria", Pessoa.Genero.MASCULINO);

            Assertions.assertEquals(4, analisador.listarTodos().size(),
                    "Tamanho da lista não deve mudar após atualização.");
        }
    }
}
