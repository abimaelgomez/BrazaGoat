package com.br.BrazaGoat.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String mensagem,
        int status,
        LocalDateTime timestamp
) {
    public ErrorResponse(String mensagem, int status) {
        this(mensagem, status, LocalDateTime.now());
    }
}
