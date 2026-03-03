package com.br.BrazaGoat.resultado.repositories;

import com.br.BrazaGoat.resultado.entities.ResultadoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoModel, Long> {
    Optional<ResultadoModel> findByPartidaIdPartida(Long idPartida);
    List<ResultadoModel> findAllByOrderByDataResultadoDesc();
}
