package com.br.BrazaGoat.partidas.gol;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.partidas.assistencia.AssistenciaModel;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "gols")
public class GolModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogador")
    private JogadorModel jogador;

    @ManyToOne(optional = true) // Associação opcional com AssistenciaModel
    @JoinColumn(name = "assistencia")
    private AssistenciaModel assistencia;

    @ManyToOne
    @JoinColumn(name = "partida")
    private PartidaModel partida;
}
