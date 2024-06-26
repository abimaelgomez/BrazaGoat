package com.br.BrazaGoat.partidas.gol;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.enums.StatusJogadorPartida;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import com.br.BrazaGoat.partidas.partida.PartidaRepository;
import com.br.BrazaGoat.partidas.partida.StatusPartida;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Classe de testes para GolService.
 * Utiliza JUnit e Mockito para testar a adição de gols e a alteração de status dos gols em uma partida.
 */
@ExtendWith(MockitoExtension.class)
class GolServiceTest {

    @Mock
    private GolRepository golRepository;
    @Mock
    private PartidaRepository partidaRepository;
    @InjectMocks
    private GolService golService;

    private PartidaModel partidaEmAndamento;
    private JogadorModel jogadorEquipeA;
    private JogadorModel jogadorEquipeB;

    /**
     * Configuração inicial dos testes. Cria uma partida em andamento com equipes escaladas.
     */
    @BeforeEach
    void setUp() {
        // Criação de uma partida em andamento com equipes escaladas
        partidaEmAndamento = criarPartidaComEquipes();
        jogadorEquipeA = partidaEmAndamento.getEquipeA().get(0);
        jogadorEquipeB = partidaEmAndamento.getEquipeB().get(0);
    }

    /**
     * Limpeza após cada teste, se necessário.
     */
    @AfterEach
    void tearDown() {
        // Limpa recursos após cada teste, se necessário
    }

    /**
     * Teste para imprimir informações da partida antes de adicionar gols.
     */
    @Test
    void imprimirInformacoesDaPartidaAntesDosTestes() {
        // Imprimir informações da partida antes dos testes de adicionar gol
        imprimirInformacoesPartida(partidaEmAndamento);
    }

    /**
     * Classe aninhada para testes de adição de gols.
     */
    @Nested
    class AdicionarTipoDeGolsTests {

        /**
         * Teste para adicionar um gol normal para a equipe A.
         */
        @Test
        void deveriaAdicionarGolNormalParaEquipeA() {
            // Simula a recuperação da partida em andamento
            when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaEmAndamento);
            // Adiciona um gol normal para a equipe A
            golService.adicionarGol(jogadorEquipeA.getIdJogador(), TipoDeGol.NORMAL, false);
            // Verifica se o gol foi salvo e a partida atualizada
            verify(golRepository, times(1)).save(any(GolModel.class));
            verify(partidaRepository, times(1)).save(partidaEmAndamento);
            // Verificar se o status do gol foi VALIDADO após a adição
            GolModel golAdicionado = capturarGolAdicionado();
            assertEquals(StatusGol.VALIDADO, golAdicionado.getStatusGol(), "O status do gol deve ser VALIDADO após a adição.");
            // Verificar o placar atualizado
            assertEquals(1, partidaEmAndamento.getPlacarEquipeA(), "O placar da Equipe A deve ser 1 após adicionar um gol normal.");
            assertEquals(0, partidaEmAndamento.getPlacarEquipeB(), "O placar da Equipe B deve ser 0.");
            imprimirResultadoGol(jogadorEquipeA.getNome(), TipoDeGol.NORMAL, partidaEmAndamento.getPlacarEquipeA(), partidaEmAndamento.getPlacarEquipeB());
        }

        /**
         * Teste para adicionar um gol contra para a equipe B.
         */
        @Test
        void deveriaAdicionarGolContraParaEquipeB() {
            // Simula a recuperação da partida em andamento
            when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaEmAndamento);
            // Adiciona um gol contra para a equipe B
            golService.adicionarGol(jogadorEquipeA.getIdJogador(), TipoDeGol.CONTRA, true);
            // Verifica se o gol foi salvo e a partida atualizada
            verify(golRepository, times(1)).save(any(GolModel.class));
            verify(partidaRepository, times(1)).save(partidaEmAndamento);
            // Verificar se o status do gol foi VALIDADO após a adição
            GolModel golAdicionado = capturarGolAdicionado();
            assertEquals(StatusGol.VALIDADO, golAdicionado.getStatusGol(), "O status do gol deve ser VALIDADO após a adição.");
            // Verificar o placar atualizado
            assertEquals(0, partidaEmAndamento.getPlacarEquipeA(), "O placar da Equipe A deve ser 0.");
            assertEquals(1, partidaEmAndamento.getPlacarEquipeB(), "O placar da Equipe B deve ser 1 após adicionar um gol contra.");
            imprimirResultadoGol(jogadorEquipeA.getNome(), TipoDeGol.CONTRA, partidaEmAndamento.getPlacarEquipeA(), partidaEmAndamento.getPlacarEquipeB());
        }

        /**
         * Teste para adicionar um gol de pênalti para a equipe A.
         */
        @Test
        void deveriaAdicionarGolDePenaltiParaEquipeA() {
            // Simula a recuperação da partida em andamento
            when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaEmAndamento);
            // Adiciona um gol de pênalti para a equipe A
            golService.adicionarGol(jogadorEquipeA.getIdJogador(), TipoDeGol.PENALTI, false);
            // Verifica se o gol foi salvo e a partida atualizada
            verify(golRepository, times(1)).save(any(GolModel.class));
            verify(partidaRepository, times(1)).save(partidaEmAndamento);
            // Verificar se o status do gol foi VALIDADO após a adição
            GolModel golAdicionado = capturarGolAdicionado();
            assertEquals(StatusGol.VALIDADO, golAdicionado.getStatusGol(), "O status do gol deve ser VALIDADO após a adição.");
            // Verificar o placar atualizado
            assertEquals(1, partidaEmAndamento.getPlacarEquipeA(), "O placar da Equipe A deve ser 1 após adicionar um gol de pênalti.");
            assertEquals(0, partidaEmAndamento.getPlacarEquipeB(), "O placar da Equipe B deve ser 0.");
            imprimirResultadoGol(jogadorEquipeA.getNome(), TipoDeGol.PENALTI, partidaEmAndamento.getPlacarEquipeA(), partidaEmAndamento.getPlacarEquipeB());
        }

        @Test
        void deveriaAdicionarGolDeCabecaParaEquipeB() {
            // Simula a recuperação da partida em andamento
            when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaEmAndamento);
            // Adiciona um gol de pênalti para a equipe A
            golService.adicionarGol(jogadorEquipeB.getIdJogador(), TipoDeGol.CABECA, false);
            // Verifica se o gol foi salvo e a partida atualizada
            verify(golRepository, times(1)).save(any(GolModel.class));
            verify(partidaRepository, times(1)).save(partidaEmAndamento);
            // Verificar se o status do gol foi VALIDADO após a adição
            GolModel golAdicionado = capturarGolAdicionado();
            assertEquals(StatusGol.VALIDADO, golAdicionado.getStatusGol(), "O status do gol deve ser VALIDADO após a adição.");
            // Verificar o placar atualizado
            assertEquals(0, partidaEmAndamento.getPlacarEquipeA(), "O placar da Equipe A deve ser 0.");
            assertEquals(1, partidaEmAndamento.getPlacarEquipeB(), "O placar da Equipe B deve ser 1 após adicionar um gol de cabeça.");
            imprimirResultadoGol(jogadorEquipeB.getNome(), TipoDeGol.CABECA, partidaEmAndamento.getPlacarEquipeA(), partidaEmAndamento.getPlacarEquipeB());
        }

        @Test
        void deveriaAdicionarGolDeTiroLivreParaEquipeA() {
            // Simula a recuperação da partida em andamento
            when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaEmAndamento);
            // Adiciona um gol de pênalti para a equipe A
            golService.adicionarGol(jogadorEquipeA.getIdJogador(), TipoDeGol.TIRO_LIVRE, false);
            // Verifica se o gol foi salvo e a partida atualizada
            verify(golRepository, times(1)).save(any(GolModel.class));
            verify(partidaRepository, times(1)).save(partidaEmAndamento);
            // Verificar se o status do gol foi VALIDADO após a adição
            GolModel golAdicionado = capturarGolAdicionado();
            assertEquals(StatusGol.VALIDADO, golAdicionado.getStatusGol(), "O status do gol deve ser VALIDADO após a adição.");
            // Verificar o placar atualizado
            assertEquals(1, partidaEmAndamento.getPlacarEquipeA(), "O placar da Equipe A deve ser 1 após adicionar um gol de tiro livre.");
            assertEquals(0, partidaEmAndamento.getPlacarEquipeB(), "O placar da Equipe B deve ser 0.");
            imprimirResultadoGol(jogadorEquipeA.getNome(), TipoDeGol.TIRO_LIVRE, partidaEmAndamento.getPlacarEquipeA(), partidaEmAndamento.getPlacarEquipeB());
        }

        /**
         * Captura o gol adicionado durante o teste.
         * @return O gol adicionado.
         */
        private GolModel capturarGolAdicionado() {
            ArgumentCaptor<GolModel> golCaptor = ArgumentCaptor.forClass(GolModel.class);
            verify(golRepository).save(golCaptor.capture());
            return golCaptor.getValue();
        }

    }

    @Nested
    class AlterarStatusGolNaPartidaTests {

        /**
         * Teste para validar um gol em uma partida.
         * Verifica se o status do gol é alterado para VALIDADO após a validação.
         */
        @Test
        void deveriaValidarGolEmPartida() {
            GolModel gol = new GolModel();
            gol.setPartida(partidaEmAndamento);

            // Simula que o gol já existe no repositório
            when(golRepository.findById(gol.getId())).thenReturn(Optional.of(gol));

            golService.validarGol(gol);

            assertEquals(StatusGol.VALIDADO, gol.getStatusGol(), "O status do gol deve ser VALIDADO após a validação.");
            imprimirStatusGol(gol);
            imprimirResultadoGol(jogadorEquipeA.getNome(), TipoDeGol.NORMAL, partidaEmAndamento.getPlacarEquipeA(), partidaEmAndamento.getPlacarEquipeB());
        }

        /**
         * Teste para invalidar um gol em uma partida.
         * Verifica se o status do gol é alterado para INVALIDADO após a invalidação.
         */
        @Test
        void deveriaInvalidarGolEmPartida() {
            GolModel gol = new GolModel();
            gol.setPartida(partidaEmAndamento);

            golService.invalidarGol(gol);

            assertEquals(StatusGol.INVALIDADO, gol.getStatusGol(), "O status do gol deve ser INVALIDADO após a invalidação.");
            imprimirStatusGol(gol);
            System.out.println("O gol foi invalidado!");
        }

        /**
         * Teste para anular um gol em uma partida.
         * Verifica se o status do gol é alterado para ANULADO após a anulação.
         */
        @Test
        void deveriaAnularGolEmPartida() {
            System.out.println("Gol validado!");
            GolModel gol = new GolModel();
            gol.setPartida(partidaEmAndamento);

            golService.anularGol(gol);

            System.out.println("Ops...");
            System.out.println("Analisando gol...");
            assertEquals(StatusGol.ANULADO, gol.getStatusGol(), "O status do gol deve ser ANULADO após a anulação.");
            imprimirStatusGol(gol);
            System.out.println("O gol foi anulado");
        }

        /**
         * Teste para revisar um gol em uma partida.
         * Verifica se o status do gol é alterado para EM_REVISAO após a revisão.
         */
        @Test
        void deveriaRevisarGolEmPartida() {
            GolModel gol = new GolModel();
            gol.setPartida(partidaEmAndamento);

            golService.revisarGol(gol);

            assertEquals(StatusGol.EM_REVISAO, gol.getStatusGol(), "O status do gol deve ser EM_REVISAO após a revisão.");
            imprimirStatusGol(gol);
            System.out.println("Gol está em revisão!");
        }

        /**
         * Teste para revisar e validar um gol em uma partida.
         * Verifica se o status do gol é alterado para EM_REVISAO durante a revisão
         * e para VALIDADO após a validação.
         */
        @Test
        void deveriaRevisarEValidarGolEmPartida() {
            GolModel gol = new GolModel();
            gol.setPartida(partidaEmAndamento);

            // Simula que o gol já existe no repositório
            when(golRepository.findById(gol.getId())).thenReturn(Optional.of(gol));

            golService.revisarGol(gol);
            assertEquals(StatusGol.EM_REVISAO, gol.getStatusGol(), "O status do gol deve ser EM_REVISAO após a revisão.");
            imprimirStatusGol(gol);

            // Simula a validação do gol após a revisão
            golService.validarGol(gol);
            assertEquals(StatusGol.VALIDADO, gol.getStatusGol(), "O status do gol deve ser VALIDADO após a validação.");
            imprimirStatusGol(gol);
            imprimirResultadoGol(jogadorEquipeA.getNome(), TipoDeGol.NORMAL, partidaEmAndamento.getPlacarEquipeA(), partidaEmAndamento.getPlacarEquipeB());
        }

        /**
         * Teste para revisar e invalidar um gol em uma partida.
         * Verifica se o status do gol é alterado para EM_REVISAO durante a revisão
         * e para INVALIDADO após a invalidação.
         */
        @Test
        void deveriaRevisarEInvalidarGolEmPartida() {
            GolModel gol = new GolModel();
            gol.setPartida(partidaEmAndamento);

            golService.revisarGol(gol);
            assertEquals(StatusGol.EM_REVISAO, gol.getStatusGol(), "O status do gol deve ser EM_REVISAO após a revisão.");
            imprimirStatusGol(gol);

            // Invalidando o gol após revisão
            System.out.println("...");
            golService.invalidarGol(gol);
            assertEquals(StatusGol.INVALIDADO, gol.getStatusGol(), "O status do gol deve ser INVALIDADO após a revisão.");
            System.out.println("O GOL FOI REVISADO E INVALIDADO!");
        }

        /**
         * Teste para verificar a validação de um gol inexistente.
         * Verifica se o status do gol é alterado para INVALIDADO se o gol não existir.
         */
        @Test
        void naoDeveriaValidarGolInexistente() {
            GolModel golInexistente = new GolModel();
            golInexistente.setId(1L);

            when(golRepository.findById(golInexistente.getId())).thenReturn(Optional.empty());

            golService.validarGol(golInexistente);

            assertEquals(StatusGol.INVALIDADO, golInexistente.getStatusGol(), "O status do gol deve ser INVALIDADO se o gol não existir.");
        }

        /**
         * Teste para verificar a validação de um gol já invalidado.
         * Verifica se o status do gol permanece INVALIDADO se ele já foi invalidado.
         */
        @Test
        void naoDeveriaValidarGolJaInvalidado() {
            GolModel gol = new GolModel();
            gol.setPartida(partidaEmAndamento);
            gol.setStatusGol(StatusGol.INVALIDADO);

            golService.validarGol(gol);

            assertEquals(StatusGol.INVALIDADO, gol.getStatusGol(), "O status do gol deve permanecer INVALIDADO se ele já foi invalidado.");
        }

        /**
         * Teste para atualizar o placar corretamente para um gol de cabeça.
         * Verifica se o placar da partida é atualizado corretamente após adicionar um gol de cabeça.
         */
        @Test
        void deveriaAtualizarPlacarCorretamenteParaGolDeCabeca() {
            when(partidaRepository.findTopByOrderByIdPartidaDesc()).thenReturn(partidaEmAndamento);

            golService.adicionarGol(jogadorEquipeB.getIdJogador(), TipoDeGol.CABECA, false);

            verify(golRepository, times(1)).save(any(GolModel.class));
            verify(partidaRepository, times(1)).save(partidaEmAndamento);

            assertEquals(0, partidaEmAndamento.getPlacarEquipeA(), "O placar da Equipe A deve ser 0.");
            assertEquals(1, partidaEmAndamento.getPlacarEquipeB(), "O placar da Equipe B deve ser 1 após adicionar um gol de cabeça.");
            System.out.println("Foi marcado um gol de " + TipoDeGol.CABECA);
        }
    }

    /**
     * Cria uma partida com equipes para os testes.
     * @return A partida criada.
     */
    private PartidaModel criarPartidaComEquipes() {
        PartidaModel partida = new PartidaModel();

        List<JogadorModel> jogadoresEquipeA = Arrays.asList(
                criarJogadorMock(UUID.randomUUID(), "Neymar", "Junior", "Atacante", 10, StatusJogadorPartida.ESCALADO),
                criarJogadorMock(UUID.randomUUID(), "Silva", "Thiago", "Zagueiro", 3, StatusJogadorPartida.ESCALADO)
        );
        List<JogadorModel> jogadoresEquipeB = Arrays.asList(
                criarJogadorMock(UUID.randomUUID(), "Messi", "Lionel", "Atacante", 10, StatusJogadorPartida.ESCALADO),
                criarJogadorMock(UUID.randomUUID(), "Ramos", "Sergio", "Zagueiro", 4, StatusJogadorPartida.ESCALADO),
                criarJogadorMock(UUID.randomUUID(), "Lewandowsky", "Robert", "Linha", 9, StatusJogadorPartida.ESCALADO)
        );
        List<JogadorModel> jogadoresReserva = Arrays.asList(
                criarJogadorMock(UUID.randomUUID(), "Mbappe", "Killian", "Reserva", 10, StatusJogadorPartida.RESERVA),
                criarJogadorMock(UUID.randomUUID(), "Halland", "Erling", "Reserva", 9, StatusJogadorPartida.RESERVA)
        );

        partida.setIdPartida(1L);
        partida.setEquipeA(jogadoresEquipeA);
        partida.setEquipeB(jogadoresEquipeB);
        partida.setReservas(jogadoresReserva);
        partida.setStatusPartida(StatusPartida.PARTIDA_EM_ANDAMENTO); // Definir status como em andamento

        partida.setPlacarEquipeA(0);
        partida.setPlacarEquipeB(0);

        return partida;
    }

    private JogadorModel criarJogadorMock(UUID id, String nome, String sobrenome, String posicao, int numeroCamisa, StatusJogadorPartida status) {
        JogadorModel jogador = new JogadorModel();
        jogador.setIdJogador(id);
        jogador.setNome(nome);
        jogador.setSobrenome(sobrenome);
        jogador.setPosicao(posicao);
        jogador.setNumeroCamisa(numeroCamisa);
        jogador.setStatusJogadorPartida(status);
        return jogador;
    }
    /**
     * Imprime as informações da partida para verificação.
     * @param partida A partida a ser impressa.
     */
    private void imprimirInformacoesPartida(PartidaModel partida) {
        System.out.println("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -");
        System.out.println("Partida  # Nº(" + partidaEmAndamento.getIdPartida() + ").");
        System.out.println("_________________ ESCALAÇÂO _________________\n");
        System.out.println("------------------- EQUIPE A -------------------");
        partida.getEquipeA().forEach(jogador -> System.out.println(jogador.getPosicao() + " - " + jogador.getNome() + " Nº( " + jogador.getNumeroCamisa() + " )"));
        System.out.println("------------------- EQUIPE B -------------------");
        partida.getEquipeB().forEach(jogador -> System.out.println(jogador.getPosicao() + " - " + jogador.getNome() + " Nº( " + jogador.getNumeroCamisa() + " )"));
        System.out.println("------------------- RESERVAS -------------------");
        partida.getReservas().forEach(jogador -> System.out.println(jogador.getPosicao() + " - " + jogador.getNome() + " Nº( " + jogador.getNumeroCamisa() + " )"));
        System.out.println("_________________ PLACAR _________________");
        System.out.println(" EQUIPE A - " + partida.getPlacarEquipeA() + " X " + partida.getPlacarEquipeB() + " - EQUIPE B");
        System.out.println("-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -");
    }

    /**
     * Imprime o resultado após a adição de um gol.
     * @param nomeJogador Nome do jogador que marcou o gol.
     * @param tipoDeGol Tipo de gol marcado.
     * @param placarEquipeA Placar da equipe A.
     * @param placarEquipeB Placar da equipe B.
     * Mostra o tipo de de gol de acordo com o gol marcado.
     */
    private void imprimirResultadoGol(String nomeJogador, TipoDeGol tipoDeGol, int placarEquipeA, int placarEquipeB) {
        switch (tipoDeGol) {
            case NORMAL:
                System.out.println(nomeJogador + " marcou um gol normal.");
                break;
            case CONTRA:
                System.out.println(nomeJogador + " marcou um gol contra.");
                break;
            case CABECA:
                System.out.println(nomeJogador + " marcou um gol de cabeça.");
                break;
            case PENALTI:
                System.out.println(nomeJogador + " marcou um gol de pênalti.");
                break;
            case TIRO_LIVRE:
                System.out.println(nomeJogador + " marcou um gol de tiro livre.");
                break;
            default:
                System.out.println(nomeJogador + " marcou um gol.");
                break;
        }
        System.out.println("EQUIPE A " + placarEquipeA + " X " + placarEquipeB + " - Equipe B\n");
    }

    /**
     * Imprime o resultado após a adição de um gol.
     * @param gol Imprime o Status do gol de acordo com o status.
     */
    private void imprimirStatusGol(GolModel gol) {
        switch (gol.getStatusGol()) {
            case VALIDADO:
                System.out.println("Status do gol: VALIDADO");
                break;
            case INVALIDADO:
                System.out.println("Status do gol: INVALIDADO");
                break;
            case ANULADO:
                System.out.println("Status do gol: ANULADO");
                break;
            case EM_REVISAO:
                System.out.println("Status do gol: EM REVISÃO");
                break;
            default:
                System.out.println("Status do gol desconhecido.");
                break;
        }
    }
}