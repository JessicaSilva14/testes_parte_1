package br.com.testes.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_eventos")
public class LogEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;        // INFO, WARN

    @Column(nullable = false)
    private String mensagem;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public LogEvento() {}

    public LogEvento(String tipo, String mensagem) {
        this.tipo      = tipo;
        this.mensagem  = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId()                 { return id; }
    public String getTipo()             { return tipo; }
    public String getMensagem()         { return mensagem; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setId(Long id)                    { this.id = id; }
    public void setTipo(String tipo)              { this.tipo = tipo; }
    public void setMensagem(String mensagem)      { this.mensagem = mensagem; }
    public void setTimestamp(LocalDateTime ts)    { this.timestamp = ts; }
}
