package com.br.BrazaGoat.jogador.controller;

import com.br.BrazaGoat.jogador.dtos.JogadorRecordDto;
import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.service.JogadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping(value = "/jogador")
public class JogadorController {

    @Autowired
    JogadorService jogadorService;

    @PostMapping("/cadastro")
    public ResponseEntity<JogadorModel> cadastrarJogador(@RequestBody @Valid JogadorRecordDto jogadorRecordDto) {
        JogadorModel jogadorModel = jogadorService.cadastrarJogador(jogadorRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jogadorModel);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<JogadorModel>> buscarTodosJogadores() {
        List<JogadorModel> listaDeJogadores = jogadorService.buscarTodosJogadores();
        return ResponseEntity.status(HttpStatus.OK).body(listaDeJogadores);
    }

    @GetMapping("/{idJogador}")
    public ResponseEntity<Object> buscarJogador(@PathVariable(value = "idJogador") UUID idJogador) {
        Optional<JogadorModel> jogadorSelecionado = jogadorService.buscarJogador(idJogador);
        if (jogadorSelecionado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador Não Encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(jogadorSelecionado.get());
    }

    @PutMapping("/atualizar/{idJogador}")
    public ResponseEntity<Object> atualizarJogador(@PathVariable(value = "idJogador") UUID idJogador,
                                                   @RequestBody @Valid JogadorRecordDto jogadorRecordDto) {
        Optional<JogadorModel> jogadorAtualizado = jogadorService.atualizarJogador(idJogador, jogadorRecordDto);
        if (jogadorAtualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador Não foi encontrado :(");
        }
        return ResponseEntity.status(HttpStatus.OK).body(jogadorAtualizado.get());
    }

    @DeleteMapping("/deletar/{idJogador}")
    public ResponseEntity<Object> deletarJogador(@PathVariable(value = "idJogador") UUID idJogador) {
        boolean isDeleted = jogadorService.deletarJogador(idJogador);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador Não encontrado :(");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Jogador deletado com sucesso.");
    }
}
