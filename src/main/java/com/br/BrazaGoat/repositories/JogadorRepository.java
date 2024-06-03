package com.br.BrazaGoat.repositories;

import com.br.BrazaGoat.model.entities.JogadorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JogadorRepository extends JpaRepository<JogadorModel, UUID> {

    List<JogadorModel> findByStatus(boolean status);
}
