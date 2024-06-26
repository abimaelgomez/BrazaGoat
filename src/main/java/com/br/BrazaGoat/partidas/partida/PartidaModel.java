package com.br.BrazaGoat.partidas.partida;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.partidas.assistencia.AssistenciaModel;
import com.br.BrazaGoat.partidas.gol.GolModel;
import com.br.BrazaGoat.partidas.substituicao.SubstituicaoModel;
import com.br.BrazaGoat.sorteio.entities.SorteioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
@Entity
@Table(name = "partidas")
public class PartidaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPartida;

    private int numeroDaPartida;

    @Enumerated(EnumType.STRING)
    private StatusPartida statusPartida;

    private int placarEquipeA = 0;
    private int placarEquipeB = 0;

    // Relacionamento OneToMany com GolModel
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GolModel> gols;

    // Relacionamento OneToMany com AssistenciaModel
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssistenciaModel> assistencias;

    // Relacionamento OneToMany com SubstituicaoModel
    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubstituicaoModel> substituicoes;

    // Relacionamento ManyToOne com SorteioModel
    @ManyToOne
    @JoinColumn(name = "sorteio_id")
    private SorteioModel sorteio;

    // Relacionamento ManyToMany com JogadorModel
    @ManyToMany
    @JoinTable(
            name = "partida_jogadores_equipe_a",
            joinColumns = @JoinColumn(name = "partida_id"),
            inverseJoinColumns = @JoinColumn(name = "jogador_id")
    )
    private List<JogadorModel> equipeA;

    @ManyToMany
    @JoinTable(
            name = "partida_jogadores_equipe_b",
            joinColumns = @JoinColumn(name = "partida_id"),
            inverseJoinColumns = @JoinColumn(name = "jogador_id")
    )
    private List<JogadorModel> equipeB;

    @ManyToMany
    @JoinTable(
            name = "partida_jogadores_reservas",
            joinColumns = @JoinColumn(name = "partida_id"),
            inverseJoinColumns = @JoinColumn(name = "jogador_id")
    )
    private List<JogadorModel> reservas;

    private boolean partidaIniciada;
    private boolean partidaFinalizada;

    private Duration tempoMaxDuracao = Duration.ofMinutes(2);
    private Duration tempoMaxDuracaoAcrescismo = Duration.ofSeconds(30);
    private Duration tempoDePartida = Duration.ZERO;
    private Duration tempoDeAcrescimo = Duration.ZERO;

    private LocalDate dataInicio = LocalDate.now();
    private LocalDate dataFinal = LocalDate.now();
    private LocalTime horaDoInicio = LocalTime.now();
    private LocalTime horaDoFinal = LocalTime.now();

    // Método para associar jogadores do sorteio à partida
    public void associarJogadoresDoSorteio(SorteioModel sorteio) {
        this.equipeA = new ArrayList<>(sorteio.getEquipeA());
        this.equipeB = new ArrayList<>(sorteio.getEquipeB());
        this.reservas = new ArrayList<>(sorteio.getJogadoresReserva());
    }

    // Métodos para alterar o status da partida
    public void gerada() {
        this.statusPartida = StatusPartida.PARTIDA_GERADA;
    }

    public void aguardarInicio() {
        this.statusPartida = StatusPartida.AGUARDANDO_INICIO;
    }

    public void EmAndamento() {
        this.statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;
    }

    public void acrescimentoEmAndamento() {
        this.statusPartida = StatusPartida.ACRESCIMO_EM_ANDAMENTO;
    }

    public void Pausada() {
        this.statusPartida = StatusPartida.PAUSADA;
    }

    public void Cancelada() {
        this.statusPartida = StatusPartida.CANCELADA;
    }

    public void aguardarFinalizacao() {
        this.statusPartida = StatusPartida.AGUARDANDO_FINALIZAR;
    }

    public void Finalizada() {
        this.statusPartida = StatusPartida.FINALIZADA;
    }
}
