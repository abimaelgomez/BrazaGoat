package com.br.BrazaGoat.sorteio.entities;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "sorteios")
@Entity
public class SorteioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idSorteio;

    @NotNull(message = "A data do sorteio é obrigatória!")
    private LocalDateTime dataSorteio;

    @ManyToMany
    @JoinTable(
            name = "sorteio_jogador",
            joinColumns = @JoinColumn(name = "sorteio_id"),
            inverseJoinColumns = @JoinColumn(name = "jogador_id")
    )
    private List<JogadorModel> jogadoresSorteados;

    @ManyToMany
    @JoinTable(
            name = "sorteio_equipe_a",
            joinColumns = @JoinColumn(name = "sorteio_id"),
            inverseJoinColumns = @JoinColumn(name = "jogador_id")
    )
    private List<JogadorModel> equipeA;

    @ManyToMany
    @JoinTable(
            name = "sorteio_equipe_b",
            joinColumns = @JoinColumn(name = "sorteio_id"),
            inverseJoinColumns = @JoinColumn(name = "jogador_id")
    )
    private List<JogadorModel> equipeB;

    @ManyToMany
    @JoinTable(
            name = "sorteio_reserva",
            joinColumns = @JoinColumn(name = "sorteio_id"),
            inverseJoinColumns = @JoinColumn(name = "jogador_id")
    )
    private List<JogadorModel> jogadoresReserva;

    public List<JogadorModel> getEquipeA() {
        return equipeA;
    }

    public List<JogadorModel> getEquipeB() {
        return equipeB;
    }

    public List<JogadorModel> getJogadoresReserva() {
        return jogadoresReserva;
    }
}
