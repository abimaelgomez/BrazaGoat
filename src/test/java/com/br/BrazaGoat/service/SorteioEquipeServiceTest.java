package com.br.BrazaGoat.service;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import com.br.BrazaGoat.sorteio.service.SorteioEquipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SorteioEquipeServiceTest {

    @Mock
    private JogadorRepository jogadorRepository;

    @InjectMocks
    private SorteioEquipeService sorteioEquipeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveriaBuscarSomenteJogadoresAtivos() {
        // Simulação dos dados do jogador ativo 1
        JogadorModel jogadorAtivo1 = new JogadorModel();
        jogadorAtivo1.setId(1L);
        jogadorAtivo1.setNome("Jogador 1");
        jogadorAtivo1.setStatus(true);
        // Adicione mais atributos conforme necessário

        // Simulação dos dados do jogador ativo 2
        JogadorModel jogadorAtivo2 = new JogadorModel();
        jogadorAtivo2.setId(2L);
        jogadorAtivo2.setNome("Jogador 2");
        jogadorAtivo2.setStatus(true);
        // Adicione mais atributos conforme necessário

        // Mock do método findByStatus para retornar a lista de jogadores ativos
        when(jogadorRepository.findByStatus(true)).thenReturn(Arrays.asList(jogadorAtivo1, jogadorAtivo2));

        // Chame o método para buscar jogadores ativos
        List<JogadorModel> jogadoresAtivosRetornados = sorteioEquipeService.buscarJogadoresAtivos();

        // Verifique se o método findByStatus foi chamado corretamente
        verify(jogadorRepository, times(1)).findByStatus(true);

        // Verifique se a lista de jogadores retornados é a mesma que a lista mockada
        assertEquals(2, jogadoresAtivosRetornados.size());
        assertEquals("Jogador 1", jogadoresAtivosRetornados.get(0).getNome());
        assertEquals("Jogador 2", jogadoresAtivosRetornados.get(1).getNome());
    }

    @Test
    public void deveriaSepararGoleirosEJogadoresDeLinha() {
        // Mock da lista de jogadores ativos
        List<JogadorModel> jogadoresAtivosMock = new ArrayList<>();
        JogadorModel jogadorGoleiro1 = new JogadorModel(/* Dados do goleiro 1 */);
        jogadorGoleiro1.setPosicao("goleiro");
        JogadorModel jogadorGoleiro2 = new JogadorModel(/* Dados do goleiro 2 */);
        jogadorGoleiro2.setPosicao("goleiro");
        JogadorModel jogadorLinha1 = new JogadorModel(/* Dados do jogador de linha 1 */);
        jogadorLinha1.setPosicao("linha");
        JogadorModel jogadorLinha2 = new JogadorModel(/* Dados do jogador de linha 2 */);
        jogadorLinha2.setPosicao("linha");
        jogadoresAtivosMock.add(jogadorGoleiro1);
        jogadoresAtivosMock.add(jogadorGoleiro2);
        jogadoresAtivosMock.add(jogadorLinha1);
        jogadoresAtivosMock.add(jogadorLinha2);

        // Separar goleiros e jogadores de linha
        List<JogadorModel> goleiros = sorteioEquipeService.separarGoleiros(jogadoresAtivosMock);
        List<JogadorModel> jogadoresDeLinha = sorteioEquipeService.separarJogadoresDeLinha(jogadoresAtivosMock);

        // Verificar se os goleiros foram separados corretamente
        assertEquals(2, goleiros.size());
        assertTrue(goleiros.contains(jogadorGoleiro1));
        assertTrue(goleiros.contains(jogadorGoleiro2));

        // Verificar se os jogadores de linha foram separados corretamente
        assertEquals(2, jogadoresDeLinha.size());
        assertTrue(jogadoresDeLinha.contains(jogadorLinha1));
        assertTrue(jogadoresDeLinha.contains(jogadorLinha2));
    }


}
