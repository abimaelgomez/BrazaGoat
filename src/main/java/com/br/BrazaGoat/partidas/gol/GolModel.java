package com.br.BrazaGoat.partidas.gol;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.partidas.assistencia.AssistenciaModel;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

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
    @JoinColumn(name = "jogador_id")
    private JogadorModel jogador;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assistencia_id")
    private AssistenciaModel assistencia;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private PartidaModel partida;

    @Enumerated(EnumType.STRING)
    private StatusGol statusGol;
    @Enumerated(EnumType.STRING)
    private TipoDeGol tipoDeGol;

    private int numeroDoGol;
    private LocalTime horaDoGol;
    private LocalDate dataDoGol;
    private int pontoPorGol;
    private int pontoPorGolContra;

    // tipos de gols marcados
    public void golNormalMarcado() {
        this.tipoDeGol = TipoDeGol.NORMAL;
    }
    public void goloDeCabecaMarcado() {
        this.tipoDeGol = TipoDeGol.CABECA;
    }
    public void golContraMarcado() {
        this.tipoDeGol = TipoDeGol.CONTRA;
    }
    public void golDePenaltiMarcado() {
        this.tipoDeGol = TipoDeGol.PENALTI;
    }
    public void golDeTiroLivreMarcado(){
        this.tipoDeGol = TipoDeGol.TIRO_LIVRE;
    }
    public void golDeGoleiro(){
        this.tipoDeGol = TipoDeGol.TIRO_LIVRE;
    }

    //Status de Gols Marcados
    public void validarGol(){
        this.statusGol = StatusGol.VALIDADO;
    }
    public void invalidarGol(){
        this.statusGol = StatusGol.INVALIDADO;
    }
    public void revisarGol(){
        this.statusGol = StatusGol.EM_REVISAO;
    }
    public void anularGol(){
        this.statusGol = StatusGol.ANULADO;
    }


}
