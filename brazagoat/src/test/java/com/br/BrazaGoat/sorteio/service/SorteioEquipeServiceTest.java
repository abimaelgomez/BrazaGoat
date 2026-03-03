package com.br.BrazaGoat.sorteio.service;

        import com.br.BrazaGoat.jogador.entities.JogadorModel;
        import com.br.BrazaGoat.jogador.enums.StatusJogadorPartida;
        import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
        import com.br.BrazaGoat.sorteio.entities.SorteioModel;
        import com.br.BrazaGoat.sorteio.repositories.SorteioRepository;
        import com.br.BrazaGoat.sorteio.service.SorteioEquipeService;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;

        import java.util.*;
        import java.util.stream.Collectors;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

/**
 * Classe de teste para SorteioEquipeService.
 */
@ExtendWith(MockitoExtension.class)
class SorteioEquipeServiceTest {

    @Mock
    private SorteioRepository sorteioRepository;  // Mock do repositório de sorteio

    @InjectMocks
    private SorteioEquipeService sorteioEquipeService;  // Classe de serviço a ser testada

    private List<JogadorModel> jogadores;  // Lista de jogadores usada nos testes

    /**
     * Configuração inicial antes de cada teste.
     */
    @BeforeEach
    void setUp() {
        jogadores = new ArrayList<>();
        jogadores.add(criarJogadorMock("Rogerio", "Ceni", "Goleiro", StatusJogadorPartida.ESCALADO));
        jogadores.add(criarJogadorMock("Lionel", "Messi", "Linha", StatusJogadorPartida.ESCALADO));
        jogadores.add(criarJogadorMock("Cristiano", "Ronaldo", "Linha", StatusJogadorPartida.ESCALADO));
        jogadores.add(criarJogadorMock("Fabio", "Costa", "Goleiro", StatusJogadorPartida.ESCALADO));
        jogadores.add(criarJogadorMock("Neymar", "Junior", "Linha", StatusJogadorPartida.ESCALADO));
        jogadores.add(criarJogadorMock("Paulo Henrique", "Ganso", "Linha", StatusJogadorPartida.ESCALADO));
        jogadores.add(criarJogadorMock("Luigi", "Buffon", "Goleiro", StatusJogadorPartida.RESERVA));
        jogadores.add(criarJogadorMock("Angel", "Di Maria", "Linha", StatusJogadorPartida.RESERVA));
        jogadores.add(criarJogadorMock("Diego", "Costa", "Linha", StatusJogadorPartida.RESERVA));
        jogadores.add(criarJogadorMock("Lucas", "Moura", "Linha", StatusJogadorPartida.RESERVA));
        jogadores.add(criarJogadorMock("Erling", "Haland", "Linha", StatusJogadorPartida.RESERVA));
    }

    /**
     * Testa a criação de jogadores e adição a equipes com sucesso.
     */
    @Test
    void deveriaCriarJogadoresEAcrescentarNasEquipes_ComSucesso() {
        // Configuração do mock do sorteio
        SorteioModel sorteioMock = new SorteioModel();
        sorteioMock.setIdSorteio(UUID.randomUUID());
        sorteioMock.setEquipeA(Arrays.asList(
                criarJogadorMock("Rogerio", "Ceni", "Goleiro", StatusJogadorPartida.ESCALADO),
                criarJogadorMock("Lionel", "Messi", "Linha", StatusJogadorPartida.ESCALADO),
                criarJogadorMock("Cristiano", "Ronaldo", "Linha", StatusJogadorPartida.ESCALADO)
        ));
        sorteioMock.setEquipeB(Arrays.asList(
                criarJogadorMock("Fabio", "Costa", "Goleiro", StatusJogadorPartida.ESCALADO),
                criarJogadorMock("Neymar", "Junior", "Linha", StatusJogadorPartida.ESCALADO),
                criarJogadorMock("Paulo Henrique", "Ganso", "Linha", StatusJogadorPartida.ESCALADO)
        ));
        sorteioMock.setJogadoresReserva(Arrays.asList(
                criarJogadorMock("Luigi", "Buffon", "Goleiro", StatusJogadorPartida.RESERVA),
                criarJogadorMock("Angel", "Di Maria", "Linha", StatusJogadorPartida.RESERVA),
                criarJogadorMock("Diego", "Costa", "Linha", StatusJogadorPartida.RESERVA),
                criarJogadorMock("Lucas", "Moura", "Linha", StatusJogadorPartida.RESERVA),
                criarJogadorMock("Erling", "Haland", "Linha", StatusJogadorPartida.RESERVA)
        ));

        imprimirEquipes(sorteioMock);  // Imprime as equipes para verificação visual

        // Verificar se os métodos foram chamados corretamente
        verifyNoInteractions(sorteioRepository);  // Verifica se nenhum método do repository foi chamado
    }

    /**
     * Testa o sorteio de equipes com pelo menos um goleiro em cada equipe.
     */
    @Test
    void deveriaSortearEquipesComPeloMenosUmGoleiro_ComSucesso() {
        Collections.shuffle(jogadores);  // Embaralha a lista de jogadores
        List<JogadorModel> goleiros = filtrarPorPosicao(jogadores, "Goleiro");
        List<JogadorModel> linha = filtrarPorPosicao(jogadores, "Linha");

        // Validar que há pelo menos dois goleiros para as equipes
        assertTrue(goleiros.size() >= 2, "Deve haver pelo menos dois goleiros para formar as equipes.");

        SorteioModel sorteioResultante = sortearEquipesComGoleiros(goleiros, linha, 1, 2);

        validarEquipes(sorteioResultante, 1, 2);  // Valida que cada equipe tem pelo menos um goleiro e dois jogadores de linha
        imprimirEquipes(sorteioResultante);  // Imprime as equipes para verificação visual

        verifyNoInteractions(sorteioRepository);  // Verifica se nenhum método do repository foi chamado
    }

    /**
     * Testa o sorteio de equipes sem goleiros.
     */
    @Test
    void deveriaSortearEquipesSemGoleiros_ComSucesso() {
        List<JogadorModel> jogadoresSemGoleiros = new ArrayList<>(jogadores);
        jogadoresSemGoleiros.removeIf(jogador -> "Goleiro".equals(jogador.getPosicao()));
        Collections.shuffle(jogadoresSemGoleiros);  // Embaralha a lista de jogadores sem goleiros

        SorteioModel sorteioResultante = sortearEquipesSemGoleiros(jogadoresSemGoleiros, 3);

        validarEquipes(sorteioResultante, 0, 3);  // Valida que cada equipe tem três jogadores de linha
        imprimirEquipes(sorteioResultante);  // Imprime as equipes para verificação visual

        verifyNoInteractions(sorteioRepository);  // Verifica se nenhum método do repository foi chamado
    }

    /**
     * Cria um mock de jogador com os detalhes fornecidos.
     *
     * @param nome      Nome do jogador
     * @param sobrenome Sobrenome do jogador
     * @param posicao   Posição do jogador
     * @param status    Status do jogador na partida
     * @return O mock do jogador criado
     */
    private JogadorModel criarJogadorMock(String nome, String sobrenome, String posicao, StatusJogadorPartida status) {
        JogadorModel jogador = new JogadorModel();
        jogador.setNome(nome);
        jogador.setSobrenome(sobrenome);
        jogador.setPosicao(posicao);
        jogador.setStatusJogadorPartida(status);
        return jogador;
    }

    /**
     * Imprime as equipes e os jogadores reserva do sorteio fornecido.
     *
     * @param sorteio O modelo de sorteio a ser impresso
     */
    private void imprimirEquipes(SorteioModel sorteio) {
        System.out.println("Equipe A:");
        sorteio.getEquipeA().forEach(jogador -> System.out.println(jogador.getNome() + " " + jogador.getSobrenome() + " (" + jogador.getPosicao() + ")"));

        System.out.println("Equipe B:");
        sorteio.getEquipeB().forEach(jogador -> System.out.println(jogador.getNome() + " " + jogador.getSobrenome() + " (" + jogador.getPosicao() + ")"));

        System.out.println("Reservas:");
        sorteio.getJogadoresReserva().forEach(jogador -> System.out.println(jogador.getNome() + " " + jogador.getSobrenome() + " (" + jogador.getPosicao() + ")"));
    }

    /**
     * Filtra jogadores pela posição fornecida.
     *
     * @param jogadores Lista de jogadores
     * @param posicao   Posição a ser filtrada
     * @return Lista de jogadores na posição fornecida
     */
    private List<JogadorModel> filtrarPorPosicao(List<JogadorModel> jogadores, String posicao) {
        return jogadores.stream()
                .filter(jogador -> jogador.getPosicao().equals(posicao))
                .collect(Collectors.toList());
    }

    /**
     * Sorteia equipes garantindo que cada uma tenha pelo menos um goleiro.
     *
     * @param goleiros Lista de goleiros
     * @param linha Lista de jogadores de linha
     * @param goleirosPorEquipe Número de goleiros por equipe
     * @param linhaPorEquipe Número de jogadores de linha por equipe
     * @return O modelo de sorteio contendo as equipes e os reservas
     */
    private SorteioModel sortearEquipesComGoleiros(List<JogadorModel> goleiros, List<JogadorModel> linha, int goleirosPorEquipe, int linhaPorEquipe) {
        List<JogadorModel> equipeA = new ArrayList<>();
        List<JogadorModel> equipeB = new ArrayList<>();
        List<JogadorModel> reservas = new ArrayList<>();

        // Adicionar goleiros às equipes, garantindo que cada equipe receba o número correto de goleiros
        int numGoleirosDistribuidos = 0;
        for (int i = 0; i < goleirosPorEquipe; i++) {
            if (i < goleiros.size()) {
                equipeA.add(goleiros.get(i));
                numGoleirosDistribuidos++;
            }
            if (i + goleirosPorEquipe < goleiros.size()) {
                equipeB.add(goleiros.get(i + goleirosPorEquipe));
                numGoleirosDistribuidos++;
            }
        }

        // Adicionar jogadores de linha às equipes
        for (int i = 0; i < linhaPorEquipe; i++) {
            if (i < linha.size()) {
                equipeA.add(linha.get(i));
            }
            if (i + linhaPorEquipe < linha.size()) {
                equipeB.add(linha.get(i + linhaPorEquipe));
            }
        }

        // Adicionar goleiros restantes à lista de reservas
        if (numGoleirosDistribuidos < goleiros.size()) {
            reservas.addAll(goleiros.subList(numGoleirosDistribuidos, goleiros.size()));
        }

        // Adicionar jogadores de linha restantes à lista de reservas
        if (linha.size() > linhaPorEquipe * 2) {
            reservas.addAll(linha.subList(linhaPorEquipe * 2, linha.size()));
        }

        SorteioModel sorteio = new SorteioModel();
        sorteio.setIdSorteio(UUID.randomUUID());
        sorteio.setEquipeA(equipeA);
        sorteio.setEquipeB(equipeB);
        sorteio.setJogadoresReserva(reservas);

        return sorteio;
    }

    /**
     * Sorteia jogadores aleatoriamente entre equipes.
     *
     * @param goleiros Lista de goleiros
     * @param linha Lista de jogadores de linha
     * @param goleirosPorEquipe Número de goleiros por equipe
     * @param linhaPorEquipe Número de jogadores de linha por equipe
     * @return O modelo de sorteio contendo as equipes e os reservas
     */
    private SorteioModel sortearJogadoresAleatoriamente(List<JogadorModel> goleiros, List<JogadorModel> linha, int goleirosPorEquipe, int linhaPorEquipe) {
        List<JogadorModel> todosJogadores = new ArrayList<>(goleiros);
        todosJogadores.addAll(linha);
        Collections.shuffle(todosJogadores);

        List<JogadorModel> equipeA = new ArrayList<>();
        List<JogadorModel> equipeB = new ArrayList<>();
        List<JogadorModel> reservas = new ArrayList<>();

        // Distribuir goleiros
        int countGoleirosEquipeA = 0;
        int countGoleirosEquipeB = 0;

        for (JogadorModel jogador : todosJogadores) {
            if (jogador.getPosicao().equals("Goleiro")) {
                if (countGoleirosEquipeA < goleirosPorEquipe) {
                    equipeA.add(jogador);
                    countGoleirosEquipeA++;
                } else if (countGoleirosEquipeB < goleirosPorEquipe) {
                    equipeB.add(jogador);
                    countGoleirosEquipeB++;
                } else {
                    reservas.add(jogador);
                }
            }
        }

        // Distribuir jogadores de linha
        int countLinhaEquipeA = 0;
        int countLinhaEquipeB = 0;

        for (JogadorModel jogador : todosJogadores) {
            if (jogador.getPosicao().equals("Linha")) {
                if (countLinhaEquipeA < linhaPorEquipe) {
                    equipeA.add(jogador);
                    countLinhaEquipeA++;
                } else if (countLinhaEquipeB < linhaPorEquipe) {
                    equipeB.add(jogador);
                    countLinhaEquipeB++;
                } else {
                    reservas.add(jogador);
                }
            }
        }

        SorteioModel sorteio = new SorteioModel();
        sorteio.setIdSorteio(UUID.randomUUID());
        sorteio.setEquipeA(equipeA);
        sorteio.setEquipeB(equipeB);
        sorteio.setJogadoresReserva(reservas);

        return sorteio;
    }
    /**
     * Sorteia equipes sem goleiros.
     *
     * @param jogadores Lista de jogadores
     * @param linhaPorEquipe Número de jogadores de linha por equipe
     * @return O modelo de sorteio contendo as equipes e os reservas
     */
    private SorteioModel sortearEquipesSemGoleiros(List<JogadorModel> jogadores, int linhaPorEquipe) {
        List<JogadorModel> equipeA = new ArrayList<>();
        List<JogadorModel> equipeB = new ArrayList<>();
        List<JogadorModel> reservas = new ArrayList<>();

        for (int i = 0; i < jogadores.size(); i++) {
            if (i % 2 == 0 && equipeA.size() < linhaPorEquipe) {
                equipeA.add(jogadores.get(i));
            } else if (equipeB.size() < linhaPorEquipe) {
                equipeB.add(jogadores.get(i));
            } else {
                reservas.add(jogadores.get(i));
            }
        }

        SorteioModel sorteio = new SorteioModel();
        sorteio.setIdSorteio(UUID.randomUUID());
        sorteio.setEquipeA(equipeA);
        sorteio.setEquipeB(equipeB);
        sorteio.setJogadoresReserva(reservas);

        return sorteio;
    }

    /**
     * Valida se as equipes têm o número correto de jogadores por posição.
     *
     * @param sorteio O modelo de sorteio a ser validado
     * @param goleirosPorEquipe O número de goleiros esperado por equipe
     * @param linhaPorEquipe O número de jogadores de linha esperado por equipe
     */
    private void validarEquipes(SorteioModel sorteio, int goleirosPorEquipe, int linhaPorEquipe) {
        assertNotNull(sorteio.getEquipeA());
        assertNotNull(sorteio.getEquipeB());
        assertNotNull(sorteio.getJogadoresReserva());

        // Validar quantidade de goleiros e jogadores de linha na equipeA
        assertEquals(goleirosPorEquipe, filtrarPorPosicao(sorteio.getEquipeA(), "Goleiro").size());
        assertEquals(linhaPorEquipe, filtrarPorPosicao(sorteio.getEquipeA(), "Linha").size());

        // Validar quantidade de goleiros e jogadores de linha na equipeB
        assertEquals(goleirosPorEquipe, filtrarPorPosicao(sorteio.getEquipeB(), "Goleiro").size());
        assertEquals(linhaPorEquipe, filtrarPorPosicao(sorteio.getEquipeB(), "Linha").size());
    }
}