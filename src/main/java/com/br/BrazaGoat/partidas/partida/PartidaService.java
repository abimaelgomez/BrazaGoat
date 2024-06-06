package com.br.BrazaGoat.partidas.partida;

import com.br.BrazaGoat.sorteio.entities.SorteioModel;
import com.br.BrazaGoat.sorteio.repositories.SorteioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PartidaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private SorteioRepository sorteioRepository;

    public PartidaRecordDTO gerarPartida() {
        // Executar o comando para excluir a coluna id_partida, se necessário
        executarComandoExclusaoColuna();

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
                novaPartida.getHoraDoFinal()
        );
    }

    private void executarComandoExclusaoColuna() {
        try {
            String sql = "ALTER TABLE public.partidas DROP COLUMN id_partida;";
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            // Se ocorrer um erro ao executar o comando, apenas registre um aviso
            System.out.println("Aviso: Falha ao executar o comando de exclusão da coluna id_partida.");
        }
    }
}
