package com.br.BrazaGoat.partidas.assistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistenciaRepository extends JpaRepository <AssistenciaModel, Long> {
}
