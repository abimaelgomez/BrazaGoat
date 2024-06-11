package com.br.BrazaGoat.partidas.partida;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.sorteio.entities.SorteioModel;
import com.br.BrazaGoat.sorteio.repositories.SorteioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private SorteioRepository sorteioRepository;

    public PartidaRecordDTO gerarPartida() {
        // Obtendo o último número de partida
        Integer ultimoNumeroPartida = partidaRepository.findMaxNumeroDaPartida();

        // Se não houver nenhuma partida no banco de dados, define o número da partida como 1
        int novoNumeroPartida = (ultimoNumeroPartida != null) ? ultimoNumeroPartida + 1 : 1;

        // Buscando o último sorteio realizado
        SorteioModel ultimoSorteio = sorteioRepository.findTopByOrderByIdSorteioDesc();

        if (ultimoSorteio == null) {
            throw new RuntimeException("Nenhum sorteio encontrado.");
        }

        // Criando uma nova partida com o número gerado
        PartidaModel novaPartida = new PartidaModel();
        novaPartida.setNumeroDaPartida(novoNumeroPartida);

        // Adicionando o status de gerada
        novaPartida.gerada();

        // Associando jogadores do sorteio à partida
        novaPartida.associarJogadoresDoSorteio(ultimoSorteio);

        // Listas para armazenar jogadores das equipes e reservas
        List<JogadorModel> jogadoresEquipeA = novaPartida.getEquipeA();
        List<JogadorModel> jogadoresEquipeB = novaPartida.getEquipeB();
        List<JogadorModel> jogadoresReserva = ultimoSorteio.getJogadoresReserva();

        // Salvando a nova partida no banco de dados
        novaPartida = partidaRepository.save(novaPartida);

        // Verificando se a nova partida foi salva com sucesso
        if (novaPartida.getIdPartida() == null) {
            throw new RuntimeException("Erro ao salvar a partida no banco de dados.");
        }
        // Imprimindo os detalhes da partida gerada no console
        System.out.println("______________________________________________________________");
        System.out.println("                PARTIDA Nº " + novaPartida.getNumeroDaPartida() + " GERADA               " + "| # ID: " + novaPartida.getIdPartida() + "|");
        System.out.println("                                       STATUS:" + novaPartida.getStatusPartida() + ".");
        System.out.println("-------------------------- PLACAR ----------------------------");
        System.out.println("                Equipe A - " + novaPartida.getPlacarEquipeA() + " x " + novaPartida.getPlacarEquipeB() + " - Equipe B ");
        System.out.println("--------------------------------------------------------------");

        // Imprimindo as equipes e reservas
        System.out.println("                 EQUIPES E RESERVAS                          ");
        System.out.println("--------------------------------------------------------------");
        System.out.println("EQUIPE A:");
        for (JogadorModel jogador : novaPartida.getEquipeA()) {
            System.out.println(jogador.getInformacaoJogador() + " #STATUS: " + jogador.getStatusJogadorPartida());
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println("EQUIPE B:");
        for (JogadorModel jogador : novaPartida.getEquipeB()) {
            System.out.println(jogador.getInformacaoJogador() + " #STATUS: " + jogador.getStatusJogadorPartida());
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println("RESERVAS:");
        for (JogadorModel jogador : novaPartida.getReservas()) {
            System.out.println(jogador.getInformacaoJogador() + " #STATUS: " + jogador.getStatusJogadorPartida());
        }
        System.out.println("--------------------------------------------------------------");

        // Criando um registro para representar a nova partida
        return new PartidaRecordDTO(
                novaPartida.getIdPartida(),
                novaPartida.getNumeroDaPartida(),
                novaPartida.getStatusPartida(),
                novaPartida.getPlacarEquipeA(),
                novaPartida.getPlacarEquipeB(),
                novaPartida.isPartidaIniciada(),
                novaPartida.isPartidaFinalizada(),
                novaPartida.getTempoMaxDuracao(),
                novaPartida.getTempoMaxDuracaoAcrescismo(),
                novaPartida.getTempoDePartida(),
                novaPartida.getTempoDeAcrescimo(),
                novaPartida.getDataInicio(),
                novaPartida.getDataFinal(),
                novaPartida.getHoraDoInicio(),
                novaPartida.getHoraDoFinal(),
                jogadoresEquipeA, // Adiciona jogadores da equipe A
                jogadoresEquipeB, // Adiciona jogadores da equipe B
                jogadoresReserva // Adiciona jogadores reservas
        );
    }

    // Método para confirmar a escalação da última partida gerada
    public void confirmarEscalacao() {
        // Buscar a última partida gerada
        PartidaModel ultimaPartidaGerada = partidaRepository.findTopByOrderByIdPartidaDesc();

        if (ultimaPartidaGerada == null) {
            throw new RuntimeException("Nenhuma partida gerada encontrada.");
        }
        // Mudar o status da partida para Aguardando Início
        ultimaPartidaGerada.aguardarInicio();
        // Salvar a partida com o novo status
        partidaRepository.save(ultimaPartidaGerada);
        System.out.println(" # AGUARDANDO INICIO DA PARTIDA ! ");
    }
}
