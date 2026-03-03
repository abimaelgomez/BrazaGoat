package com.br.BrazaGoat.partidas.substituicao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubstituicaoRepository extends JpaRepository<SubstituicaoModel, Long> {
    List<SubstituicaoModel> findByPartidaIdPartida(Long idPartida);
}
