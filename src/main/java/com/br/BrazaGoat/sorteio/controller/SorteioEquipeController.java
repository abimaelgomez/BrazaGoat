package com.br.BrazaGoat.sorteio.controller;

import com.br.BrazaGoat.sorteio.service.SorteioEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sorteio_de_equipes")
public class SorteioEquipeController {

    @Autowired
    private SorteioEquipeService sorteioEquipeService;

    // Corrigido: era @GetMapping — operação de escrita deve ser POST
    @PostMapping("/sortear")
    public ResponseEntity<String> sortearEquipes() {
        sorteioEquipeService.sortearEquipes();
        return ResponseEntity.status(HttpStatus.CREATED).body("Equipes sorteadas com sucesso!");
    }
}
