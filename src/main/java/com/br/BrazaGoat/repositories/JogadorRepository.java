package com.br.BrazaGoat.repositories;

import com.br.BrazaGoat.model.JogadorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JogadorRepository extends JpaRepository<JogadorModel, UUID> {

}
