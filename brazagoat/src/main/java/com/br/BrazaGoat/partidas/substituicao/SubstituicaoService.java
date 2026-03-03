package com.br.BrazaGoat.partidas.substituicao;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.enums.StatusJogadorPartida;
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
public class SubstituicaoService {

    private final SubstituicaoRepository substituicaoRepository;
    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;

    @Transactional
    public SubstituicaoModel realizarSubstituicao(SubstituicaoRecordDTO dto) {
        // Busca a partida em andamento
        PartidaModel partida = partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(
                StatusPartida.PARTIDA_EM_ANDAMENTO);

        if (partida == null) {
            throw new RuntimeException("Nenhuma partida em andamento encontrada.");
        }

        JogadorModel jogadorSaiu = jogadorRepository.findById(dto.idJogadorSaiu())
                .orElseThrow(() -> new RuntimeException("Jogador que saiu não encontrado: " + dto.idJogadorSaiu()));

        JogadorModel jogadorEntrou = jogadorRepository.findById(dto.idJogadorEntrou())
                .orElseThrow(() -> new RuntimeException("Jogador que entrou não encontrado: " + dto.idJogadorEntrou()));

        // Valida que o jogador que saiu está em campo
        boolean saiuEstaEmCampo = partida.getEquipeA().contains(jogadorSaiu)
                || partida.getEquipeB().contains(jogadorSaiu);
        if (!saiuEstaEmCampo) {
            throw new RuntimeException("Jogador " + jogadorSaiu.getNome() + " não está em campo nesta partida.");
        }

        // Valida que o jogador que entrou é reserva
        if (!partida.getReservas().contains(jogadorEntrou)) {
            throw new RuntimeException("Jogador " + jogadorEntrou.getNome() + " não está na lista de reservas.");
        }

        // Executa a substituição nas listas da partida
        substituirNaEquipe(partida.getEquipeA(), jogadorSaiu, jogadorEntrou);
        substituirNaEquipe(partida.getEquipeB(), jogadorSaiu, jogadorEntrou);
        partida.getReservas().remove(jogadorEntrou);
        partida.getReservas().add(jogadorSaiu);

        // Atualiza status dos jogadores
        jogadorSaiu.setStatusJogadorPartida(StatusJogadorPartida.SUBSTITUIDO);
        jogadorEntrou.setStatusJogadorPartida(StatusJogadorPartida.JOGANDO_PARTIDA);

        jogadorRepository.save(jogadorSaiu);
        jogadorRepository.save(jogadorEntrou);
        partidaRepository.save(partida);

        // Registra a substituição
        SubstituicaoModel substituicao = SubstituicaoModel.builder()
                .jogadorSaiu(jogadorSaiu)
                .jogadorEntrou(jogadorEntrou)
                .partida(partida)
                .build();

        substituicaoRepository.save(substituicao);

        log.info("Substituição realizada: {} saiu, {} entrou | Partida #{}",
                jogadorSaiu.getNome(), jogadorEntrou.getNome(), partida.getIdPartida());

        return substituicao;
    }

    public List<SubstituicaoModel> listarSubstituicoesDaPartida(Long idPartida) {
        return substituicaoRepository.findByPartidaIdPartida(idPartida);
    }

    private void substituirNaEquipe(List<JogadorModel> equipe, JogadorModel sai, JogadorModel entra) {
        int index = equipe.indexOf(sai);
        if (index >= 0) {
            equipe.set(index, entra);
        }
    }
}
