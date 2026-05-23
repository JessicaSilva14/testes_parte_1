import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class AnalisadorGrupoTest {

    private AnalisadorGrupo analisador;

    @BeforeEach
    public void setup() {
        analisador = new AnalisadorGrupo();
    }

    @Test
    public void deveRetornarTrueQuandoListaContemApenasMulheres() {
        // Criando uma lista apenas com mulheres
        List<Pessoa> apenasMulheres = List.of(
                new Pessoa("Maria", Pessoa.Genero.FEMININO),
                new Pessoa("Ana", Pessoa.Genero.FEMININO),
                new Pessoa("Jessica", Pessoa.Genero.FEMININO)
        );

        boolean resultado = analisador.temApenasMulheres(apenasMulheres);

        // O JUnit valida se o resultado é verdadeiro
        Assertions.assertTrue(resultado, "A lista deveria conter apenas mulheres.");
    }

    @Test
    public void deveRetornarFalseQuandoListaContemHomensEMulheres() {
        // Criando uma lista mista
        List<Pessoa> grupoMisto = List.of(
                new Pessoa("Maria", Pessoa.Genero.FEMININO),
                new Pessoa("João", Pessoa.Genero.MASCULINO),
                new Pessoa("Ana", Pessoa.Genero.FEMININO)
        );

        boolean resultado = analisador.temApenasMulheres(grupoMisto);

        // O JUnit valida se o resultado é falso
        Assertions.assertFalse(resultado, "A lista não deveria passar, pois contém homens.");
    }
}
