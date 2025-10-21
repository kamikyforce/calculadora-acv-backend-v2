package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.FatorEnergiaEletrica;
import br.gov.serpro.calculadoraacv.repository.FatorEnergiaEletricaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FatorEnergiaEletricaService {
    
    @Autowired
    private FatorEnergiaEletricaRepository fatorRepository;
    
    public List<FatorEnergiaEletrica> listarTodos() {
        return fatorRepository.findByAtivoTrue();
    }
    
    public Optional<FatorEnergiaEletrica> buscarPorAnoEMes(Integer ano, Integer mes) {
        return fatorRepository.findByAnoAndMesAndAtivoTrue(ano, mes);
    }
    
    public BigDecimal obterFatorEmissao(Integer ano, Integer mes) {
        return buscarPorAnoEMes(ano, mes)
                .map(FatorEnergiaEletrica::getFatorEmissao)
                .orElse(BigDecimal.ZERO);
    }
    
    public List<Integer> listarAnosDisponiveis() {
        return fatorRepository.findDistinctAnosOrderByAno();
    }
    
    // Novos métodos necessários para o controller
    public List<FatorEnergiaEletrica> listarPorAno(Integer ano) {
        return fatorRepository.findByAnoAndAtivoTrue(ano);
    }
    
    public List<FatorEnergiaEletrica> listarPorTipoDado(String tipoDado) {
        return fatorRepository.findByTipoDadoAndAtivoTrue(tipoDado);
    }
    
    public List<FatorEnergiaEletrica> listarPorAnoETipoDado(Integer ano, String tipoDado) {
        return fatorRepository.findByAnoAndTipoDadoAndAtivoTrue(ano, tipoDado);
    }
    
    public List<FatorEnergiaEletrica> listarPorPeriodo(Integer anoInicio, Integer anoFim) {
        return fatorRepository.findByAnoBetweenAndAtivoTrue(anoInicio, anoFim);
    }
    
    public FatorEnergiaEletrica salvar(FatorEnergiaEletrica fator) {
        return fatorRepository.save(fator);
    }
    
    public Optional<FatorEnergiaEletrica> buscarPorId(Long id) {
        return fatorRepository.findById(id)
                .filter(FatorEnergiaEletrica::getAtivo);
    }
    
    public void excluir(Long id) {
        fatorRepository.findById(id).ifPresent(fator -> {
            fator.setAtivo(false);
            fatorRepository.save(fator);
        });
    }
}