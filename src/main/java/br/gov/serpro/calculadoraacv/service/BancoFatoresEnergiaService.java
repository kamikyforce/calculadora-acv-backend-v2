package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.BancoFatoresEnergia;
import br.gov.serpro.calculadoraacv.repository.BancoFatoresEnergiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BancoFatoresEnergiaService {
    
    @Autowired
    private BancoFatoresEnergiaRepository bancoRepository;
    
    public List<BancoFatoresEnergia> listarTodos() {
        return bancoRepository.findByAtivoTrueOrderByAno();
    }
    
    public Optional<BancoFatoresEnergia> buscarPorAno(Integer ano) {
        return bancoRepository.findByAnoAndAtivoTrue(ano);
    }
    
    public BigDecimal obterFatorMedioAnual(Integer ano) {
        return buscarPorAno(ano)
                .map(BancoFatoresEnergia::getFatorMedioAnual)
                .orElse(BigDecimal.ZERO);
    }
    
    public List<BancoFatoresEnergia> listarComFatorMedioAnual() {
        return bancoRepository.findByFatorMedioAnualNotNullAndAtivoTrue();
    }
    
    public List<Integer> listarAnosDisponiveis() {
        return bancoRepository.findDistinctAnosDisponiveis();
    }
    
    public BancoFatoresEnergia salvar(BancoFatoresEnergia bancoFator) {
        return bancoRepository.save(bancoFator);
    }
    
    public Optional<BancoFatoresEnergia> buscarPorId(Long id) {
        return bancoRepository.findById(id)
                .filter(BancoFatoresEnergia::getAtivo);
    }
    
    public void excluir(Long id) {
        bancoRepository.findById(id).ifPresent(fator -> {
            fator.setAtivo(false);
            bancoRepository.save(fator);
        });
    }
}