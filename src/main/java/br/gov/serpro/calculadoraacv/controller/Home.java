package br.gov.serpro.calculadoraacv.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.serpro.calculadoraacv.utils.Constantes;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping(Constantes.PUBLIC_PATH + "/home")
public class Home {

  @GetMapping("/")
  public String getHome() {
      log.info("Endpoint Home acessado");
      return "Hello Calculadora ACV!";
  }
  
}
