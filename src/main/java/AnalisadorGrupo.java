import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnalisadorGrupo {

    private List<Pessoa> pessoas = new ArrayList<>();

    // ─── Método original ────────────────────────────────────────────────────────

    public boolean temApenasMulheres(List<Pessoa> lista) {
        if (lista == null || lista.isEmpty()) {
            return false;
        }
        return lista.stream()
                .allMatch(pessoa -> pessoa.getGenero() == Pessoa.Genero.FEMININO);
    }

    // ─── Gerenciamento interno da lista ─────────────────────────────────────────

    public void adicionar(Pessoa pessoa) {
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa não pode ser nula.");
        }
        pessoas.add(pessoa);
    }

    public List<Pessoa> listarTodos() {
        return new ArrayList<>(pessoas);
    }

    // ─── BUSCAR ─────────────────────────────────────────────────────────────────

    /**
     * Busca uma pessoa pelo nome (case-insensitive).
     *
     * @param nome nome a ser buscado
     * @return Optional contendo a pessoa encontrada, ou vazio se não existir
     */
    public Optional<Pessoa> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        return pessoas.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    /**
     * Busca todas as pessoas de um determinado gênero.
     *
     * @param genero gênero a ser filtrado
     * @return lista de pessoas do gênero informado
     */
    public List<Pessoa> buscarPorGenero(Pessoa.Genero genero) {
        if (genero == null) {
            throw new IllegalArgumentException("Gênero não pode ser nulo.");
        }
        return pessoas.stream()
                .filter(p -> p.getGenero() == genero)
                .toList();
    }

    // ─── EXCLUIR ────────────────────────────────────────────────────────────────

    /**
     * Remove a primeira pessoa cujo nome seja igual ao informado (case-insensitive).
     *
     * @param nome nome da pessoa a ser removida
     * @return true se a remoção foi realizada, false se o nome não foi encontrado
     */
    public boolean excluirPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        return pessoas.removeIf(p -> p.getNome().equalsIgnoreCase(nome));
    }

    // ─── ATUALIZAR ──────────────────────────────────────────────────────────────

    /**
     * Atualiza o gênero da pessoa com o nome informado.
     *
     * @param nome     nome da pessoa a ser atualizada
     * @param novoGenero novo gênero
     * @return true se a atualização foi realizada, false se o nome não foi encontrado
     */
    public boolean atualizarGenero(String nome, Pessoa.Genero novoGenero) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        if (novoGenero == null) {
            throw new IllegalArgumentException("Gênero não pode ser nulo.");
        }

        Optional<Pessoa> encontrada = buscarPorNome(nome);
        if (encontrada.isEmpty()) {
            return false;
        }

        // Substitui o objeto na lista pelo novo (Pessoa é imutável por design)
        int indice = pessoas.indexOf(encontrada.get());
        pessoas.set(indice, new Pessoa(encontrada.get().getNome(), novoGenero));
        return true;
    }
}
