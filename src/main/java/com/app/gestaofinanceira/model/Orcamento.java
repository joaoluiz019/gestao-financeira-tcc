package com.app.gestaofinanceira.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orcamento", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "mes"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Orcamento {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String mes;

    @Column(nullable = false)
    private float saldo;

    @Column(nullable = false)
    private float receitas;

    @Column(nullable = false)
    private float despesas;

    @Column(nullable = false)
    private float metaGastos;

    @Column(nullable = false)
    private float qtdAtingida;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @CreatedDate
    private LocalDateTime dataCriacao;

    public void setMes(String mes) {
        this.mes = mes.toUpperCase();
    }
}
