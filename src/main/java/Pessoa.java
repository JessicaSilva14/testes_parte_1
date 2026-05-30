public class Pessoa {
    private String nome;
    private Genero genero;

    public Pessoa(String nome, Genero genero) {
        this.nome = nome;
        this.genero = genero;
    }

    public String getNome() {
        return nome;
    }

    public Genero getGenero() {
        return genero;
    }

    public enum Genero {
        MASCULINO, FEMININO
    }
}