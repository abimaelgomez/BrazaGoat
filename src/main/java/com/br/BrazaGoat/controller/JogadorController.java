package com.br.BrazaGoat.controller;

import com.br.BrazaGoat.dtos.JogadorRecordDto;
import com.br.BrazaGoat.model.JogadorModel;
import com.br.BrazaGoat.repositories.JogadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
