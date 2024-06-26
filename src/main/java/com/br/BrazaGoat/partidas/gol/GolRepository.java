package com.br.BrazaGoat.partidas.gol;

import com.br.BrazaGoat.partidas.partida.PartidaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GolRepository extends JpaRepository<GolModel, Long> {
    List<GolModel> findByPartida(PartidaModel partida);
}
