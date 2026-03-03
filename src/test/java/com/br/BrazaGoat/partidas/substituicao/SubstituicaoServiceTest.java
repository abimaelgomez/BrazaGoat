package com.br.BrazaGoat.partidas.substituicao;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.enums.StatusJogadorPartida;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import com.br.BrazaGoat.partidas.partida.PartidaRepository;
import com.br.BrazaGoat.partidas.partida.StatusPartida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubstituicaoServiceTest {

    @Mock private SubstituicaoRepository substituicaoRepository;
    @Mock private PartidaRepository partidaRepository;
    @Mock private JogadorRepository jogadorRepository;

    @InjectMocks
    private SubstituicaoService substituicaoService;

    private PartidaModel partidaEmAndamento;
    private JogadorModel jogadorEmCampo;
    private JogadorModel jogadorReserva;
    private UUID idEmCampo, idReserva;

    @BeforeEach
    void setUp() {
        idEmCampo = UUID.randomUUID();
        idReserva = UUID.randomUUID();

        jogadorEmCampo = new JogadorModel();
        jogadorEmCampo.setIdJogador(idEmCampo);
        jogadorEmCampo.setNome("Titular");
        jogadorEmCampo.setStatusJogadorPartida(StatusJogadorPartida.JOGANDO_PARTIDA);

        jogadorReserva = new JogadorModel();
        jogadorReserva.setIdJogador(idReserva);
        jogadorReserva.setNome("Reserva");
        jogadorReserva.setStatusJogadorPartida(StatusJogadorPartida.RESERVA);

        partidaEmAndamento = new PartidaModel();
        partidaEmAndamento.setIdPartida(1L);
        partidaEmAndamento.setStatusPartida(StatusPartida.PARTIDA_EM_ANDAMENTO);
        partidaEmAndamento.setEquipeA(new ArrayList<>());
        partidaEmAndamento.getEquipeA().add(jogadorEmCampo);
        partidaEmAndamento.setEquipeB(new ArrayList<>());
        partidaEmAndamento.setReservas(new ArrayList<>());
        partidaEmAndamento.getReservas().add(jogadorReserva);
    }

    @Test
    void deveriaRealizarSubstituicaoComSucesso() {
        when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.PARTIDA_EM_ANDAMENTO))
            .thenReturn(partidaEmAndamento);
        when(jogadorRepository.findById(idEmCampo)).thenReturn(Optional.of(jogadorEmCampo));
        when(jogadorRepository.findById(idReserva)).thenReturn(Optional.of(jogadorReserva));
        when(substituicaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SubstituicaoRecordDTO dto = new SubstituicaoRecordDTO(idEmCampo, idReserva);
        SubstituicaoModel resultado = substituicaoService.realizarSubstituicao(dto);

        assertNotNull(resultado);
        assertEquals(jogadorEmCampo, resultado.getJogadorSaiu());
        assertEquals(jogadorReserva, resultado.getJogadorEntrou());
        assertEquals(StatusJogadorPartida.SUBSTITUIDO, jogadorEmCampo.getStatusJogadorPartida());
        assertEquals(StatusJogadorPartida.JOGANDO_PARTIDA, jogadorReserva.getStatusJogadorPartida());
    }

    @Test
    void deveriaLancarExcecaoSemPartidaEmAndamento() {
        when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.PARTIDA_EM_ANDAMENTO))
            .thenReturn(null);

        SubstituicaoRecordDTO dto = new SubstituicaoRecordDTO(idEmCampo, idReserva);
        assertThrows(RuntimeException.class, () -> substituicaoService.realizarSubstituicao(dto));
    }

    @Test
    void deveriaLancarExcecaoJogadorNaoEstaEmCampo() {
        JogadorModel jogadorForaDaPartida = new JogadorModel();
        UUID idFora = UUID.randomUUID();
        jogadorForaDaPartida.setIdJogador(idFora);
        jogadorForaDaPartida.setNome("Fora");

        when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.PARTIDA_EM_ANDAMENTO))
            .thenReturn(partidaEmAndamento);
        when(jogadorRepository.findById(idFora)).thenReturn(Optional.of(jogadorForaDaPartida));
        when(jogadorRepository.findById(idReserva)).thenReturn(Optional.of(jogadorReserva));

        SubstituicaoRecordDTO dto = new SubstituicaoRecordDTO(idFora, idReserva);
        assertThrows(RuntimeException.class, () -> substituicaoService.realizarSubstituicao(dto));
    }

    @Test
    void deveriaLancarExcecaoJogadorNaoEhReserva() {
        JogadorModel jogadorNaoReserva = new JogadorModel();
        UUID idNaoReserva = UUID.randomUUID();
        jogadorNaoReserva.setIdJogador(idNaoReserva);
        jogadorNaoReserva.setNome("NaoReserva");

        when(partidaRepository.findTopByStatusPartidaOrderByIdPartidaDesc(StatusPartida.PARTIDA_EM_ANDAMENTO))
            .thenReturn(partidaEmAndamento);
        when(jogadorRepository.findById(idEmCampo)).thenReturn(Optional.of(jogadorEmCampo));
        when(jogadorRepository.findById(idNaoReserva)).thenReturn(Optional.of(jogadorNaoReserva));

        SubstituicaoRecordDTO dto = new SubstituicaoRecordDTO(idEmCampo, idNaoReserva);
        assertThrows(RuntimeException.class, () -> substituicaoService.realizarSubstituicao(dto));
    }
}
