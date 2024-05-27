package com.br.BrazaGoat.model;

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
@Entity
@Table(name="jogadores")
public class JogadorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idJogador;

    @NotBlank(message = "O nome do Jogador é obrigatório!")
    @Size(min = 3, message = "O nome deve ter no minimo e caracteres")
    private String nome;
    @NotBlank(message = "Digite o Sobrenome do jogador, obrigatório!")
    private String sobrenome;
    @NotBlank(message = "Se for jogador de linha digite linha, se for goleiro digite goleiro!")
    private String posicao;
    @NotNull(message = "Digite a idade do jogador, obrigatório!")
    private int idade;
    @NotNull(message = "Digite o numero da camisa, obrigatório!")
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

    //Metodos para alterar Status do jogador
    public void escalarJogador(){
        this.statusJogadorPartida = StatusJogadorPartida.ESCALADO;
    }

    public void reservarJogador(){
        this.statusJogadorPartida = StatusJogadorPartida.RESERVA;
    }

    public void jogarPartida(){
        this.statusJogadorPartida = StatusJogadorPartida.JOGANDO_PARTIDA;
    }

    public void substituirJogador(){
        this.statusJogadorPartida = StatusJogadorPartida.SUBSTITUIDO;
    }

    public void jogadorLesionado (){
        this.statusJogadorPartida = StatusJogadorPartida.LESIONADO;
    }

    public void jogadorDescansando (){
        this.statusJogadorPartida = StatusJogadorPartida.FORA_DA_PARTIDA;
    }

}
