package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.EnergiaECombustivel;
import br.gov.serpro.calculadoraacv.repository.EnergiaECombustivelRepository;
import br.gov.serpro.calculadoraacv.dto.EnergiaComFatorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Timestamp; // Adicionar este import

@Service
@Transactional
public class EnergiaDadosService {

    @Autowired
    private EnergiaECombustivelRepository energiaRepository;

    public List<EnergiaComFatorResponse> listarPorUsuarioComFatores(Long usuarioId) {
        List<Object[]> resultados = energiaRepository.findEnergiaComFatorMedioByUsuarioId(usuarioId);
    
        return resultados.stream().map(row -> {
            EnergiaComFatorResponse response = new EnergiaComFatorResponse();
            response.setId(((Number) row[0]).longValue());
            response.setUsuarioId(((Number) row[1]).longValue());
            response.setTipoEnergia((String) row[2]);
            response.setFonteEnergia((String) row[3]);
            response.setConsumoAnual((BigDecimal) row[4]);
            response.setUnidade((String) row[5]);
            response.setEscopo((String) row[6]);
            
            // ano efetivo (derivado): row[7]
            Object anoEfetivoObj = row[7];
            Integer anoEfetivo = anoEfetivoObj != null ? ((Number) anoEfetivoObj).intValue() : null;
            
            // fator médio anual (query já trata COALESCE): row[8]
            response.setFatorMedioAnual((BigDecimal) row[8]);
            
            // datas: row[9], row[10]
            Timestamp dataCriacao = (Timestamp) row[9];
            response.setDataCriacao(dataCriacao != null ? dataCriacao.toLocalDateTime() : null);
            Timestamp dataAtualizacao = (Timestamp) row[10];
            response.setDataAtualizacao(dataAtualizacao != null ? dataAtualizacao.toLocalDateTime() : null);
            
            // ano definido pelo usuário: row[11]
            Object anoRefObj = row[11];
            Integer anoReferencia = anoRefObj != null ? ((Number) anoRefObj).intValue() : null;
            
            // enviar para o front: ano = ano definido pelo usuário (não pré-preencher)
            response.setAno(anoReferencia);
            response.setAnoReferencia(anoReferencia);
            response.setAnoReferenciaEfetivo(anoEfetivo);
            response.setAnoAssumido(anoReferencia == null && anoEfetivo != null);
            
            // dados mensais json: row[12]
            String dadosMensaisJson = (String) row[12];
            response.setDadosMensaisJson(dadosMensaisJson);
            
            // 🔥 CORREÇÃO: ADICIONAR OS NOVOS CAMPOS QUE ESTAVAM FALTANDO
            // NOVOS CAMPOS: média anual calculada, meses preenchidos, status
            BigDecimal mediaAnualCalculada = (BigDecimal) row[13];
            response.setMediaAnualCalculada(mediaAnualCalculada);
    
            Integer mesesPreenchidos = row[14] != null ? ((Number) row[14]).intValue() : null;
            response.setMesesPreenchidos(mesesPreenchidos);
    
            String statusCalculo = (String) row[15];
            response.setStatusCalculo(statusCalculo);
            
            return response;
        }).collect(Collectors.toList());
    }

    public List<EnergiaComFatorResponse> listarTodosComFatores() {
        List<Object[]> resultados = energiaRepository.findAllEnergiaComFatorMedio();
    
        return resultados.stream().map(row -> {
            EnergiaComFatorResponse response = new EnergiaComFatorResponse();
            response.setId(((Number) row[0]).longValue());
            response.setUsuarioId(((Number) row[1]).longValue());
            response.setTipoEnergia((String) row[2]);
            response.setFonteEnergia((String) row[3]);
            response.setConsumoAnual((BigDecimal) row[4]);
            response.setUnidade((String) row[5]);
            response.setEscopo((String) row[6]);
    
            Object anoEfetivoObj = row[7];
            Integer anoEfetivo = anoEfetivoObj != null ? ((Number) anoEfetivoObj).intValue() : null;
    
            response.setFatorMedioAnual((BigDecimal) row[8]);
    
            Timestamp dataCriacao = (Timestamp) row[9];
            response.setDataCriacao(dataCriacao != null ? dataCriacao.toLocalDateTime() : null);
            Timestamp dataAtualizacao = (Timestamp) row[10];
            response.setDataAtualizacao(dataAtualizacao != null ? dataAtualizacao.toLocalDateTime() : null);
    
            Object anoRefObj = row[11];
            Integer anoReferencia = anoRefObj != null ? ((Number) anoRefObj).intValue() : null;
    
            response.setAno(anoReferencia);
            response.setAnoReferencia(anoReferencia);
            response.setAnoReferenciaEfetivo(anoEfetivo);
            response.setAnoAssumido(anoReferencia == null && anoEfetivo != null);
    
            String dadosMensaisJson = (String) row[12];
            response.setDadosMensaisJson(dadosMensaisJson);
    
            // NOVOS CAMPOS: média anual calculada, meses preenchidos, status
            BigDecimal mediaAnualCalculada = (BigDecimal) row[13];
            response.setMediaAnualCalculada(mediaAnualCalculada);
    
            Integer mesesPreenchidos = row[14] != null ? ((Number) row[14]).intValue() : null;
            response.setMesesPreenchidos(mesesPreenchidos);
    
            String statusCalculo = (String) row[15];
            response.setStatusCalculo(statusCalculo);
    
            return response;
        }).collect(Collectors.toList());
    }

    public EnergiaECombustivel salvar(EnergiaECombustivel energia) {
        energia.setDataAtualizacao(LocalDateTime.now());
        if (energia.getDataCriacao() == null) {
            energia.setDataCriacao(LocalDateTime.now());
        }
        return energiaRepository.save(energia);
    }
}
