package com.br.BrazaGoat.resultado.controller;

import com.br.BrazaGoat.resultado.entities.ResultadoModel;
import com.br.BrazaGoat.resultado.service.ResultadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resultado")
@RequiredArgsConstructor
@Tag(name = "Resultados", description = "Resultados e estatísticas das partidas finalizadas")
public class ResultadoController {

    private final ResultadoService resultadoService;

    @PostMapping("/gerar")
    @Operation(summary = "Gerar resultado", description = "Gera o resultado da última partida finalizada e atualiza estatísticas dos jogadores")
    @ApiResponse(responseCode = "201", description = "Resultado gerado com sucesso")
    public ResponseEntity<ResultadoModel> gerarResultado() {
        return ResponseEntity.status(HttpStatus.CREATED).body(resultadoService.gerarResultado());
    }

    @GetMapping
    @Operation(summary = "Listar resultados", description = "Retorna todos os resultados em ordem cronológica decrescente")
    public ResponseEntity<List<ResultadoModel>> listarResultados() {
        return ResponseEntity.ok(resultadoService.listarResultados());
    }

    @GetMapping("/partida/{idPartida}")
    @Operation(summary = "Resultado por partida")
    @ApiResponse(responseCode = "404", description = "Resultado não encontrado")
    public ResponseEntity<ResultadoModel> buscarPorPartida(@PathVariable Long idPartida) {
        return ResponseEntity.ok(resultadoService.buscarResultadoPorPartida(idPartida));
    }
}
