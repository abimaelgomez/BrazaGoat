package com.br.BrazaGoat.partidas.substituicao;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SubstituicaoRecordDTO(
        @NotNull(message = "ID do jogador que saiu é obrigatório") UUID idJogadorSaiu,
        @NotNull(message = "ID do jogador que entrou é obrigatório") UUID idJogadorEntrou
) {}
