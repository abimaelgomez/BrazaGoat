package com.br.BrazaGoat.resultado.service;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import com.br.BrazaGoat.partidas.partida.PartidaRepository;
import com.br.BrazaGoat.partidas.partida.StatusPartida;
import com.br.BrazaGoat.resultado.entities.ResultadoModel;
import com.br.BrazaGoat.resultado.repositories.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;
    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;

    @Transactional
    public ResultadoModel gerarResultado() {
        PartidaModel partida = partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(
                StatusPartida.FINALIZADA);

        if (partida == null) {
            throw new RuntimeException("Nenhuma partida finalizada encontrada para gerar resultado.");
        }

        // Verifica se resultado já foi gerado
        if (resultadoRepository.findByPartidaIdPartida(partida.getIdPartida()).isPresent()) {
            throw new RuntimeException("Resultado já foi gerado para esta partida.");
        }

        int placarA = partida.getPlacarEquipeA();
        int placarB = partida.getPlacarEquipeB();
        String vencedor = determinarVencedor(placarA, placarB);

        ResultadoModel resultado = ResultadoModel.builder()
                .partida(partida)
                .placarEquipeA(placarA)
                .placarEquipeB(placarB)
                .vencedor(vencedor)
                .dataResultado(LocalDateTime.now())
                .build();

        resultadoRepository.save(resultado);

        // Atualiza estatísticas dos jogadores
        atualizarEstatisticasJogadores(partida, vencedor);

        log.info("Resultado gerado | Partida #{} | EquipeA {} x {} EquipeB | Vencedor: {}",
                partida.getIdPartida(), placarA, placarB, vencedor);

        return resultado;
    }

    public List<ResultadoModel> listarResultados() {
        return resultadoRepository.findAllByOrderByDataResultadoDesc();
    }

    public ResultadoModel buscarResultadoPorPartida(Long idPartida) {
        return resultadoRepository.findByPartidaIdPartida(idPartida)
                .orElseThrow(() -> new RuntimeException("Resultado não encontrado para partida #" + idPartida));
    }

    private String determinarVencedor(int placarA, int placarB) {
        if (placarA > placarB) return "EQUIPE_A";
        if (placarB > placarA) return "EQUIPE_B";
        return "EMPATE";
    }

    private void atualizarEstatisticasJogadores(PartidaModel partida, String vencedor) {
        List<JogadorModel> todosJogadores = new java.util.ArrayList<>();
        todosJogadores.addAll(partida.getEquipeA());
        todosJogadores.addAll(partida.getEquipeB());
        todosJogadores.addAll(partida.getReservas());

        for (JogadorModel jogador : todosJogadores) {
            jogador.setPartidasDisputadas(jogador.getPartidasDisputadas() + 1);

            // Calcula pontuação por resultado
            long pontos = 0;
            if (partida.getEquipeA().contains(jogador) && "EQUIPE_A".equals(vencedor)) pontos += 3;
            else if (partida.getEquipeB().contains(jogador) && "EQUIPE_B".equals(vencedor)) pontos += 3;
            else if ("EMPATE".equals(vencedor)) pontos += 1;

            jogador.setPontuacaoDapartida(pontos);
            jogador.setPontuacaoTotal(jogador.getPontuacaoTotal() + pontos);

            // Calcula média de pontuação
            if (jogador.getPartidasDisputadas() > 0) {
                jogador.setPontuacaoMedia(jogador.getPontuacaoTotal() / jogador.getPartidasDisputadas());
            }
        }

        jogadorRepository.saveAll(todosJogadores);
        log.info("Estatísticas de {} jogadores atualizadas.", todosJogadores.size());
    }
}
