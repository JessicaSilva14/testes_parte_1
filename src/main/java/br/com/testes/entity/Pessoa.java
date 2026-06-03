package br.com.testes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pessoas")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    public Pessoa() {}

    public Pessoa(String nome, Genero genero) {
        this.nome   = nome;
        this.genero = genero;
    }

    public Long getId()          { return id; }
    public String getNome()      { return nome; }
    public Genero getGenero()    { return genero; }

    public void setId(Long id)           { this.id = id; }
    public void setNome(String nome)     { this.nome = nome; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public enum Genero {
        MASCULINO, FEMININO
    }
}
