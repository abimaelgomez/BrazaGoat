package com.br.BrazaGoat.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JogadorRecordDto(
        @NotBlank String nome,
        @NotBlank String sobrenome,
        @NotBlank String posicao,
        @NotNull int idade,
        @NotNull int numeroCamisa,
        @NotNull boolean status) {
        }
