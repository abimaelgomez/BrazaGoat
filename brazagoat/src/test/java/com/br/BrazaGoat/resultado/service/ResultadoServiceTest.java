package com.br.BrazaGoat.resultado.service;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.enums.StatusJogadorPartida;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import com.br.BrazaGoat.partidas.partida.PartidaRepository;
import com.br.BrazaGoat.partidas.partida.StatusPartida;
import com.br.BrazaGoat.resultado.entities.ResultadoModel;
import com.br.BrazaGoat.resultado.repositories.ResultadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultadoServiceTest {

    @Mock private ResultadoRepository resultadoRepository;
    @Mock private PartidaRepository partidaRepository;
    @Mock private JogadorRepository jogadorRepository;

    @InjectMocks
    private ResultadoService resultadoService;

    private PartidaModel partidaFinalizada;
    private JogadorModel jogadorA1, jogadorB1;

    @BeforeEach
    void setUp() {
        jogadorA1 = criarJogador("Carlos", "Silva", 0, 0);
        jogadorB1 = criarJogador("Ana", "Pereira", 0, 0);

        partidaFinalizada = new PartidaModel();
        partidaFinalizada.setIdPartida(1L);
        partidaFinalizada.setStatusPartida(StatusPartida.FINALIZADA);
        partidaFinalizada.setEquipeA(Arrays.asList(jogadorA1));
        partidaFinalizada.setEquipeB(Arrays.asList(jogadorB1));
        partidaFinalizada.setReservas(List.of());
    }

    @Nested
    class GerarResultadoTests {

        @Test
        void deveriaGerarResultadoComVencedorEquipeA() {
            partidaFinalizada.setPlacarEquipeA(3);
            partidaFinalizada.setPlacarEquipeB(1);

            when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.FINALIZADA))
                .thenReturn(partidaFinalizada);
            when(resultadoRepository.findByPartidaIdPartida(1L)).thenReturn(Optional.empty());
            when(resultadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            ResultadoModel resultado = resultadoService.gerarResultado();

            assertNotNull(resultado);
            assertEquals("EQUIPE_A", resultado.getVencedor());
            assertEquals(3, resultado.getPlacarEquipeA());
            assertEquals(1, resultado.getPlacarEquipeB());
        }

        @Test
        void deveriaGerarResultadoComEmpate() {
            partidaFinalizada.setPlacarEquipeA(2);
            partidaFinalizada.setPlacarEquipeB(2);

            when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.FINALIZADA))
                .thenReturn(partidaFinalizada);
            when(resultadoRepository.findByPartidaIdPartida(1L)).thenReturn(Optional.empty());
            when(resultadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            ResultadoModel resultado = resultadoService.gerarResultado();

            assertEquals("EMPATE", resultado.getVencedor());
        }

        @Test
        void deveriaGerarResultadoComVencedorEquipeB() {
            partidaFinalizada.setPlacarEquipeA(0);
            partidaFinalizada.setPlacarEquipeB(2);

            when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.FINALIZADA))
                .thenReturn(partidaFinalizada);
            when(resultadoRepository.findByPartidaIdPartida(1L)).thenReturn(Optional.empty());
            when(resultadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            ResultadoModel resultado = resultadoService.gerarResultado();

            assertEquals("EQUIPE_B", resultado.getVencedor());
        }

        @Test
        void deveriaLancarExcecaoSemPartidaFinalizada() {
            when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.FINALIZADA))
                .thenReturn(null);

            assertThrows(RuntimeException.class, () -> resultadoService.gerarResultado());
        }

        @Test
        void deveriaLancarExcecaoSeResultadoJaExiste() {
            when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.FINALIZADA))
                .thenReturn(partidaFinalizada);
            when(resultadoRepository.findByPartidaIdPartida(1L))
                .thenReturn(Optional.of(new ResultadoModel()));

            assertThrows(RuntimeException.class, () -> resultadoService.gerarResultado());
        }

        @Test
        void deveriaAtualizarPontuacaoJogadorVencedor() {
            partidaFinalizada.setPlacarEquipeA(2);
            partidaFinalizada.setPlacarEquipeB(0);

            when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.FINALIZADA))
                .thenReturn(partidaFinalizada);
            when(resultadoRepository.findByPartidaIdPartida(1L)).thenReturn(Optional.empty());
            when(resultadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            resultadoService.gerarResultado();

            // Jogador da equipe vencedora deve ter 3 pontos (vitória)
            assertEquals(3, jogadorA1.getPontuacaoDapartida());
            assertEquals(1, jogadorA1.getPartidasDisputadas());
            // Jogador perdedor deve ter 0 pontos
            assertEquals(0, jogadorB1.getPontuacaoDapartida());
        }

        @Test
        void deveriaAtualizarPontuacaoEmpate() {
            partidaFinalizada.setPlacarEquipeA(1);
            partidaFinalizada.setPlacarEquipeB(1);

            when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.FINALIZADA))
                .thenReturn(partidaFinalizada);
            when(resultadoRepository.findByPartidaIdPartida(1L)).thenReturn(Optional.empty());
            when(resultadoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            resultadoService.gerarResultado();

            // Em empate, ambos recebem 1 ponto
            assertEquals(1, jogadorA1.getPontuacaoDapartida());
            assertEquals(1, jogadorB1.getPontuacaoDapartida());
        }
    }

    @Nested
    class BuscarResultadoTests {

        @Test
        void deveriaBuscarResultadoPorPartida() {
            ResultadoModel resultado = new ResultadoModel();
            resultado.setVencedor("EQUIPE_A");

            when(resultadoRepository.findByPartidaIdPartida(1L)).thenReturn(Optional.of(resultado));

            ResultadoModel encontrado = resultadoService.buscarResultadoPorPartida(1L);

            assertEquals("EQUIPE_A", encontrado.getVencedor());
        }

        @Test
        void deveriaLancarExcecaoResultadoNaoEncontrado() {
            when(resultadoRepository.findByPartidaIdPartida(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> resultadoService.buscarResultadoPorPartida(99L));
        }
    }

    private JogadorModel criarJogador(String nome, String sobrenome, int golsMarcados, long pontuacao) {
        JogadorModel j = new JogadorModel();
        j.setNome(nome);
        j.setSobrenome(sobrenome);
        j.setGolsMarcados(golsMarcados);
        j.setPontuacaoTotal(pontuacao);
        j.setPartidasDisputadas(0);
        j.setStatusJogadorPartida(StatusJogadorPartida.JOGANDO_PARTIDA);
        return j;
    }
}
