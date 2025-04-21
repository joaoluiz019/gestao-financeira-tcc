package com.app.gestaofinanceira.model;

import com.app.gestaofinanceira.emum.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private Float saldo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status= Status.ATIVO;
}
