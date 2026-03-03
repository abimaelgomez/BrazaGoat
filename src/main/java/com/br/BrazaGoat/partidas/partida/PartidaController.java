package com.br.BrazaGoat.partidas.partida;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partida")
@RequiredArgsConstructor
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping("/gerar")
    public ResponseEntity<Object> gerarPartida() {
        PartidaRecordDTO novaPartida = partidaService.gerarPartida();
        if (novaPartida != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPartida);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Não foi possível gerar a partida.");
    }

    @PostMapping("/ok")
    public ResponseEntity<String> confirmarEscalacao() {
        partidaService.confirmarEscalacao();
        return ResponseEntity.ok("Escalação confirmada! Aguardando início da partida.");
    }

    @PostMapping("/iniciar")
    public ResponseEntity<String> iniciarPartida() {
        partidaService.iniciarPartida();
        return ResponseEntity.ok("Partida iniciada com sucesso!");
    }

    @PostMapping("/finalizar")
    public ResponseEntity<String> finalizarPartida() {
        partidaService.finalizarPartida();
        return ResponseEntity.ok("Partida finalizada com sucesso!");
    }
}
