package com.br.BrazaGoat.model.entities;

import com.br.BrazaGoat.model.enums.StatusJogadorPartida;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "jogadores")
@Entity
public class JogadorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idJogador;

    @NotBlank(message = "O nome do Jogador é obrigatório!")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    private String nome;

    @NotBlank(message = "Digite o Sobrenome do jogador, obrigatório!")
    private String sobrenome;

    @NotBlank(message = "Se for jogador de linha digite linha, se for goleiro digite goleiro!")
    private String posicao;

    @NotNull(message = "Digite a idade do jogador, obrigatório!")
    private int idade;

    @NotNull(message = "Digite o número da camisa, obrigatório!")
    private int numeroCamisa;

    private boolean status = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_do_jogador_partida", length = 50)
    private StatusJogadorPartida statusJogadorPartida;

    private long pontuacaoTotal;
    private long pontuacaoMedia;
    private int golsMarcados;
    private int assistencias;
    private int partidasDisputadas;

    @Column(name = "minutos_jogados", nullable = false, columnDefinition = "int default 0")
    private int minutosJogados;

    // Métodos para alterar Status do jogador
    public void escalarJogador() {
        this.statusJogadorPartida = StatusJogadorPartida.ESCALADO;
    }

    public void reservarJogador() {
        this.statusJogadorPartida = StatusJogadorPartida.RESERVA;
    }

    public void jogarPartida() {
        this.statusJogadorPartida = StatusJogadorPartida.JOGANDO_PARTIDA;
    }

    public void substituirJogador() {
        this.statusJogadorPartida = StatusJogadorPartida.SUBSTITUIDO;
    }

    public void jogadorLesionado() {
        this.statusJogadorPartida = StatusJogadorPartida.LESIONADO;
    }

    public void jogadorDescansando() {
        this.statusJogadorPartida = StatusJogadorPartida.FORA_DA_PARTIDA;
    }

    public String getInformacaoJogador() {
        return this.numeroCamisa + " - " + this.nome + " " + this.sobrenome + " (" + this.posicao + ")";
    }

    public void setId(long l) {
    }
}
