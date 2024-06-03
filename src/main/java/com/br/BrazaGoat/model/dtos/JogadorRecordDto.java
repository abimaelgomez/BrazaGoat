package com.br.BrazaGoat.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record JogadorRecordDto(
        @NotBlank String nome,
        @NotBlank String sobrenome,
        @NotBlank String posicao,
        @NotNull int idade,
        @NotNull int numeroCamisa,
        @NotNull boolean status) {
        }
