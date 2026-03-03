package com.br.BrazaGoat.sorteio.controller;

import com.br.BrazaGoat.sorteio.service.SorteioEquipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sorteio_de_equipes")
@RequiredArgsConstructor
@Tag(name = "Sorteio", description = "Sorteio aleatório e balanceado de equipes")
public class SorteioEquipeController {

    private final SorteioEquipeService sorteioEquipeService;

    @PostMapping("/sortear")
    @Operation(summary = "Sortear equipes", description = "Sorteia aleatoriamente jogadores ativos em duas equipes equilibradas com goleiros")
    @ApiResponse(responseCode = "201", description = "Equipes sorteadas com sucesso")
    public ResponseEntity<String> sortearEquipes() {
        sorteioEquipeService.sortearEquipes();
        return ResponseEntity.status(HttpStatus.CREATED).body("Equipes sorteadas com sucesso!");
    }
}
