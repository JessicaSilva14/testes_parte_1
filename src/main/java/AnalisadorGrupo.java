import java.util.List;

public class AnalisadorGrupo {

    public boolean temApenasMulheres(List<Pessoa> pessoas) {
        if (pessoas == null || pessoas.isEmpty()) {
            return false; // Ou lance uma exceção, dependendo da sua regra de negócio
        }
        
        // O allMatch verifica se TODOS os itens da lista atendem ao predicado
        return pessoas.stream()
                .allMatch(pessoa -> pessoa.getGenero() == Pessoa.Genero.FEMININO);
    }
}
