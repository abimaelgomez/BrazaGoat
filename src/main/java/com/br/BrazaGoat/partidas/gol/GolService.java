package com.br.BrazaGoat.partidas.gol;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.partidas.partida.PartidaModel;
import com.br.BrazaGoat.partidas.partida.PartidaRepository;
import com.br.BrazaGoat.partidas.partida.StatusPartida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class GolService {

    @Autowired
    private GolRepository golRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    public void adicionarGol(UUID idJogador, TipoDeGol tipoDeGol, boolean golContra) {
        // Buscar a última partida em andamento
        PartidaModel partidaEmAndamento = partidaRepository.findTopByOrderByIdPartidaDesc();

        if (partidaEmAndamento == null || partidaEmAndamento.getStatusPartida() != StatusPartida.PARTIDA_EM_ANDAMENTO) {
            throw new RuntimeException("Nenhuma partida em andamento encontrada.");
        }

        // Buscar o jogador pelo ID
        JogadorModel jogador = buscarJogadorPorId(idJogador, partidaEmAndamento);

        if (jogador == null) {
            throw new RuntimeException("Jogador não encontrado na partida.");
        }

        // Criar um novo gol
        GolModel gol = new GolModel();
        gol.setJogador(jogador);
        gol.setPartida(partidaEmAndamento);
        gol.setTipoDeGol(tipoDeGol);
        gol.setDataDoGol(LocalDate.now());
        gol.setHoraDoGol(LocalTime.now());
        gol.validarGol();

        // Atualizar o placar da partida
        if (tipoDeGol == TipoDeGol.GOLEIRO) {
            gol.setPontoPorGol(1); // Pode ajustar conforme regras específicas
            // Atualiza apenas o placar do time adversário
            if (partidaEmAndamento.getEquipeA().contains(jogador)) {
                partidaEmAndamento.setPlacarEquipeB(partidaEmAndamento.getPlacarEquipeB() + 1);
            } else if (partidaEmAndamento.getEquipeB().contains(jogador)) {
                partidaEmAndamento.setPlacarEquipeA(partidaEmAndamento.getPlacarEquipeA() + 1);
            }
        } else {
            if (golContra) {
                if (partidaEmAndamento.getEquipeA().contains(jogador)) {
                    partidaEmAndamento.setPlacarEquipeB(partidaEmAndamento.getPlacarEquipeB() + 1);
                    gol.setPontoPorGolContra(1);
                } else if (partidaEmAndamento.getEquipeB().contains(jogador)) {
                    partidaEmAndamento.setPlacarEquipeA(partidaEmAndamento.getPlacarEquipeA() + 1);
                    gol.setPontoPorGolContra(1);
                }
            } else {
                if (partidaEmAndamento.getEquipeA().contains(jogador)) {
                    partidaEmAndamento.setPlacarEquipeA(partidaEmAndamento.getPlacarEquipeA() + 1);
                    gol.setPontoPorGol(1);
                } else if (partidaEmAndamento.getEquipeB().contains(jogador)) {
                    partidaEmAndamento.setPlacarEquipeB(partidaEmAndamento.getPlacarEquipeB() + 1);
                    gol.setPontoPorGol(1);
                }
            }
        }

        // Salvar o gol e a partida atualizada
        golRepository.save(gol);
        partidaRepository.save(partidaEmAndamento);

        // Imprimir o placar atualizado no console
        System.out.println("Placar atualizado: Equipe A - " + partidaEmAndamento.getPlacarEquipeA() + " x " + partidaEmAndamento.getPlacarEquipeB() + " - Equipe B");
    }

    private JogadorModel buscarJogadorPorId(UUID idJogador, PartidaModel partida) {
        for (JogadorModel jogador : partida.getEquipeA()) {
            if (jogador.getIdJogador().equals(idJogador)) {
                return jogador;
            }
        }
        for (JogadorModel jogador : partida.getEquipeB()) {
            if (jogador.getIdJogador().equals(idJogador)) {
                return jogador;
            }
        }
        for (JogadorModel jogador : partida.getReservas()) {
            if (jogador.getIdJogador().equals(idJogador)) {
                return jogador;
            }
        }
        return null;
    }

    public void validarGol(GolModel gol) {
        Optional<GolModel> optionalGol = golRepository.findById(gol.getId());
        if (optionalGol.isPresent()) {
            GolModel golExistente = optionalGol.get();
            golExistente.setStatusGol(StatusGol.VALIDADO);  // Altera o status para VALIDADO
            golRepository.save(golExistente);  // Salva o gol com o novo status no repositório
        } else {
            gol.setStatusGol(StatusGol.INVALIDADO);  // Define como INVALIDADO se não encontrado
        }
    }

    public void invalidarGol(GolModel gol) {
        gol.setStatusGol(StatusGol.INVALIDADO);
        golRepository.save(gol);
    }

    public void revisarGol(GolModel gol) {
        gol.setStatusGol(StatusGol.EM_REVISAO);
        golRepository.save(gol);
    }

    public void anularGol(GolModel gol) {
        gol.setStatusGol(StatusGol.ANULADO);
        golRepository.save(gol);
    }

}
