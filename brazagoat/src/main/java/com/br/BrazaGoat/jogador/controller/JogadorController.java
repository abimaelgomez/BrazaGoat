package com.br.BrazaGoat.jogador.controller;

import com.br.BrazaGoat.jogador.dtos.JogadorRecordDto;
import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.service.JogadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Jogadores", description = "Gerenciamento de jogadores cadastrados no sistema")
public class JogadorController {

    private final JogadorService jogadorService;

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar jogador", description = "Cadastra um novo jogador no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Jogador cadastrado com sucesso"),
        @ApiResponse(responseCode = "422", description = "Dados inválidos")
    })
    public ResponseEntity<JogadorModel> cadastrarJogador(@RequestBody @Valid JogadorRecordDto jogadorRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jogadorService.cadastrarJogador(jogadorRecordDto));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Listar jogadores", description = "Retorna todos os jogadores cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<JogadorModel>> buscarTodosJogadores() {
        return ResponseEntity.ok(jogadorService.buscarTodosJogadores());
    }

    @GetMapping("/{idJogador}")
    @Operation(summary = "Buscar jogador por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jogador encontrado"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public ResponseEntity<Object> buscarJogador(@PathVariable UUID idJogador) {
        Optional<JogadorModel> jogador = jogadorService.buscarJogador(idJogador);
        if (jogador.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador não encontrado.");
        return ResponseEntity.ok(jogador.get());
    }

    @PutMapping("/atualizar/{idJogador}")
    @Operation(summary = "Atualizar jogador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jogador atualizado"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public ResponseEntity<Object> atualizarJogador(@PathVariable UUID idJogador,
                                                   @RequestBody @Valid JogadorRecordDto dto) {
        Optional<JogadorModel> atualizado = jogadorService.atualizarJogador(idJogador, dto);
        if (atualizado.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador não encontrado.");
        return ResponseEntity.ok(atualizado.get());
    }

    @DeleteMapping("/deletar/{idJogador}")
    @Operation(summary = "Deletar jogador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Jogador deletado"),
        @ApiResponse(responseCode = "404", description = "Jogador não encontrado")
    })
    public ResponseEntity<Object> deletarJogador(@PathVariable UUID idJogador) {
        if (!jogadorService.deletarJogador(idJogador))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador não encontrado.");
        return ResponseEntity.ok("Jogador deletado com sucesso.");
    }
}
