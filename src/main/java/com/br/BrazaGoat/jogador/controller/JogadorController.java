package com.br.BrazaGoat.jogador.controller;

import com.br.BrazaGoat.jogador.dtos.JogadorRecordDto;
import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.service.JogadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/jogador")
@RequiredArgsConstructor
public class JogadorController {

    private final JogadorService jogadorService;

    @PostMapping("/cadastro")
    public ResponseEntity<JogadorModel> cadastrarJogador(@RequestBody @Valid JogadorRecordDto jogadorRecordDto) {
        JogadorModel jogadorModel = jogadorService.cadastrarJogador(jogadorRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jogadorModel);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<JogadorModel>> buscarTodosJogadores() {
        return ResponseEntity.ok(jogadorService.buscarTodosJogadores());
    }

    @GetMapping("/{idJogador}")
    public ResponseEntity<Object> buscarJogador(@PathVariable UUID idJogador) {
        Optional<JogadorModel> jogadorSelecionado = jogadorService.buscarJogador(idJogador);
        if (jogadorSelecionado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador não encontrado.");
        }
        return ResponseEntity.ok(jogadorSelecionado.get());
    }

    @PutMapping("/atualizar/{idJogador}")
    public ResponseEntity<Object> atualizarJogador(@PathVariable UUID idJogador,
                                                   @RequestBody @Valid JogadorRecordDto jogadorRecordDto) {
        Optional<JogadorModel> jogadorAtualizado = jogadorService.atualizarJogador(idJogador, jogadorRecordDto);
        if (jogadorAtualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador não encontrado.");
        }
        return ResponseEntity.ok(jogadorAtualizado.get());
    }

    @DeleteMapping("/deletar/{idJogador}")
    public ResponseEntity<Object> deletarJogador(@PathVariable UUID idJogador) {
        boolean isDeleted = jogadorService.deletarJogador(idJogador);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador não encontrado.");
        }
        return ResponseEntity.ok("Jogador deletado com sucesso.");
    }
}
