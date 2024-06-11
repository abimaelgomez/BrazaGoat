package com.br.BrazaGoat.sorteio.service;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.sorteio.entities.SorteioModel;
import com.br.BrazaGoat.jogador.repositories.JogadorRepository;
import com.br.BrazaGoat.sorteio.repositories.SorteioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SorteioEquipeService {

    @Autowired
    private JogadorRepository jogadorRepository;

    @Autowired
    private SorteioRepository sorteioRepository;

    private final int quantidadeGoleiros = 1; // Quantidade de goleiros por equipe
    private final int quantidadeJogadoresLinha = 2; // Quantidade de jogadores de linha por equipe

    public void sortearEquipes() {
        try {
            // Buscar o último sorteio realizado
            SorteioModel ultimoSorteio = sorteioRepository.findTopByOrderByIdSorteioDesc();

            // Atualizar apenas se houver um sorteio anterior
            if (ultimoSorteio != null) {
                // Limpar os dados do último sorteio
                ultimoSorteio.getEquipeA().clear();
                ultimoSorteio.getEquipeB().clear();
                ultimoSorteio.getJogadoresReserva().clear();

                // Sortear novamente as equipes
                sortearNovaEquipe(ultimoSorteio);

                // Salvar as atualizações no banco de dados
                sorteioRepository.save(ultimoSorteio);
            } else {
                // Se não houver sorteios anteriores, realizar um sorteio novo
                SorteioModel novoSorteio = new SorteioModel();
                sortearNovaEquipe(novoSorteio);
                sorteioRepository.save(novoSorteio);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao sortear equipes: " + e.getMessage());
        }
    }

    private void sortearNovaEquipe(SorteioModel sorteio) {
        try {
            // Busca somente por jogadores ativos
            List<JogadorModel> jogadoresAtivos = jogadorRepository.findByStatus(true);

            // Embaralhar a lista de jogadores ativos
            Collections.shuffle(jogadoresAtivos);

            // Separar goleiros e jogadores de linha
            List<JogadorModel> goleiros = jogadoresAtivos.stream()
                    .filter(jogador -> jogador.getPosicao().equalsIgnoreCase("goleiro"))
                    .collect(Collectors.toList());

            List<JogadorModel> jogadoresDeLinha = jogadoresAtivos.stream()
                    .filter(jogador -> !jogador.getPosicao().equalsIgnoreCase("goleiro"))
                    .collect(Collectors.toList());

            // Inicializar as listas de jogadores para cada equipe
            List<JogadorModel> equipeA = new ArrayList<>();
            List<JogadorModel> equipeB = new ArrayList<>();
            List<JogadorModel> reservas = new ArrayList<>();

            // Usado para garantir que um jogador não seja adicionado em ambas as equipes
            Set<String> nomeUsados = new HashSet<>();
            Set<UUID> idsUsados = new HashSet<>();

            // Garantir que cada equipe tenha exatamente um goleiro
            if (goleiros.size() >= 2) {
                // Sorteio de goleiros para cada equipe
                Collections.shuffle(goleiros); // Embaralha a lista de goleiros
                JogadorModel goleiroA = goleiros.get(0);
                JogadorModel goleiroB = goleiros.get(1);

                if (!idsUsados.contains(goleiroA.getIdJogador()) && !idsUsados.contains(goleiroB.getIdJogador())) {
                    equipeA.add(goleiroA); // Adiciona o primeiro goleiro sorteado à equipe A
                    goleiroA.escalarJogador(); // Define o status do goleiro da equipe A como "escalado"
                    equipeB.add(goleiroB); // Adiciona o segundo goleiro sorteado à equipe B
                    goleiroB.escalarJogador(); // Define o status do goleiro da equipe B como "escalado"
                    idsUsados.add(goleiroA.getIdJogador()); // Adiciona o primeiro goleiro à lista de IDs usados
                    idsUsados.add(goleiroB.getIdJogador()); // Adiciona o segundo goleiro à lista de IDs usados
                }
                // Definir status de reserva para goleiros restantes, se houver
                for (int i = 2; i < goleiros.size(); i++) {
                    goleiros.get(i).reservarJogador();
                    reservas.add(goleiros.get(i));
                }
            } else {
                // Adicionar jogadores de linha como goleiros se não houver goleiros cadastrados ou não houver goleiros suficientes
                if (!jogadoresDeLinha.isEmpty()) {
                    JogadorModel jogadorA = jogadoresDeLinha.remove(0);
                    jogadorA.setPosicao("goleiro");
                    jogadorA.escalarJogador();
                    equipeA.add(jogadorA);
                    nomeUsados.add(jogadorA.getNome());
                }
                if (!jogadoresDeLinha.isEmpty()) {
                    JogadorModel jogadorB = jogadoresDeLinha.remove(0);
                    jogadorB.setPosicao("goleiro");
                    jogadorB.escalarJogador();
                    equipeB.add(jogadorB);
                    nomeUsados.add(jogadorB.getNome());
                }
            }

            // Distribuir os jogadores de linha nas equipes
            for (JogadorModel jogador : jogadoresDeLinha) {
                if (equipeA.size() < (quantidadeGoleiros + quantidadeJogadoresLinha) && !nomeUsados.contains(jogador.getNome())) {
                    equipeA.add(jogador);
                    nomeUsados.add(jogador.getNome());
                    jogador.escalarJogador();
                } else if (equipeB.size() < (quantidadeGoleiros + quantidadeJogadoresLinha) && !nomeUsados.contains(jogador.getNome())) {
                    equipeB.add(jogador);
                    nomeUsados.add(jogador.getNome());
                    jogador.escalarJogador();
                } else {
                    jogador.reservarJogador(); // Define o status dos jogadores que não foram escalados como "reserva"
                    reservas.add(jogador); // Adiciona jogador à lista de reservas
                }
            }

            // Atualizar os dados do sorteio
            sorteio.setDataSorteio(LocalDateTime.now());
            sorteio.setEquipeA(equipeA);
            sorteio.setEquipeB(equipeB);
            sorteio.setJogadoresReserva(reservas);

            // Exibir as equipes formadas
            System.out.println("_________________________________________");
            System.out.println("# UUID PARTIDA:" + sorteio.getIdSorteio() + " #");
            System.out.println("     ##### EQUIPES SORTEADAS #####");
            System.out.println("- - - - - - - - - - - - - - - - - - - - -");
            System.out.println("- EQUIPE A:");
            for (JogadorModel jogador : equipeA) {
                System.out.println(jogador.getInformacaoJogador() + " #STATUS: " + jogador.getStatusJogadorPartida());
            }
            System.out.println("- - - - - - - - - - - - - - - - - - - - -");
            System.out.println("- EQUIPE B:");
            for (JogadorModel jogador : equipeB) {
                System.out.println(jogador.getInformacaoJogador() + " #STATUS: " + jogador.getStatusJogadorPartida());
            }
            System.out.println("- - - - - - - - - - - - - - - - - - - - -");
            System.out.println(" # RESERVAS #");
            for (JogadorModel jogador : reservas) {
                System.out.println(jogador.getInformacaoJogador() + " #STATUS: " + jogador.getStatusJogadorPartida());
            }
            System.out.println("_________________________________________");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao sortear nova equipe: " + e.getMessage());
        }
    }
}