package com.br.BrazaGoat.partidas.substituicao;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/substituicao")
@RequiredArgsConstructor
public class SubstituicaoController {

    private final SubstituicaoService substituicaoService;

    @PostMapping("/realizar")
    public ResponseEntity<SubstituicaoModel> realizarSubstituicao(
            @RequestBody @Valid SubstituicaoRecordDTO dto) {
        SubstituicaoModel sub = substituicaoService.realizarSubstituicao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sub);
    }

    @GetMapping("/partida/{idPartida}")
    public ResponseEntity<List<SubstituicaoModel>> listarPorPartida(@PathVariable Long idPartida) {
        return ResponseEntity.ok(substituicaoService.listarSubstituicoesDaPartida(idPartida));
    }
}
