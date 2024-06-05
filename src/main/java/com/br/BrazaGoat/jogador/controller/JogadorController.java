package com.br.BrazaGoat.jogador.controller;

import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import com.br.BrazaGoat.jogador.dtos.JogadorRecordDto;
import com.br.BrazaGoat.jogador.entities.JogadorModel;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value ="/jogador")
public class JogadorController {

    @Autowired
    JogadorRepository jogadorRepository;

    /*Adicionando jogador*/
    @PostMapping("/cadastro")
        public ResponseEntity cadastrarJogador(@RequestBody @Valid JogadorRecordDto jogadorRecordDto){
        // Criando uma nova instância da classe PlayerModel
        var jogadorModel = new JogadorModel();
        // Copiando propriedades do playerRecordDto para o PlayerModel
        BeanUtils.copyProperties(jogadorRecordDto, jogadorModel);
        // Definindo o status como ativo
        jogadorModel.setStatus(true);
        // Salvando o jogador no banco de dados
        JogadorModel cadastrarJogador = jogadorRepository.save(jogadorModel);
        // Retornando a resposta com o jogador criado
        return ResponseEntity.status(HttpStatus.CREATED).body(jogadorModel);
    }

    /*Busca Jogador*/
    @GetMapping("/buscar")
    public ResponseEntity <List<JogadorModel>> buscarTodosJogadores(){
        List<JogadorModel> listaDeJogadores = jogadorRepository.findAll();
        return ResponseEntity.status(HttpStatus.FOUND).body(jogadorRepository.findAll());
    }

    //Metodo - buscar 1 jogador especifico
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarJogador(@PathVariable(value = "id") UUID id) {
        Optional<JogadorModel> jogadorSelecionado = jogadorRepository.findById(id);
        if (jogadorSelecionado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador Não Encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(jogadorSelecionado.get());
    }

    /*Atualizando Jogador*/
    @PutMapping("/atualizar/{id}")
    public ResponseEntity <Object> atualizarJogador(
            @PathVariable(value = "id") UUID id,
            @RequestBody @Valid JogadorRecordDto jogadorRecordDto) {
        Optional<JogadorModel> jogadorSelecionado = jogadorRepository.findById(id);
        if (jogadorSelecionado.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador NÂO foi encontrado :( ");
        }
        var jogadorModel = jogadorSelecionado.get();
        BeanUtils.copyProperties(jogadorRecordDto, jogadorModel);
        jogadorModel.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(jogadorRepository.save(jogadorModel));
    }

    //Deletando jogador
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Object> deletarJogador(@PathVariable(value = "id") UUID id) {
        Optional<JogadorModel> jogadorSelecionado = jogadorRepository.findById(id);
        if (jogadorSelecionado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Jogador Não encontrado :( ");
        }
        jogadorRepository.delete(jogadorSelecionado.get());
        return ResponseEntity.status(HttpStatus.OK).body("Jogador deletado com sucesso.");
    }

}
