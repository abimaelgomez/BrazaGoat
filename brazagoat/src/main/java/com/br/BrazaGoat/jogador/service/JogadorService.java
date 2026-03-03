package com.br.BrazaGoat.jogador.service;

import com.br.BrazaGoat.jogador.dtos.JogadorRecordDto;
import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JogadorService {

    private final JogadorRepository jogadorRepository;

    public JogadorModel cadastrarJogador(JogadorRecordDto jogadorRecordDto) {
        var jogadorModel = new JogadorModel();
        BeanUtils.copyProperties(jogadorRecordDto, jogadorModel);
        jogadorModel.setStatus(true);
        log.info("Cadastrando jogador: {} {}", jogadorRecordDto.nome(), jogadorRecordDto.sobrenome());
        return jogadorRepository.save(jogadorModel);
    }

    public List<JogadorModel> buscarTodosJogadores() {
        return jogadorRepository.findAll();
    }

    public Optional<JogadorModel> buscarJogador(UUID idJogador) {
        return jogadorRepository.findById(idJogador);
    }

    public Optional<JogadorModel> atualizarJogador(UUID idJogador, JogadorRecordDto jogadorRecordDto) {
        Optional<JogadorModel> jogadorSelecionado = jogadorRepository.findById(idJogador);
        if (jogadorSelecionado.isPresent()) {
            var jogadorModel = jogadorSelecionado.get();
            BeanUtils.copyProperties(jogadorRecordDto, jogadorModel);
            jogadorModel.setStatus(true);
            log.info("Atualizando jogador: {}", idJogador);
            return Optional.of(jogadorRepository.save(jogadorModel));
        }
        log.warn("Jogador não encontrado para atualização: {}", idJogador);
        return Optional.empty();
    }

    public boolean deletarJogador(UUID idJogador) {
        Optional<JogadorModel> jogadorSelecionado = jogadorRepository.findById(idJogador);
        if (jogadorSelecionado.isPresent()) {
            jogadorRepository.delete(jogadorSelecionado.get());
            log.info("Jogador deletado: {}", idJogador);
            return true;
        }
        log.warn("Tentativa de deletar jogador inexistente: {}", idJogador);
        return false;
    }
}
