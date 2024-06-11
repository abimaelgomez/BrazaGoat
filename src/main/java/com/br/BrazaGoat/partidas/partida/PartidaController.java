package com.br.BrazaGoat.partidas.partida;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partida")
public class PartidaController {

    @Autowired
    PartidaService partidaService;

    @PostMapping("/gerar")
    public ResponseEntity<String> gerarPartida() {
        PartidaRecordDTO novaPartida = partidaService.gerarPartida();
        if (novaPartida != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Partida gerada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível gerar a partida.");
        }
    }

    @PostMapping("/confirmar-escalacao")
    public void confirmarEscalacao() {
        partidaService.confirmarEscalacao();
    }

}
