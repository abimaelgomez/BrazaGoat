package com.br.BrazaGoat.sorteio.repositories;

import com.br.BrazaGoat.jogador.entities.JogadorModel;
import com.br.BrazaGoat.sorteio.entities.SorteioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SorteioRepository extends JpaRepository<SorteioModel, UUID> {

    SorteioModel findTopByOrderByIdSorteioDesc();
}