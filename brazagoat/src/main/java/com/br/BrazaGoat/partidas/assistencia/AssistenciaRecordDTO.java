package com.br.BrazaGoat.partidas.assistencia;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssistenciaRecordDTO(
        @NotNull(message = "ID do jogador assistente é obrigatório") UUID idJogador,
        @NotNull(message = "ID da partida é obrigatório") Long idPartida
) {}
