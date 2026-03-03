package com.br.BrazaGoat.partidas.partida;

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
@RequestMapping("/partida")
@RequiredArgsConstructor
@Tag(name = "Partidas", description = "Ciclo de vida de uma partida: gerar → confirmar → iniciar → finalizar")
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping("/gerar")
    @Operation(summary = "Gerar partida", description = "Gera uma nova partida com base no último sorteio realizado")
    @ApiResponse(responseCode = "201", description = "Partida gerada com sucesso")
    public ResponseEntity<Object> gerarPartida() {
        PartidaRecordDTO novaPartida = partidaService.gerarPartida();
        if (novaPartida != null) return ResponseEntity.status(HttpStatus.CREATED).body(novaPartida);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível gerar a partida.");
    }

    @PostMapping("/ok")
    @Operation(summary = "Confirmar escalação", description = "Confirma a escalação e coloca a partida em aguardando início")
    @ApiResponse(responseCode = "200", description = "Escalação confirmada")
    public ResponseEntity<String> confirmarEscalacao() {
        partidaService.confirmarEscalacao();
        return ResponseEntity.ok("Escalação confirmada! Aguardando início da partida.");
    }

    @PostMapping("/iniciar")
    @Operation(summary = "Iniciar partida", description = "Inicia a partida que está aguardando")
    @ApiResponse(responseCode = "200", description = "Partida iniciada")
    public ResponseEntity<String> iniciarPartida() {
        partidaService.iniciarPartida();
        return ResponseEntity.ok("Partida iniciada com sucesso!");
    }

    @PostMapping("/finalizar")
    @Operation(summary = "Finalizar partida", description = "Encerra a partida em andamento")
    @ApiResponse(responseCode = "200", description = "Partida finalizada")
    public ResponseEntity<String> finalizarPartida() {
        partidaService.finalizarPartida();
        return ResponseEntity.ok("Partida finalizada com sucesso!");
    }
}
