package com.br.BrazaGoat.controller;

import com.br.BrazaGoat.model.JogadorModel;
import com.br.BrazaGoat.service.SorteioEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value ="/sorteio_de_equipes")
public class SorteioEquipeController {

    @Autowired
    private SorteioEquipeService sorteioEquipeService;

    @GetMapping("/sortear")
    public void sortearEquipes(){
        sorteioEquipeService.sortearEquipes();
    }
}
