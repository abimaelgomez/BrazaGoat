package com.br.BrazaGoat.partidas.gol;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.partidas.assistencia.AssistenciaModel;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record GolRecordDTO(
        Long id,
        JogadorModel jogador,
        AssistenciaModel assistencia,
        PartidaModel partida,
        int numeroDoGol,
        LocalTime horaDoGol,
        LocalDate dataDoGol,
        int pontoPorGol,
        int pontoPorGolContra
) {
}
