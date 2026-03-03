package com.br.BrazaGoat.resultado.controller;

import com.br.BrazaGoat.resultado.entities.ResultadoModel;
import com.br.BrazaGoat.resultado.service.ResultadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resultado")
@RequiredArgsConstructor
public class ResultadoController {

    private final ResultadoService resultadoService;

    @PostMapping("/gerar")
    public ResponseEntity<ResultadoModel> gerarResultado() {
        ResultadoModel resultado = resultadoService.gerarResultado();
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @GetMapping
    public ResponseEntity<List<ResultadoModel>> listarResultados() {
        return ResponseEntity.ok(resultadoService.listarResultados());
    }

    @GetMapping("/partida/{idPartida}")
    public ResponseEntity<ResultadoModel> buscarPorPartida(@PathVariable Long idPartida) {
        return ResponseEntity.ok(resultadoService.buscarResultadoPorPartida(idPartida));
    }
}
