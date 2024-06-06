package com.br.BrazaGoat.partidas.partida;

import com.br.BrazaGoat.partidas.assistencia.AssistenciaRecordDTO;
import com.br.BrazaGoat.partidas.gol.GolRecordDTO;
import com.br.BrazaGoat.partidas.substituicao.SubstituicaoRecordDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PartidaRecordDTO(
        Long id,
        int numeroDaPartida,
        StatusPartida statusPartida,
        int placarEquipeA,
        int placarEquipeB,
        List<GolRecordDTO> gols,
        List<AssistenciaRecordDTO> assistencias,
        List<SubstituicaoRecordDTO> substituicoes,
        boolean partidaIniciada,
        boolean partidaFinalizada,
        Duration tempoMaxDuracao,
        Duration tempoMaxDuracaoAcrescismo,
        Duration tempoDePartida,
        Duration tempoDeAcrescimo,
        LocalDate dataInicio,
        LocalDate dataFinal,
        LocalTime horaDoInicio,
        LocalTime horaDoFinal) {

    public PartidaRecordDTO(
            Long id,
            int numeroDaPartida,
            StatusPartida statusPartida,
            int placarEquipeA,
            int placarEquipeB,
            boolean partidaIniciada,
            boolean partidaFinalizada,
            Duration tempoMaxDuracao,
            Duration tempoMaxDuracaoAcrescismo,
            Duration tempoDePartida,
            Duration tempoDeAcrescimo,
            LocalDate dataInicio,
            LocalDate dataFinal,
            LocalTime horaDoInicio,
            LocalTime horaDoFinal) {
        this(id, numeroDaPartida, statusPartida, placarEquipeA, placarEquipeB,
                List.of(), List.of(), List.of(),
                partidaIniciada, partidaFinalizada,
                tempoMaxDuracao, tempoMaxDuracaoAcrescismo,
                tempoDePartida, tempoDeAcrescimo,
                dataInicio, dataFinal,
                horaDoInicio, horaDoFinal);
    }

}
