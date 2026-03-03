package com.br.BrazaGoat.resultado.entities;

import com.br.BrazaGoat.partidas.partida.PartidaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "resultados")
public class ResultadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "partida_id", nullable = false)
    private PartidaModel partida;

    private int placarEquipeA;
    private int placarEquipeB;

    private String vencedor; // "EQUIPE_A", "EQUIPE_B" ou "EMPATE"

    private LocalDateTime dataResultado;
}
