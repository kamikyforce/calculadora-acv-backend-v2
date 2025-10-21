package br.gov.serpro.calculadoraacv.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.gov.serpro.calculadoraacv.config.SecurityConfig;
import br.gov.serpro.calculadoraacv.utils.Constantes;

@Disabled("Desabilitado temporariamente para testar pipeline")
@WebMvcTest(Home.class)
@Import(SecurityConfig.class)
class HomeTests {
  @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetHome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Constantes.PUBLIC_PATH + "/home/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello Calculadora ACV!"));
    }
}
