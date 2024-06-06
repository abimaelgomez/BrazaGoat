package com.br.BrazaGoat.partidas.partida;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRepository extends JpaRepository<PartidaModel, Long> {

    @Query("SELECT MAX(p.numeroDaPartida) FROM PartidaModel p")
    Integer findMaxNumeroDaPartida();
}
