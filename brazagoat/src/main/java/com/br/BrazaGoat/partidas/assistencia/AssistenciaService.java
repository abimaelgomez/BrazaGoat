package com.br.BrazaGoat.partidas.assistencia;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import com.br.BrazaGoat.partidas.partida.PartidaRepository;
import com.br.BrazaGoat.partidas.partida.StatusPartida;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssistenciaService {

    private final AssistenciaRepository assistenciaRepository;
    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;

    @Transactional
    public AssistenciaModel registrarAssistencia(AssistenciaRecordDTO dto) {
        PartidaModel partida = partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(
                StatusPartida.PARTIDA_EM_ANDAMENTO);

        if (partida == null) {
            throw new RuntimeException("Nenhuma partida em andamento encontrada.");
        }

        JogadorModel jogador = jogadorRepository.findById(dto.idJogador())
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado: " + dto.idJogador()));

        boolean jogadorNaPartida = partida.getEquipeA().contains(jogador)
                || partida.getEquipeB().contains(jogador);

        if (!jogadorNaPartida) {
            throw new RuntimeException("Jogador " + jogador.getNome() + " não está em campo nesta partida.");
        }

        AssistenciaModel assistencia = AssistenciaModel.builder()
                .jogador(jogador)
                .partida(partida)
                .build();

        assistenciaRepository.save(assistencia);

        // Atualiza estatística do jogador
        jogador.setAssistencias(jogador.getAssistencias() + 1);
        jogadorRepository.save(jogador);

        log.info("Assistência registrada para {} | Partida #{}", jogador.getNome(), partida.getIdPartida());

        return assistencia;
    }

    public List<AssistenciaModel> listarAssistenciasDaPartida(Long idPartida) {
        return assistenciaRepository.findAll().stream()
                .filter(a -> a.getPartida().getIdPartida().equals(idPartida))
                .toList();
    }
}
