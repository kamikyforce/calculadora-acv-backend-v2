package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.IngredienteResponse;
import br.gov.serpro.calculadoraacv.model.BdDietaIngrediente;
import br.gov.serpro.calculadoraacv.model.BdFatorEmissaoDietaNdtIngrediente;
import br.gov.serpro.calculadoraacv.model.BdValor;
import br.gov.serpro.calculadoraacv.repository.BdDietaIngredienteRepository;
import br.gov.serpro.calculadoraacv.repository.BdFatorEmissaoDietaNdtIngredienteRepository;
import br.gov.serpro.calculadoraacv.repository.BdValorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BancoIngredientesService {

    private final BdDietaIngredienteRepository bdDietaIngredienteRepository;
    private final BdValorRepository bdValorRepository;
    private final BdFatorEmissaoDietaNdtIngredienteRepository bdFatorEmissaoDietaNdtIngredienteRepository;

    public List<IngredienteResponse> listarTodosIngredientes() {
        log.info("Buscando todos os ingredientes do banco de fatores");
        
        List<IngredienteResponse> ingredientes = new ArrayList<>();
        
        // Buscar ingredientes da tabela bd_dieta_ingredientes
        List<BdDietaIngrediente> dietaIngredientes = bdDietaIngredienteRepository.findAll();
        for (BdDietaIngrediente ingrediente : dietaIngredientes) {
            if (ingrediente.getNomeIngrediente() != null && !ingrediente.getNomeIngrediente().trim().isEmpty()) {
                IngredienteResponse response = new IngredienteResponse();
                response.setId(ingrediente.getId());
                response.setNome(ingrediente.getNomeIngrediente());
                response.setTipo(ingrediente.getTipoIngrediente());
                response.setFonte("bd_dieta_ingredientes");
                response.setNdtPercentual(ingrediente.getNdtPercentual());
                response.setEnergiaBruta(ingrediente.getEnergiaBruta());
                response.setMateriaSeca(ingrediente.getMateriaSeca());
                response.setProteinaBruta(ingrediente.getProteinaBrutaMs());
                response.setFibraDetrgenteNeutro(ingrediente.getFibraDetrgenteNeutro());
                ingredientes.add(response);
            }
        }
        
        // Buscar ingredientes da tabela bd_valor
        List<BdValor> valorIngredientes = bdValorRepository.findAll();
        for (BdValor ingrediente : valorIngredientes) {
            if (ingrediente.getNomeAlimento() != null && !ingrediente.getNomeAlimento().trim().isEmpty()) {
                IngredienteResponse response = new IngredienteResponse();
                response.setId(ingrediente.getId());
                response.setNome(ingrediente.getNomeAlimento());
                response.setTipo(ingrediente.getTipo());
                response.setFonte("bd_valor");
                response.setNdtPercentual(ingrediente.getNdt());
                response.setEnergiaBruta(ingrediente.getEnergiaBrutaTotal());
                response.setMateriaSeca(ingrediente.getMateriaSeca());
                response.setProteinaBruta(ingrediente.getProteinaBruta());
                response.setFibraDetrgenteNeutro(ingrediente.getFibraDetrgenteNeutro());
                response.setRepresentatividadeCorte(ingrediente.getRepresentatividadeCorte());
                response.setRepresentatividadeLeite(ingrediente.getRepresentatividadeLeite());
                response.setExtratoEtereo(ingrediente.getExtratoEtereo());
                response.setMateriaMineral(ingrediente.getMateriaMineral());
                ingredientes.add(response);
            }
        }
        
        // Buscar ingredientes da tabela bd_fator_emissao_dieta_ndt_ingredientes
        List<BdFatorEmissaoDietaNdtIngrediente> fatorIngredientes = bdFatorEmissaoDietaNdtIngredienteRepository.findAll();
        for (BdFatorEmissaoDietaNdtIngrediente ingrediente : fatorIngredientes) {
            if (ingrediente.getNomeIngrediente() != null && !ingrediente.getNomeIngrediente().trim().isEmpty()) {
                IngredienteResponse response = new IngredienteResponse();
                response.setId(ingrediente.getId());
                response.setNome(ingrediente.getNomeIngrediente());
                response.setTipo(ingrediente.getTipoIngrediente());
                response.setFonte("bd_fator_emissao_dieta_ndt_ingredientes");
                response.setNdtPercentual(ingrediente.getNdtPercentual());
                response.setEnergiaBruta(ingrediente.getEnergiaBruta());
                response.setMateriaSeca(ingrediente.getMateriaSeca());
                response.setProteinaBruta(ingrediente.getProteinaBrutaMs());
                response.setFatoresEmissoesCalculados(ingrediente.getFatoresEmissoesCalculados());
                response.setObservacoes(ingrediente.getObservacoes());
                ingredientes.add(response);
            }
        }
        
        // Ordenar por nome
        ingredientes.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
        
        log.info("Encontrados {} ingredientes no banco de fatores", ingredientes.size());
        return ingredientes;
    }

    public List<IngredienteResponse> buscarIngredientesPorTipo(String tipo) {
        log.info("Buscando ingredientes por tipo: {}", tipo);
        
        List<IngredienteResponse> ingredientes = new ArrayList<>();
        
        // Buscar na tabela bd_dieta_ingredientes
        List<BdDietaIngrediente> dietaIngredientes = bdDietaIngredienteRepository.findByTipoIngrediente(tipo);
        for (BdDietaIngrediente ingrediente : dietaIngredientes) {
            if (ingrediente.getNomeIngrediente() != null && !ingrediente.getNomeIngrediente().trim().isEmpty()) {
                IngredienteResponse response = new IngredienteResponse();
                response.setId(ingrediente.getId());
                response.setNome(ingrediente.getNomeIngrediente());
                response.setTipo(ingrediente.getTipoIngrediente());
                response.setFonte("bd_dieta_ingredientes");
                response.setNdtPercentual(ingrediente.getNdtPercentual());
                response.setEnergiaBruta(ingrediente.getEnergiaBruta());
                response.setMateriaSeca(ingrediente.getMateriaSeca());
                response.setProteinaBruta(ingrediente.getProteinaBrutaMs());
                response.setFibraDetrgenteNeutro(ingrediente.getFibraDetrgenteNeutro());
                ingredientes.add(response);
            }
        }
        
        // Buscar na tabela bd_valor
        List<BdValor> valorIngredientes = bdValorRepository.findByTipo(tipo);
        for (BdValor ingrediente : valorIngredientes) {
            if (ingrediente.getNomeAlimento() != null && !ingrediente.getNomeAlimento().trim().isEmpty()) {
                IngredienteResponse response = new IngredienteResponse();
                response.setId(ingrediente.getId());
                response.setNome(ingrediente.getNomeAlimento());
                response.setTipo(ingrediente.getTipo());
                response.setFonte("bd_valor");
                response.setNdtPercentual(ingrediente.getNdt());
                response.setEnergiaBruta(ingrediente.getEnergiaBrutaTotal());
                response.setMateriaSeca(ingrediente.getMateriaSeca());
                response.setProteinaBruta(ingrediente.getProteinaBruta());
                response.setFibraDetrgenteNeutro(ingrediente.getFibraDetrgenteNeutro());
                response.setRepresentatividadeCorte(ingrediente.getRepresentatividadeCorte());
                response.setRepresentatividadeLeite(ingrediente.getRepresentatividadeLeite());
                response.setExtratoEtereo(ingrediente.getExtratoEtereo());
                response.setMateriaMineral(ingrediente.getMateriaMineral());
                ingredientes.add(response);
            }
        }
        
        // Buscar na tabela bd_fator_emissao_dieta_ndt_ingredientes
        List<BdFatorEmissaoDietaNdtIngrediente> fatorIngredientes = bdFatorEmissaoDietaNdtIngredienteRepository.findByTipoIngrediente(tipo);
        for (BdFatorEmissaoDietaNdtIngrediente ingrediente : fatorIngredientes) {
            if (ingrediente.getNomeIngrediente() != null && !ingrediente.getNomeIngrediente().trim().isEmpty()) {
                IngredienteResponse response = new IngredienteResponse();
                response.setId(ingrediente.getId());
                response.setNome(ingrediente.getNomeIngrediente());
                response.setTipo(ingrediente.getTipoIngrediente());
                response.setFonte("bd_fator_emissao_dieta_ndt_ingredientes");
                response.setNdtPercentual(ingrediente.getNdtPercentual());
                response.setEnergiaBruta(ingrediente.getEnergiaBruta());
                response.setMateriaSeca(ingrediente.getMateriaSeca());
                response.setProteinaBruta(ingrediente.getProteinaBrutaMs());
                response.setFatoresEmissoesCalculados(ingrediente.getFatoresEmissoesCalculados());
                response.setObservacoes(ingrediente.getObservacoes());
                ingredientes.add(response);
            }
        }
        
        // Ordenar por nome
        ingredientes.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
        
        return ingredientes;
    }

    public List<IngredienteResponse> buscarIngredientesPorNome(String nome) {
        log.info("Buscando ingredientes por nome: {}", nome);
        
        List<IngredienteResponse> ingredientes = new ArrayList<>();
        
        // Buscar na tabela bd_dieta_ingredientes
        List<BdDietaIngrediente> dietaIngredientes = bdDietaIngredienteRepository.findByNomeIngredienteContainingIgnoreCase(nome);
        for (BdDietaIngrediente ingrediente : dietaIngredientes) {
            IngredienteResponse response = new IngredienteResponse();
            response.setId(ingrediente.getId());
            response.setNome(ingrediente.getNomeIngrediente());
            response.setTipo(ingrediente.getTipoIngrediente());
            response.setFonte("bd_dieta_ingredientes");
            response.setNdtPercentual(ingrediente.getNdtPercentual());
            response.setEnergiaBruta(ingrediente.getEnergiaBruta());
            response.setMateriaSeca(ingrediente.getMateriaSeca());
            response.setProteinaBruta(ingrediente.getProteinaBrutaMs());
            response.setFibraDetrgenteNeutro(ingrediente.getFibraDetrgenteNeutro());
            ingredientes.add(response);
        }
        
        // Buscar na tabela bd_valor
        List<BdValor> valorIngredientes = bdValorRepository.findByNomeAlimentoContainingIgnoreCase(nome);
        for (BdValor ingrediente : valorIngredientes) {
            IngredienteResponse response = new IngredienteResponse();
            response.setId(ingrediente.getId());
            response.setNome(ingrediente.getNomeAlimento());
            response.setTipo(ingrediente.getTipo());
            response.setFonte("bd_valor");
            response.setNdtPercentual(ingrediente.getNdt());
            response.setEnergiaBruta(ingrediente.getEnergiaBrutaTotal());
            response.setMateriaSeca(ingrediente.getMateriaSeca());
            response.setProteinaBruta(ingrediente.getProteinaBruta());
            response.setFibraDetrgenteNeutro(ingrediente.getFibraDetrgenteNeutro());
            response.setRepresentatividadeCorte(ingrediente.getRepresentatividadeCorte());
            response.setRepresentatividadeLeite(ingrediente.getRepresentatividadeLeite());
            response.setExtratoEtereo(ingrediente.getExtratoEtereo());
            response.setMateriaMineral(ingrediente.getMateriaMineral());
            ingredientes.add(response);
        }
        
        // Buscar na tabela bd_fator_emissao_dieta_ndt_ingredientes
        List<BdFatorEmissaoDietaNdtIngrediente> fatorIngredientes = bdFatorEmissaoDietaNdtIngredienteRepository.findByNomeIngredienteContainingIgnoreCase(nome);
        for (BdFatorEmissaoDietaNdtIngrediente ingrediente : fatorIngredientes) {
            IngredienteResponse response = new IngredienteResponse();
            response.setId(ingrediente.getId());
            response.setNome(ingrediente.getNomeIngrediente());
            response.setTipo(ingrediente.getTipoIngrediente());
            response.setFonte("bd_fator_emissao_dieta_ndt_ingredientes");
            response.setNdtPercentual(ingrediente.getNdtPercentual());
            response.setEnergiaBruta(ingrediente.getEnergiaBruta());
            response.setMateriaSeca(ingrediente.getMateriaSeca());
            response.setProteinaBruta(ingrediente.getProteinaBrutaMs());
            response.setFatoresEmissoesCalculados(ingrediente.getFatoresEmissoesCalculados());
            response.setObservacoes(ingrediente.getObservacoes());
            ingredientes.add(response);
        }
        
        // Ordenar por nome
        ingredientes.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
        
        return ingredientes;
    }

    public List<String> listarTiposIngredientes() {
        log.info("Buscando tipos de ingredientes disponíveis");
        
        List<String> tipos = new ArrayList<>();
        
        // Buscar tipos da tabela bd_dieta_ingredientes
        tipos.addAll(bdDietaIngredienteRepository.findDistinctTipoIngrediente());
        
        // Buscar tipos da tabela bd_valor
        tipos.addAll(bdValorRepository.findDistinctTipo());
        
        // Buscar tipos da tabela bd_fator_emissao_dieta_ndt_ingredientes
        tipos.addAll(bdFatorEmissaoDietaNdtIngredienteRepository.findDistinctTipoIngrediente());
        
        // Remover duplicatas e ordenar
        return tipos.stream()
                .distinct()
                .filter(tipo -> tipo != null && !tipo.trim().isEmpty())
                .sorted()
                .collect(Collectors.toList());
    }
}