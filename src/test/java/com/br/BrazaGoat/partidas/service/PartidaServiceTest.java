package com.br.BrazaGoat.partidas.service;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.enums.StatusJogadorPartida;
import com.br.BrazaGoat.partidas.partida.*;
import com.br.BrazaGoat.sorteio.entities.SorteioModel;
import com.br.BrazaGoat.sorteio.repositories.SorteioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private SorteioRepository sorteioRepository;

    @InjectMocks
    private PartidaService partidaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void gerarPartida_deveLancarExcecaoQuandoNaoHouverSorteio() {
        // Configurar mock do último número de partida
        when(partidaRepository.findMaxNumeroDaPartida()).thenReturn(null); // Simula nenhum registro encontrado

        // Simular que não há sorteio disponível
        when(sorteioRepository.findTopByOrderByIdSorteioDesc()).thenReturn(null);

        // Verificar lançamento de exceção ao tentar gerar partida sem sorteio
        assertThrows(RuntimeException.class, () -> {
            partidaService.gerarPartida();
        });
    }

    @Test
    void deveriaGerarUmaPartidaComSucesso() {
        // Configurar o mock do último número de partida
        when(partidaRepository.findMaxNumeroDaPartida()).thenReturn(null); // Simula nenhum registro encontrado

        // Configurar o mock do último sorteio
        SorteioModel sorteioMock = new SorteioModel(); // Criar um sorteio de exemplo
        sorteioMock.setIdSorteio(UUID.randomUUID()); // Usar UUID.randomUUID() para criar um UUID
        sorteioMock.setEquipeA(Arrays.asList(
                criarJogadorMock("Carlos", "Silva", "Goleiro", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Miguel", "Santos", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("João", "Oliveira", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA)
        ));
        sorteioMock.setEquipeB(Arrays.asList(
                criarJogadorMock("Ana", "Pereira", "Goleiro", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Silva", "Souza", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Maria", "Costa", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA)
        ));
        sorteioMock.setJogadoresReserva(Arrays.asList(
                criarJogadorMock("Pedro", "Ferreira", "Reserva1", StatusJogadorPartida.RESERVA),
                criarJogadorMock("José", "Nascimento", "Reserva2", StatusJogadorPartida.RESERVA)
        ));
        when(sorteioRepository.findTopByOrderByIdSorteioDesc()).thenReturn(sorteioMock);

        // Criar partida esperada
        PartidaModel partidaEsperada = new PartidaModel();
        partidaEsperada.setIdPartida(1L); // Usar Long para o ID
        partidaEsperada.setNumeroDaPartida(1);
        partidaEsperada.gerada(); // Definir como gerada
        partidaEsperada.associarJogadoresDoSorteio(sorteioMock); // Associar jogadores do sorteio

        // Mock para salvar a nova partida
        when(partidaRepository.save(any(PartidaModel.class))).thenReturn(partidaEsperada);

        // Chamar o método a ser testado
        PartidaRecordDTO resultado = partidaService.gerarPartida();

        // Verificar resultados
        assertNotNull(resultado, "O resultado não deve ser nulo.");
        assertEquals(1, resultado.numeroDaPartida(), "O número da partida deve ser 1.");
        assertEquals(3, resultado.jogadoresEquipeA().size(), "Deveria haver 3 jogadores na Equipe A.");
        assertEquals(3, resultado.jogadoresEquipeB().size(), "Deveria haver 3 jogadores na Equipe B.");
        assertEquals(2, resultado.jogadoresReserva().size(), "Deveria haver 2 jogadores reservas.");

        // Verificar se os jogadores reservas estão com status correto
        List<JogadorModel> jogadoresReserva = resultado.jogadoresReserva();
        for (JogadorModel jogador : jogadoresReserva) {
            assertEquals(StatusJogadorPartida.RESERVA, jogador.getStatusJogadorPartida(), "O jogador reserva deve ter status RESERVA.");
        }
    }

    @Test
    void deveriaConfirmarEscalacaoDosJogadoresDaPartidaGerada() {
        // Configurar o mock do último sorteio
        SorteioModel sorteioMock = new SorteioModel();
        sorteioMock.setIdSorteio(UUID.randomUUID());
        sorteioMock.setEquipeA(Arrays.asList(
                criarJogadorMock("Carlos", "Silva", "Goleiro", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Miguel", "Santos", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("João", "Oliveira", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA)
        ));
        sorteioMock.setEquipeB(Arrays.asList(
                criarJogadorMock("Ana", "Pereira", "Goleiro", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Silva", "Souza", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Maria", "Costa", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA)
        ));
        sorteioMock.setJogadoresReserva(Arrays.asList(
                criarJogadorMock("Pedro", "Ferreira", "Reserva1", StatusJogadorPartida.RESERVA),
                criarJogadorMock("José", "Nascimento", "Reserva2", StatusJogadorPartida.RESERVA)
        ));

        PartidaModel ultimaPartidaGerada = new PartidaModel();
        ultimaPartidaGerada.setIdPartida(1L);
        ultimaPartidaGerada.setNumeroDaPartida(1);
        ultimaPartidaGerada.gerada();
        ultimaPartidaGerada.associarJogadoresDoSorteio(sorteioMock);

        when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(ultimaPartidaGerada);
        when(partidaRepository.save(any(PartidaModel.class))).thenReturn(ultimaPartidaGerada);

        // Chamar o método a ser testado
        partidaService.confirmarEscalacao();

        // Atualizar o status dos jogadores da partida para ESCALADO
        for (JogadorModel jogador : ultimaPartidaGerada.getEquipeA()) {
            jogador.setStatusJogadorPartida(StatusJogadorPartida.ESCALADO);
        }
        for (JogadorModel jogador : ultimaPartidaGerada.getEquipeB()) {
            jogador.setStatusJogadorPartida(StatusJogadorPartida.ESCALADO);
        }
        for (JogadorModel jogador : ultimaPartidaGerada.getReservas()) {
            jogador.setStatusJogadorPartida(StatusJogadorPartida.RESERVA);
        }

        // Verificar se a partida foi atualizada para o estado de "Aguardando Início"
        Assertions.assertEquals(StatusPartida.AGUARDANDO_INICIO, ultimaPartidaGerada.getStatusPartida(), "A partida deve estar com status AGUARDANDO_INICIO.");

        // Verificar se os jogadores foram escalados
        for (JogadorModel jogador : ultimaPartidaGerada.getEquipeA()) {
            assertEquals(StatusJogadorPartida.ESCALADO, jogador.getStatusJogadorPartida(), "O jogador da Equipe A deve ter status ESCALADO.");
        }
        for (JogadorModel jogador : ultimaPartidaGerada.getEquipeB()) {
            assertEquals(StatusJogadorPartida.ESCALADO, jogador.getStatusJogadorPartida(), "O jogador da Equipe B deve ter status ESCALADO.");
        }
        for (JogadorModel jogador : ultimaPartidaGerada.getReservas()) {
            assertEquals(StatusJogadorPartida.RESERVA, jogador.getStatusJogadorPartida(), "O jogador reserva deve ter status RESERVA.");
        }

        // Mostrar no console as equipes escaladas
        System.out.println("------------------------------------------------------");
        System.out.println("# Equipe A Escalada:");
        for (JogadorModel jogador : ultimaPartidaGerada.getEquipeA()) {
            System.out.println(jogador.getNome() + " " + jogador.getSobrenome() + " - " + jogador.getStatusJogadorPartida());
        }
        System.out.println("------------------------------------------------------");
        System.out.println("# Equipe B Escalada:");
        for (JogadorModel jogador : ultimaPartidaGerada.getEquipeB()) {
            System.out.println(jogador.getNome() + " " + jogador.getSobrenome() + " - " + jogador.getStatusJogadorPartida());
        }
        System.out.println("------------------------------------------------------");
        System.out.println("# Jogadores na Reservas:");
        for (JogadorModel jogador : ultimaPartidaGerada.getReservas()) {
            System.out.println(jogador.getNome() + " " + jogador.getSobrenome() + " - " + jogador.getStatusJogadorPartida());
        }
        System.out.println("------------------------------------------------------");
    }

    @Test
    void deveriaLancarExcecaoQuandoPartidaNaoEstaGerada() {
        // Simular que não há partida gerada
        when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(null);

        // Verificar lançamento de exceção ao tentar confirmar escalacao sem partida gerada
        assertThrows(RuntimeException.class, () -> {
            partidaService.confirmarEscalacao();
        });
    }

    @Test
    void deveriaIniciarPartidaComSucesso() {
        // Criar uma partida previamente escalada
        PartidaModel partidaEscalada = new PartidaModel();
        partidaEscalada.setIdPartida(1L);
        partidaEscalada.setNumeroDaPartida(1);
        partidaEscalada.gerada();

        // Simular jogadores escalados para a partida
        List<JogadorModel> jogadoresEquipeA = Arrays.asList(
                criarJogadorMock("Carlos", "Silva", "Goleiro", StatusJogadorPartida.ESCALADO),
                criarJogadorMock("Miguel", "Santos", "Linha", StatusJogadorPartida.ESCALADO),
                criarJogadorMock("João", "Oliveira", "Linha", StatusJogadorPartida.ESCALADO)
        );
        List<JogadorModel> jogadoresEquipeB = Arrays.asList(
                criarJogadorMock("Ana", "Pereira", "Goleiro", StatusJogadorPartida.ESCALADO),
                criarJogadorMock("Silva", "Souza", "Linha", StatusJogadorPartida.ESCALADO),
                criarJogadorMock("Maria", "Costa", "Linha", StatusJogadorPartida.ESCALADO)
        );
        List<JogadorModel> jogadoresReserva = Arrays.asList(
                criarJogadorMock("Pedro", "Ferreira", "Reserva1", StatusJogadorPartida.RESERVA),
                criarJogadorMock("José", "Nascimento", "Reserva2", StatusJogadorPartida.RESERVA)
        );

        partidaEscalada.setEquipeA(jogadoresEquipeA);
        partidaEscalada.setEquipeB(jogadoresEquipeB);
        partidaEscalada.setReservas(jogadoresReserva);
        partidaEscalada.setStatusPartida(StatusPartida.AGUARDANDO_INICIO);

        when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaEscalada);
        when(partidaRepository.save(any(PartidaModel.class))).thenReturn(partidaEscalada);

        // Chamar o método a ser testado
        partidaService.iniciarPartida();

        // Verificar se a partida foi iniciada com sucesso
        assertEquals(StatusPartida.PARTIDA_EM_ANDAMENTO, partidaEscalada.getStatusPartida(), "A partida deve estar com status PARTIDA_EM_ANDAMENTO.");
        assertTrue(partidaEscalada.isPartidaIniciada(), "A partida deve estar marcada como iniciada.");

        // Verificar se os jogadores estão com status correto para JOGANDO_PARTIDA
        for (JogadorModel jogador : jogadoresEquipeA) {
            assertEquals(StatusJogadorPartida.JOGANDO_PARTIDA, jogador.getStatusJogadorPartida(), "O jogador da Equipe A deve ter status JOGANDO_PARTIDA.");
        }
        for (JogadorModel jogador : jogadoresEquipeB) {
            assertEquals(StatusJogadorPartida.JOGANDO_PARTIDA, jogador.getStatusJogadorPartida(), "O jogador da Equipe B deve ter status JOGANDO_PARTIDA.");
        }
        for (JogadorModel jogador : jogadoresReserva) {
            assertEquals(StatusJogadorPartida.RESERVA, jogador.getStatusJogadorPartida(), "O jogador reserva deve ter status RESERVA.");
        }
    }

    @Test
    void deveriaLancarExcecaoQuandoNaoHouverPartidaEmAndamento() {
        // Simular que não há partida em andamento
        PartidaModel partidaNaoIniciada = new PartidaModel();
        partidaNaoIniciada.setIdPartida(1L);
        partidaNaoIniciada.setNumeroDaPartida(1);
        partidaNaoIniciada.gerada();
        when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaNaoIniciada);

        // Verificar lançamento de exceção ao tentar finalizar partida que não está em andamento
        assertThrows(RuntimeException.class, () -> {
            partidaService.finalizarPartida();
        });
    }

    @Test
    void deveriaLancarExcecaoSePartidaNaoEstiverEscalada() {
        // Criar uma partida não escalada
        PartidaModel partidaNaoEscalada = new PartidaModel();
        partidaNaoEscalada.setIdPartida(1L);
        partidaNaoEscalada.setNumeroDaPartida(1);
        partidaNaoEscalada.gerada();
        partidaNaoEscalada.setStatusPartida(StatusPartida.PARTIDA_GERADA); // Status diferente de AGUARDANDO_INICIO

        when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaNaoEscalada);

        // Chamar o método a ser testado e verificar a exceção
        assertThrows(RuntimeException.class, () -> partidaService.iniciarPartida(), "Você precisa confirmar a escalação antes de iniciar a partida.");
    }

    @Test
    void deveriaFinalizarPartidaComSucesso() {
        // Criar uma partida previamente em andamento
        PartidaModel partidaEmAndamento = new PartidaModel();
        partidaEmAndamento.setIdPartida(1L);
        partidaEmAndamento.setNumeroDaPartida(1);
        partidaEmAndamento.gerada();
        partidaEmAndamento.EmAndamento();
        partidaEmAndamento.setPartidaIniciada(true);

        // Simular jogadores em partida
        List<JogadorModel> jogadoresEquipeA = Arrays.asList(
                criarJogadorMock("Carlos", "Silva", "Goleiro", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Miguel", "Santos", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("João", "Oliveira", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA)
        );
        List<JogadorModel> jogadoresEquipeB = Arrays.asList(
                criarJogadorMock("Ana", "Pereira", "Goleiro", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Silva", "Souza", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA),
                criarJogadorMock("Maria", "Costa", "Linha", StatusJogadorPartida.JOGANDO_PARTIDA)
        );
        List<JogadorModel> jogadoresReserva = Arrays.asList(
                criarJogadorMock("Pedro", "Ferreira", "Reserva1", StatusJogadorPartida.RESERVA),
                criarJogadorMock("José", "Nascimento", "Reserva2", StatusJogadorPartida.RESERVA)
        );

        partidaEmAndamento.setEquipeA(jogadoresEquipeA);
        partidaEmAndamento.setEquipeB(jogadoresEquipeB);
        partidaEmAndamento.setReservas(jogadoresReserva);

        when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaEmAndamento);

        // Chamar o método a ser testado
        partidaService.finalizarPartida();

        // Verificar se a partida foi finalizada com sucesso
        assertEquals(StatusPartida.FINALIZADA, partidaEmAndamento.getStatusPartida(), "A partida deve estar com status PARTIDA_FINALIZADA.");
        assertTrue(partidaEmAndamento.isPartidaIniciada(), "A partida deve estar marcada como iniciada.");

        // Verificar se os jogadores estão com status correto após finalização
        for (JogadorModel jogador : jogadoresEquipeA) {
            assertEquals(StatusJogadorPartida.FORA_DA_PARTIDA, jogador.getStatusJogadorPartida(), "O jogador da Equipe A deve ter status FINALIZADO.");
        }
        for (JogadorModel jogador : jogadoresEquipeB) {
            assertEquals(StatusJogadorPartida.FORA_DA_PARTIDA, jogador.getStatusJogadorPartida(), "O jogador da Equipe B deve ter status FINALIZADO.");
        }
        for (JogadorModel jogador : jogadoresReserva) {
            assertEquals(StatusJogadorPartida.FORA_DA_PARTIDA, jogador.getStatusJogadorPartida(), "O jogador reserva deve ter status RESERVA.");
        }
    }

    @Test
    void deveriaLancarExcecaoSePartidaNaoEstiverEmAndamento() {
        // Criar uma partida não em andamento
        PartidaModel partidaNaoEmAndamento = new PartidaModel();
        partidaNaoEmAndamento.setIdPartida(1L);
        partidaNaoEmAndamento.setNumeroDaPartida(1);
        partidaNaoEmAndamento.gerada();
        partidaNaoEmAndamento.setStatusPartida(StatusPartida.AGUARDANDO_INICIO); // Status diferente de PARTIDA_EM_ANDAMENTO

        when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaNaoEmAndamento);

        // Chamar o método a ser testado e verificar a exceção
        assertThrows(RuntimeException.class, () -> partidaService.finalizarPartida(), "A partida precisa estar em andamento para ser finalizada.");
    }

    private JogadorModel criarJogadorMock(String nome, String sobrenome, String posicao, StatusJogadorPartida status) {
        JogadorModel jogador = new JogadorModel();
        jogador.setNome(nome);
        jogador.setSobrenome(sobrenome);
        jogador.setPosicao(posicao);
        jogador.setStatusJogadorPartida(status);
        return jogador;
    }
}
