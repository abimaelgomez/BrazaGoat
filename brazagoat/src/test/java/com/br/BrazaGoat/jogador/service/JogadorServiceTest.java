package com.br.BrazaGoat.jogador.service;

import com.br.BrazaGoat.jogador.dtos.JogadorRecordDto;
import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JogadorServiceTest {

    @InjectMocks
    JogadorService jogadorService;

    @Mock
    JogadorRepository jogadorRepository;

    @Test
    void deveriaCadastrarJogadorComSucesso() {
        //configurar dados de teste
        JogadorRecordDto jogadorRecordDto = new JogadorRecordDto("Alberto", "Freire", "Linha", 18, 10, true);

        JogadorModel jogadorModelEsperado = new JogadorModel();
        BeanUtils.copyProperties(jogadorRecordDto, jogadorModelEsperado);
        jogadorModelEsperado.setStatus(true);

        //configurar mock para retornar o objeto esperado ao salvar
        when(jogadorRepository.save(any(JogadorModel.class))).thenReturn(jogadorModelEsperado);

        // Chamar o método a ser testado
        JogadorModel jogadorModelRetornado = jogadorService.cadastrarJogador(jogadorRecordDto);

        // Verificar os resultados
        assertNotNull(jogadorModelRetornado);
        assertEquals("Alberto", jogadorModelRetornado.getNome());
        assertEquals("Freire", jogadorModelRetornado.getSobrenome());
        assertEquals("Linha", jogadorModelRetornado.getPosicao());
        assertEquals(18, jogadorModelRetornado.getIdade());
        assertEquals(10, jogadorModelRetornado.getNumeroCamisa());
        assertTrue(jogadorModelRetornado.isStatus());
    }

    @Test
    void deveriaBuscarTodosJogadores() {
        // Configurar dados de teste
        JogadorRecordDto jogadorDto1 = new JogadorRecordDto("Lionel", "Messi", "Linha", 36, 10, true);
        JogadorRecordDto jogadorDto2 = new JogadorRecordDto("Cristiano", "Ronaldo", "Linha", 39, 7, true);

        // Criar modelos correspondentes a partir dos DTOs
        JogadorModel jogador1 = new JogadorModel();
        BeanUtils.copyProperties(jogadorDto1, jogador1);
        JogadorModel jogador2 = new JogadorModel();
        BeanUtils.copyProperties(jogadorDto2, jogador2);

        List<JogadorModel> listaDeJogadoresEsperada = Arrays.asList(jogador1, jogador2);

        // Configurar mock para retornar a lista de jogadores
        when(jogadorRepository.findAll()).thenReturn(listaDeJogadoresEsperada);

        // Chamar o método a ser testado
        List<JogadorModel> resultado = jogadorService.buscarTodosJogadores();

        // Converter os modelos retornados para DTOs para verificação
        List<JogadorRecordDto> resultadoDtos = resultado.stream().map(jogadorModel -> {
            JogadorRecordDto dto = new JogadorRecordDto(
                    jogadorModel.getNome(),
                    jogadorModel.getSobrenome(),
                    jogadorModel.getPosicao(),
                    jogadorModel.getIdade(),
                    jogadorModel.getNumeroCamisa(),
                    jogadorModel.isStatus()
            );
            return dto;
        })
        //Agrega os elementos transformados do Stream em uma nova lista (List<JogadorRecordDto>).
        .collect(Collectors.toList());


        // Verificar os resultados
        assertNotNull(resultadoDtos, "A lista de jogadores não deve ser nula.");
        //jogador1
        assertEquals(2, resultadoDtos.size(), "A lista de jogadores deve conter 2 elementos'jogadores'.");
        assertEquals("Lionel", resultadoDtos.get(0).nome(), "O nome do primeiro jogador deve ser 'Lionel'.");
        assertEquals("Messi", resultadoDtos.get(0).sobrenome(), "O sobrenome do primeiro jogador deve ser 'Messi'.");
        assertEquals("Linha", resultadoDtos.get(0).posicao(), "A posição do jogador 1 deve ser 'Linha'.");
        assertEquals(36, resultadoDtos.get(0).idade(), "A idade do primeiro jogador deve ser 36.");
        assertEquals(10, resultadoDtos.get(0).numeroCamisa(), "O numero da camisa do jogador 1 deve ser 10.");
        //jogador2
        assertEquals("Cristiano", resultadoDtos.get(1).nome(), "O nome do segundo jogador deve ser 'Cristiano'.");
        assertEquals("Ronaldo", resultadoDtos.get(1).sobrenome(), "O sobrenome do primeiro jogador deve ser 'Ronaldo'.");
        assertEquals("Linha", resultadoDtos.get(1).posicao(), "A posição do jogador 1 deve ser 'Linha'.");
        assertEquals(39, resultadoDtos.get(1).idade(), "A idade do segundo jogador deve ser 39.");
        assertEquals(7, resultadoDtos.get(1).numeroCamisa(), "A idade do segundo jogador deve ser 7.");
    }


    @Test
    void deveriaBuscarUmJogadorPeloUUID() {
        // Configurar dados de teste
        UUID idJogador = UUID.randomUUID();
        JogadorModel jogador = new JogadorModel();
        jogador.setIdJogador(idJogador);
        jogador.setNome("José");
        jogador.setSobrenome("Pereira");
        jogador.setPosicao("Linha");
        jogador.setIdade(18);
        jogador.setNumeroCamisa(15);
        jogador.setStatus(true);

        // Configurar mock para retornar o jogador
        when(jogadorRepository.findById(idJogador)).thenReturn(Optional.of(jogador));

        // Chamar o método a ser testado
        Optional<JogadorModel> resultado = jogadorService.buscarJogador(idJogador);

        // Verificar os resultados
        assertTrue(resultado.isPresent(), "O jogador deve existir.");
        assertEquals(idJogador, resultado.get().getIdJogador(), "O ID do jogador deve ser igual ao ID fornecido.");
        assertEquals("José", resultado.get().getNome(), "O nome do jogador deve ser 'José'.");
    }

    @Test
    void deveriaAtualizarOsAtributosDoJogador() {
        // Configurar dados de teste
        UUID idJogador = UUID.randomUUID();
        JogadorRecordDto jogadorDto = new JogadorRecordDto("Pedro", "Silva", "Linha", 19, 10, true);
        JogadorModel jogadorExistente = new JogadorModel();
        jogadorExistente.setIdJogador(idJogador);
        jogadorExistente.setNome("José");
        jogadorExistente.setSobrenome("Pereira");
        jogadorExistente.setPosicao("Linha");
        jogadorExistente.setIdade(18);
        jogadorExistente.setNumeroCamisa(15);
        jogadorExistente.setStatus(true);

        // Configurar mock para retornar o jogador existente
        when(jogadorRepository.findById(idJogador)).thenReturn(Optional.of(jogadorExistente));
        // Configurar mock para salvar o jogador atualizado
        when(jogadorRepository.save(any(JogadorModel.class))).thenAnswer(invocation -> {
            JogadorModel jogadorAtualizado = invocation.getArgument(0);
            return jogadorAtualizado;
        });

        // Chamar o método a ser testado
        Optional<JogadorModel> resultado = jogadorService.atualizarJogador(idJogador, jogadorDto);

        // Verificar os resultados
        assertTrue(resultado.isPresent(), "O jogador deve estar presente.");
        assertEquals(idJogador, resultado.get().getIdJogador(), "O ID do jogador deve ser igual ao ID fornecido.");
        assertEquals("Pedro", resultado.get().getNome(), "O nome do jogador deve ser 'Pedro'.");
        assertEquals("Silva", resultado.get().getSobrenome(), "O sobrenome do jogador deve ser 'Silva'.");
        assertEquals(19, resultado.get().getIdade(), "A idade do jogador deve ser 19.");
        assertEquals(10, resultado.get().getNumeroCamisa(), "O número da camisa do jogador deve ser 10.");
    }

    @Test
    void deveriaDeletarOJogadorSelecionadoPeloUUID() {
        // Configurar dados de teste
        UUID idJogador = UUID.randomUUID();
        JogadorModel jogador = new JogadorModel();
        jogador.setIdJogador(idJogador);
        jogador.setNome("Neymar");
        jogador.setSobrenome("Junior");
        jogador.setPosicao("Linha");
        jogador.setIdade(31);
        jogador.setNumeroCamisa(11);
        jogador.setStatus(true);

        // Configurar mock para retornar o jogador
        when(jogadorRepository.findById(idJogador)).thenReturn(Optional.of(jogador));

        // Chamar o método a ser testado
        boolean resultado = jogadorService.deletarJogador(idJogador);

        // Verificar os resultados
        assertTrue(resultado, "O jogador deve ser deletado com sucesso.");
        verify(jogadorRepository, times(1)).delete(jogador);
    }
}