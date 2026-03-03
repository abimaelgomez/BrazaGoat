package com.br.BrazaGoat.partidas.substituicao;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
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
@Table(name = "substituicoes")
public class SubstituicaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogador_saiu")
    private JogadorModel jogadorSaiu;

    @ManyToOne
    @JoinColumn(name = "jogador_entrou")
    private JogadorModel jogadorEntrou;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private PartidaModel partida;
}
