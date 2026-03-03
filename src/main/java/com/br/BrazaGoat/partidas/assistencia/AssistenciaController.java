package com.br.BrazaGoat.partidas.assistencia;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assistencia")
@RequiredArgsConstructor
public class AssistenciaController {

    private final AssistenciaService assistenciaService;

    @PostMapping("/registrar")
    public ResponseEntity<AssistenciaModel> registrarAssistencia(
            @RequestBody @Valid AssistenciaRecordDTO dto) {
        AssistenciaModel assistencia = assistenciaService.registrarAssistencia(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assistencia);
    }

    @GetMapping("/partida/{idPartida}")
    public ResponseEntity<List<AssistenciaModel>> listarPorPartida(@PathVariable Long idPartida) {
        return ResponseEntity.ok(assistenciaService.listarAssistenciasDaPartida(idPartida));
    }
}
