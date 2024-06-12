package com.br.BrazaGoat.jogador.service;

import com.br.BrazaGoat.jogador.dtos.JogadorRecordDto;
import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JogadorService {

    @Autowired
    JogadorRepository jogadorRepository;

    public JogadorModel cadastrarJogador(JogadorRecordDto jogadorRecordDto) {
        var jogadorModel = new JogadorModel();
        BeanUtils.copyProperties(jogadorRecordDto, jogadorModel);
        jogadorModel.setStatus(true);
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
            return Optional.of(jogadorRepository.save(jogadorModel));
        }
        return Optional.empty();
    }

    public boolean deletarJogador(UUID idJogador) {
        Optional<JogadorModel> jogadorSelecionado = jogadorRepository.findById(idJogador);
        if (jogadorSelecionado.isPresent()) {
            jogadorRepository.delete(jogadorSelecionado.get());
            return true;
        }
        return false;
    }
}
