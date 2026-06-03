package br.com.testes.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "relatorio_grupos")
public class RelatorioGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private int totalPessoas;

    @Column(nullable = false)
    private int totalMasculino;

    @Column(nullable = false)
    private int totalFeminino;

    @Column(nullable = false)
    private boolean apenasMultheres;

    public RelatorioGrupo() {}

    public RelatorioGrupo(LocalDate data, int totalPessoas,
                          int totalMasculino, int totalFeminino,
                          boolean apenasMultheres) {
        this.data            = data;
        this.totalPessoas    = totalPessoas;
        this.totalMasculino  = totalMasculino;
        this.totalFeminino   = totalFeminino;
        this.apenasMultheres = apenasMultheres;
    }

    public Long getId()              { return id; }
    public LocalDate getData()       { return data; }
    public int getTotalPessoas()     { return totalPessoas; }
    public int getTotalMasculino()   { return totalMasculino; }
    public int getTotalFeminino()    { return totalFeminino; }
    public boolean isApenasMultheres() { return apenasMultheres; }

    public void setId(Long id)                   { this.id = id; }
    public void setData(LocalDate data)          { this.data = data; }
    public void setTotalPessoas(int v)           { this.totalPessoas = v; }
    public void setTotalMasculino(int v)         { this.totalMasculino = v; }
    public void setTotalFeminino(int v)          { this.totalFeminino = v; }
    public void setApenasMultheres(boolean v)    { this.apenasMultheres = v; }
}
