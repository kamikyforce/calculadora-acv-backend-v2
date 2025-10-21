package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.MutFiltros;
import br.gov.serpro.calculadoraacv.dto.MutRequest;
import br.gov.serpro.calculadoraacv.dto.MutResponse;
import br.gov.serpro.calculadoraacv.dto.MutStats;
import br.gov.serpro.calculadoraacv.enums.*;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.exception.DuplicacaoRegistroException;
import br.gov.serpro.calculadoraacv.model.DadosDesmatamento;
import br.gov.serpro.calculadoraacv.model.DadosSolo;
import br.gov.serpro.calculadoraacv.model.DadosVegetacao;
import br.gov.serpro.calculadoraacv.model.FatorMut;
import br.gov.serpro.calculadoraacv.model.Usuario;
import br.gov.serpro.calculadoraacv.repository.FatorMutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// Add EntityManager import
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
@Slf4j
public class MutService {

    private final FatorMutRepository fatorMutRepository;
    
    // Add EntityManager for direct SQL operations
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public MutResponse criar(MutRequest request, Usuario usuario) {
        validarDadosMut(request);

        // First, check if there's a deleted record with the same combination that can be reactivated
        List<FatorMut> fatoresInativos = fatorMutRepository.findByTipoMudancaAndEscopoAndAtivoFalse(
            request.getTipoMudanca(), request.getEscopo());
        
        FatorMut fatorMut;
        if (!fatoresInativos.isEmpty()) {
            // Reactivate the most recent deleted record (first in the ordered list)
            fatorMut = fatoresInativos.get(0);
            fatorMut.setAtivo(true);
            
            // Use the proper method to clear data and force persistence
            limparDadosEspecificos(fatorMut);
            
            processarDadosEspecificos(fatorMut, request);
            // RN008B — valida unicidade somente após todos os campos da combinação
            validarUnicidadeRN008(fatorMut);
        } else {
            // Check if there's an active record with the same exact combination (tipo + escopo)
            // This check is done here to handle race conditions
            if (verificarExistencia(request.getTipoMudanca(), request.getEscopo())) {
                List<FatorMut> fatoresAtivos = fatorMutRepository
                    .findAllByTipoMudancaAndEscopoAndAtivoTrueOrderByDataAtualizacaoDesc(
                        request.getTipoMudanca(), request.getEscopo());
                if (!fatoresAtivos.isEmpty()) {
                    FatorMut fatorAtivo = fatoresAtivos.get(0);
                    limparDadosEspecificosSafe(fatorAtivo);
                    processarDadosEspecificos(fatorAtivo, request);
                    validarUnicidadeRN008(fatorAtivo);
                    fatorMut = fatorAtivo;
                } else {
                    FatorMut novo = new FatorMut();
                    novo.setTipoMudanca(request.getTipoMudanca());
                    novo.setEscopo(request.getEscopo());
                    novo.setAtivo(true);
                    processarDadosEspecificos(novo, request);
                    validarUnicidadeRN008(novo);
                    fatorMut = novo;
                }
            } else {
                FatorMut novo = new FatorMut();
                novo.setTipoMudanca(request.getTipoMudanca());
                novo.setEscopo(request.getEscopo());
                novo.setAtivo(true);
                processarDadosEspecificos(novo, request);
                validarUnicidadeRN008(novo);
                fatorMut = novo;
            }
        }

        // Antes de salvar, garantir que dataAtualizacao está definida
    fatorMut.setDataAtualizacao(LocalDateTime.now());
    
    fatorMut = fatorMutRepository.save(fatorMut);
    return converterParaResponse(fatorMut);
    }

    @Transactional
    public MutResponse atualizar(Long id, MutRequest request, Usuario usuario) {
        log.info("=== INÍCIO DA ATUALIZAÇÃO MUT ID: {} ===", id);
        log.debug("Dados recebidos - Tipo: {}, Escopo: {}", 
                 request.getTipoMudanca(), request.getEscopo());
        
        try {
            validarDadosMut(request);
            log.debug("Validação dos dados concluída com sucesso");

            FatorMut fatorMut = fatorMutRepository.findByIdAndAtivoTrue(id)
                    .orElseThrow(() -> {
                        log.error("Fator MUT com ID {} não encontrado ou inativo", id);
                        return new ValidacaoException("Fator MUT não encontrado");
                    });
            
            log.debug("Fator MUT encontrado - Tipo atual: {}, Escopo atual: {}", 
                     fatorMut.getTipoMudanca(), fatorMut.getEscopo());

            // Verifica se houve mudança nos dados identificadores
            boolean dadosIdentificadoresAlterados = !fatorMut.getTipoMudanca().equals(request.getTipoMudanca()) ||
                !fatorMut.getEscopo().equals(request.getEscopo());
                
            if (dadosIdentificadoresAlterados) {
                log.debug("Dados identificadores alterados, verificando duplicatas");
                if (fatorMutRepository.existsByTipoMudancaAndEscopoAndAtivoTrueAndIdNot(
                        request.getTipoMudanca(), request.getEscopo(), id)) {
                    log.error("Tentativa de criar duplicata - Tipo: {}, Escopo: {}", 
                             request.getTipoMudanca(), request.getEscopo());
                    throw new ValidacaoException("Já existe um fator MUT com este tipo e escopo");
                }
            }

            // Atualiza dados básicos
            fatorMut.setTipoMudanca(request.getTipoMudanca());
            fatorMut.setEscopo(request.getEscopo());
            log.debug("Dados básicos atualizados");

            // ✅ CORREÇÃO: Limpar dados específicos de forma mais segura
            log.debug("Iniciando limpeza de dados específicos");
            limparDadosEspecificosSafe(fatorMut);
            log.debug("Limpeza de dados específicos concluída");
            
            // Processa novos dados específicos
            log.debug("Iniciando processamento de novos dados específicos");
            processarDadosEspecificos(fatorMut, request);
            // RN008B — valida unicidade somente após todos os campos da combinação
            validarUnicidadeRN008(fatorMut);
            log.debug("Processamento de novos dados específicos concluído");

            // Antes de salvar, garantir que dataAtualizacao está definida
            fatorMut.setDataAtualizacao(LocalDateTime.now());
            
            // ✅ CORREÇÃO: Salvar e fazer flush para garantir persistência
            fatorMut = fatorMutRepository.saveAndFlush(fatorMut);
            log.info("Fator MUT ID {} atualizado com sucesso", id);
            
            return converterParaResponse(fatorMut);
            
        } catch (ValidacaoException e) {
            log.error("Erro de validação ao atualizar MUT ID {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar MUT ID {}: {}", id, e.getMessage(), e);
            throw new ValidacaoException("Erro interno ao atualizar fator MUT: " + e.getMessage());
        }
    }

    @Transactional
    public void remover(Long id) {
        FatorMut fatorMut = fatorMutRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ValidacaoException("Fator MUT não encontrado"));
        fatorMut.setAtivo(false);
        fatorMutRepository.save(fatorMut);
    }

    @Transactional(readOnly = true)
    public MutResponse obterPorId(Long id) {
        FatorMut fatorMut = fatorMutRepository.findByIdAndAtivoTrueWithVegetacaoCategories(id)
                .orElseThrow(() -> new ValidacaoException("Fator MUT não encontrado"));
        return converterParaResponse(fatorMut);
    }

    @Transactional(readOnly = true)
    public Page<MutResponse> listar(MutFiltros filtros) {
        Pageable pageable = PageRequest.of(
                filtros.getPage(),
                filtros.getSize(),
                Sort.Direction.fromString(filtros.getDirection()),
                filtros.getSort());

        // Always filter by ativo=true unless explicitly set to false
        Boolean ativo = filtros.getAtivo() != null ? filtros.getAtivo() : true;
        
        // ✅ NOVO: Usar termoBusca se fornecido, senão usar nome
        String termoBusca = filtros.getTermoBusca() != null && !filtros.getTermoBusca().trim().isEmpty() 
            ? "%" + filtros.getTermoBusca().trim() + "%" 
            : (filtros.getNome() != null && !filtros.getNome().trim().isEmpty() 
                ? "%" + filtros.getNome().trim() + "%" 
                : null);

        // ✅ CORREÇÃO: Usar query sem FETCH para paginação e ordenação
        Page<FatorMut> fatores = fatorMutRepository.findWithFiltersOnly(
                filtros.getTipoMudanca(),
                filtros.getEscopo(),
                ativo,
                filtros.getBiomas() != null && !filtros.getBiomas().isEmpty() ? 
                    filtros.getBiomas().iterator().next() : null,
                termoBusca,
                pageable);

        // ✅ CORREÇÃO: Fazer FETCH separado das coleções
        if (fatores.hasContent()) {
            List<Long> ids = fatores.getContent().stream()
                    .map(FatorMut::getId)
                    .collect(Collectors.toList());
            
            // Load vegetacao categories in separate query
            List<FatorMut> fatoresComVegetacao = fatorMutRepository.findWithVegetacaoCategories(ids);
            Map<Long, FatorMut> vegetacaoMap = fatoresComVegetacao.stream()
                    .collect(Collectors.toMap(FatorMut::getId, Function.identity()));
            
            // Load desmatamento and ufs in separate query
            List<FatorMut> fatoresComDesmatamento = fatorMutRepository.findWithDesmatamentoAndUfs(ids);
            Map<Long, FatorMut> desmatamentoMap = fatoresComDesmatamento.stream()
                    .collect(Collectors.toMap(FatorMut::getId, Function.identity()));
            
            // Load dados solo in separate query
            List<FatorMut> fatoresComSolo = fatorMutRepository.findWithDadosSolo(ids);
            Map<Long, FatorMut> soloMap = fatoresComSolo.stream()
                    .collect(Collectors.toMap(FatorMut::getId, Function.identity()));
            
            // Merge collections back into main entities
            fatores.getContent().forEach(fator -> {
                FatorMut fatorComVegetacao = vegetacaoMap.get(fator.getId());
                if (fatorComVegetacao != null) {
                    fator.setDadosVegetacao(fatorComVegetacao.getDadosVegetacao());
                }
                
                FatorMut fatorComDesmatamento = desmatamentoMap.get(fator.getId());
                if (fatorComDesmatamento != null) {
                    fator.setDadosDesmatamento(fatorComDesmatamento.getDadosDesmatamento());
                }
                
                FatorMut fatorComSolo = soloMap.get(fator.getId());
                if (fatorComSolo != null) {
                    fator.setDadosSolo(fatorComSolo.getDadosSolo());
                }
            });
        }

        return fatores.map(this::converterParaResponse);
    }

    public boolean verificarExistencia(TipoMudanca tipoMudanca, EscopoEnum escopo) {
        boolean existe = fatorMutRepository.existsByTipoMudancaAndEscopoAndAtivoTrue(tipoMudanca, escopo);
        log.info("Verificando existência: tipo={}, escopo={}, existe={}", tipoMudanca, escopo, existe);
        return existe;
    }

    public MutStats obterEstatisticas() {
        MutStats stats = new MutStats();
        stats.setTotalFatores(fatorMutRepository.count());
        stats.setFatoresAtivos(fatorMutRepository.countAtivos());
        return stats;
    }

    @Transactional
    public void importarExcel(MultipartFile arquivo) {
        log.info("=== INÍCIO DA IMPORTAÇÃO EXCEL ===");

        if (arquivo.isEmpty()) {
            throw new ValidacaoException("Arquivo não pode estar vazio");
        }
        
        String nomeArquivo = arquivo.getOriginalFilename();
        if (nomeArquivo == null || (!nomeArquivo.toLowerCase().endsWith(".xlsx") && !nomeArquivo.toLowerCase().endsWith(".xls"))) {
            throw new ValidacaoException("Arquivo deve ser do tipo Excel (.xlsx ou .xls)");
        }
        
        if (arquivo.getSize() > 10 * 1024 * 1024) {
            throw new ValidacaoException("Arquivo muito grande. Tamanho máximo: 10MB");
        }

        try {
            processarArquivoExcelMultiplasAbas(arquivo);
            log.info("=== IMPORTAÇÃO CONCLUÍDA COM SUCESSO ===");
        } catch (IOException e) {
            log.error("Erro ao processar arquivo Excel", e);
            throw new ValidacaoException("Erro ao processar arquivo Excel: " + e.getMessage());
        }
    }

    private void processarArquivoExcelMultiplasAbas(MultipartFile arquivo) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(arquivo.getInputStream())) {
            
            // Mapear abas esperadas conforme regras de negócio
            Map<String, TipoMudanca> abasEsperadas = Map.of(
                "Base | Solo", TipoMudanca.SOLO,
                "Base | Desmatamento", TipoMudanca.DESMATAMENTO,
                "Base | Vegetação", TipoMudanca.VEGETACAO
            );
            
            // Verificar se todas as abas obrigatórias existem
            Set<String> abasEncontradas = new HashSet<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String nomeAba = workbook.getSheetAt(i).getSheetName();
                if (abasEsperadas.containsKey(nomeAba)) {
                    abasEncontradas.add(nomeAba);
                }
            }
            
            if (abasEncontradas.size() != abasEsperadas.size()) {
                Set<String> abasFaltantes = new HashSet<>(abasEsperadas.keySet());
                abasFaltantes.removeAll(abasEncontradas);
                throw new ValidacaoException("Abas obrigatórias não encontradas: " + String.join(", ", abasFaltantes));
            }
            
            // Processar cada aba
            int totalLinhasProcessadas = 0;
            for (Map.Entry<String, TipoMudanca> entry : abasEsperadas.entrySet()) {
                String nomeAba = entry.getKey();
                TipoMudanca tipoMudanca = entry.getValue();
                
                Sheet sheet = workbook.getSheet(nomeAba);
                if (sheet == null) {
                    throw new ValidacaoException("Aba não encontrada: " + nomeAba);
                }
                
                log.info("Processando aba: {}", nomeAba);
                int linhasAba = processarAba(sheet, tipoMudanca, nomeAba);
                totalLinhasProcessadas += linhasAba;
                log.info("Aba {} processada: {} linhas", nomeAba, linhasAba);
            }
            
            log.info("Total de linhas processadas: {}", totalLinhasProcessadas);
        }
    }

    private int processarAba(Sheet sheet, TipoMudanca tipoMudanca, String nomeAba) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new ValidacaoException("Aba " + nomeAba + " não possui cabeçalho");
        }

        validarCabecalhoEspecifico(headerRow, tipoMudanca, nomeAba);

        int linhasProcessadas = 0;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) {
                continue;
            }
            
            try {
                switch (tipoMudanca) {
                    case SOLO -> processarLinhaSolo(row, i + 1, nomeAba);
                    case DESMATAMENTO -> processarLinhaDesmatamento(row, i + 1, nomeAba);
                    case VEGETACAO -> processarLinhaVegetacao(row, i + 1, nomeAba);
                }
                linhasProcessadas++;
            } catch (Exception e) {
                log.error("Erro ao processar linha {} da aba {}: {}", i + 1, nomeAba, e.getMessage());
                throw new ValidacaoException("Erro na linha " + (i + 1) + " da aba " + nomeAba + ": " + e.getMessage());
            }
        }
        
        return linhasProcessadas;
    }

    private void validarCabecalhoEspecifico(Row headerRow, TipoMudanca tipoMudanca, String nomeAba) {
        String[] cabecalhoEsperado = switch (tipoMudanca) {
            case SOLO -> new String[]{
                "Nome", "Escopo", "Bioma", "Tipo Fator Solo", "Valor Fator", 
                "Fator CO2", "Fator CH4", "Fator N2O", "Descrição"
            };
            case DESMATAMENTO -> new String[]{
                "Nome", "Escopo", "Bioma", "Nome Fitofisionomia", "Sigla Fitofisionomia", 
                "Categoria Desmatamento", "UFs", "Estoque Carbono", "Fator CO2", "Fator CH4", "Fator N2O"
            };
            case VEGETACAO -> new String[]{
                "Nome", "Escopo", "Bioma", "Categorias Fitofisionomia", "Parâmetro", 
                "Fator CO2", "Fator CH4", "Fator N2O"
            };
        };

        List<String> cabecalhoRecebido = new ArrayList<>();
        int ultimaColuna = headerRow.getLastCellNum();
        
        for (int i = 0; i < ultimaColuna; i++) {
            Cell cell = headerRow.getCell(i);
            String valor = getCellValueAsString(cell);
            cabecalhoRecebido.add(valor != null ? valor.trim() : "");
        }

        if (cabecalhoRecebido.size() != cabecalhoEsperado.length) {
            throw new ValidacaoException(
                String.format("Aba %s: Número de colunas incorreto. Esperado: %d, Recebido: %d. Colunas esperadas: %s", 
                             nomeAba, cabecalhoEsperado.length, cabecalhoRecebido.size(),
                             String.join(", ", cabecalhoEsperado)));
        }

        for (int i = 0; i < cabecalhoEsperado.length; i++) {
            String esperado = cabecalhoEsperado[i];
            String recebido = cabecalhoRecebido.get(i);
            
            if (recebido == null || recebido.trim().isEmpty()) {
                throw new ValidacaoException(
                    String.format("Aba %s: Coluna %d está vazia. Esperado: '%s'", nomeAba, i + 1, esperado));
            }
            
            if (!esperado.equalsIgnoreCase(recebido.trim())) {
                throw new ValidacaoException(
                    String.format("Aba %s: Cabeçalho inválido na coluna %d. Esperado: '%s', Recebido: '%s'", 
                                 nomeAba, i + 1, esperado, recebido));
            }
        }
        
        log.info("Cabeçalho da aba {} validado com sucesso", nomeAba);
    }

    private void processarLinhaSolo(Row row, int numeroLinha, String nomeAba) {
        // Extrai valores específicos para Solo
        String nome = getCellValueAsString(row.getCell(0));
        String escopoStr = getCellValueAsString(row.getCell(1));
        String biomaStr = getCellValueAsString(row.getCell(2));
        String tipoFatorSoloStr = getCellValueAsString(row.getCell(3));
        BigDecimal valorFator = getCellValueAsBigDecimal(row.getCell(4));
        BigDecimal fatorCO2 = getCellValueAsBigDecimal(row.getCell(5));
        BigDecimal fatorCH4 = getCellValueAsBigDecimal(row.getCell(6));
        BigDecimal fatorN2O = getCellValueAsBigDecimal(row.getCell(7));
        String descricao = getCellValueAsString(row.getCell(8));

        // Validações obrigatórias
        validarCamposObrigatorios(nome, escopoStr, biomaStr, numeroLinha, nomeAba);
        
        if (tipoFatorSoloStr == null || tipoFatorSoloStr.trim().isEmpty()) {
            throw new ValidacaoException("Tipo Fator Solo é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }

        // Conversões
        EscopoEnum escopo = converterEscopo(escopoStr);
        Bioma bioma = converterBioma(biomaStr);
        
        // Log da conversão de valores simples
        log.debug("Linha {}: Convertendo tipo fator solo '{}' (valor simples)", numeroLinha, tipoFatorSoloStr);
        TipoFatorSolo tipoFatorSolo = converterTipoFatorSolo(tipoFatorSoloStr);

        // Verifica duplicatas
        if (fatorMutRepository.existsByTipoMudancaAndEscopoAndAtivoTrue(TipoMudanca.SOLO, escopo)) {
            throw new ValidacaoException(
                String.format("Fator MUT Solo ativo já existe na linha %d da aba %s com Escopo: '%s'", 
                             numeroLinha, nomeAba, escopo));
        }

        // Cria ou reativa fator MUT
        FatorMut fatorMut = criarOuReativarFatorMut(TipoMudanca.SOLO, escopo, numeroLinha, nomeAba);
        
        // Cria dados específicos do Solo
        DadosSolo dadosSolo = new DadosSolo();
        dadosSolo.setBioma(bioma);
        dadosSolo.setTipoFatorSolo(tipoFatorSolo);
        dadosSolo.setValorFator(valorFator != null ? valorFator : BigDecimal.ZERO);
        dadosSolo.setFatorCO2(fatorCO2 != null ? fatorCO2 : BigDecimal.ZERO);
        dadosSolo.setFatorCH4(fatorCH4 != null ? fatorCH4 : BigDecimal.ZERO);
        dadosSolo.setFatorN2O(fatorN2O != null ? fatorN2O : BigDecimal.ZERO);
        dadosSolo.setDescricao(descricao != null ? descricao.trim() : "Importado via Excel");
        dadosSolo.setFatorMut(fatorMut);
        
        fatorMut.setDadosSolo(List.of(dadosSolo));
        fatorMutRepository.save(fatorMut);
        
        log.debug("Linha {} da aba {} processada com sucesso: {}", numeroLinha, nomeAba, nome);
    }

    private void processarLinhaDesmatamento(Row row, int numeroLinha, String nomeAba) {
        // Extrai valores específicos para Desmatamento
        String nome = getCellValueAsString(row.getCell(0));
        String escopoStr = getCellValueAsString(row.getCell(1));
        String biomaStr = getCellValueAsString(row.getCell(2));
        String nomeFitofisionomia = getCellValueAsString(row.getCell(3));
        String siglaFitofisionomiaStr = getCellValueAsString(row.getCell(4));
        String categoriaDesmatamentoStr = getCellValueAsString(row.getCell(5));
        String ufsStr = getCellValueAsString(row.getCell(6));
        BigDecimal estoqueCarbono = getCellValueAsBigDecimal(row.getCell(7));
        BigDecimal fatorCO2 = getCellValueAsBigDecimal(row.getCell(8));
        BigDecimal fatorCH4 = getCellValueAsBigDecimal(row.getCell(9));
        BigDecimal fatorN2O = getCellValueAsBigDecimal(row.getCell(10));

        // Validações obrigatórias
        validarCamposObrigatorios(nome, escopoStr, biomaStr, numeroLinha, nomeAba);
        
        if (nomeFitofisionomia == null || nomeFitofisionomia.trim().isEmpty()) {
            throw new ValidacaoException("Nome Fitofisionomia é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }

        // Conversões
        EscopoEnum escopo = converterEscopo(escopoStr);
        Bioma bioma = converterBioma(biomaStr);
        SiglaFitofisionomia siglaFitofisionomia = converterSiglaFitofisionomia(siglaFitofisionomiaStr);
        CategoriaDesmatamento categoriaDesmatamento = converterCategoriaDesmatamento(categoriaDesmatamentoStr);
        
        // Log da conversão de valores simples para array
        log.debug("Linha {}: Convertendo UFs '{}' para array", numeroLinha, ufsStr);
        Set<UF> ufs = converterUfs(ufsStr);
        log.debug("Linha {}: UFs convertidas: {}", numeroLinha, ufs);

        // Verifica duplicatas
        if (fatorMutRepository.existsByTipoMudancaAndEscopoAndAtivoTrue(TipoMudanca.DESMATAMENTO, escopo)) {
            throw new ValidacaoException(
                String.format("Fator MUT Desmatamento ativo já existe na linha %d da aba %s com Escopo: '%s'", 
                             numeroLinha, nomeAba, escopo));
        }

        // Cria ou reativa fator MUT
        FatorMut fatorMut = criarOuReativarFatorMut(TipoMudanca.DESMATAMENTO, escopo, numeroLinha, nomeAba);
        
        // Cria dados específicos do Desmatamento
        DadosDesmatamento dadosDesmatamento = new DadosDesmatamento();
        dadosDesmatamento.setBioma(bioma);
        dadosDesmatamento.setNomeFitofisionomia(nomeFitofisionomia.trim());
        dadosDesmatamento.setSiglaFitofisionomia(siglaFitofisionomia);
        dadosDesmatamento.setCategoriaDesmatamento(categoriaDesmatamento);
        dadosDesmatamento.setUfs(ufs);
        dadosDesmatamento.setEstoqueCarbono(estoqueCarbono != null ? estoqueCarbono : BigDecimal.ZERO);
        dadosDesmatamento.setFatorCO2(fatorCO2 != null ? fatorCO2 : BigDecimal.ZERO);
        dadosDesmatamento.setFatorCH4(fatorCH4 != null ? fatorCH4 : BigDecimal.ZERO);
        dadosDesmatamento.setFatorN2O(fatorN2O != null ? fatorN2O : BigDecimal.ZERO);
        dadosDesmatamento.setValorUnico(ufs == null || ufs.isEmpty());
        dadosDesmatamento.setFatorMut(fatorMut);
        
        fatorMut.setDadosDesmatamento(List.of(dadosDesmatamento));
        fatorMutRepository.save(fatorMut);
        
        log.debug("Linha {} da aba {} processada com sucesso: {}", numeroLinha, nomeAba, nome);
    }

    private void processarLinhaVegetacao(Row row, int numeroLinha, String nomeAba) {
        // Extrai valores específicos para Vegetação
        String nome = getCellValueAsString(row.getCell(0));
        String escopoStr = getCellValueAsString(row.getCell(1));
        String biomaStr = getCellValueAsString(row.getCell(2));
        String categoriasFitofisionomiaStr = getCellValueAsString(row.getCell(3));
        String parametro = getCellValueAsString(row.getCell(4));
        BigDecimal fatorCO2 = getCellValueAsBigDecimal(row.getCell(5));
        BigDecimal fatorCH4 = getCellValueAsBigDecimal(row.getCell(6));
        BigDecimal fatorN2O = getCellValueAsBigDecimal(row.getCell(7));

        // Validações obrigatórias
        validarCamposObrigatorios(nome, escopoStr, biomaStr, numeroLinha, nomeAba);
        
        if (categoriasFitofisionomiaStr == null || categoriasFitofisionomiaStr.trim().isEmpty()) {
            throw new ValidacaoException("Categorias Fitofisionomia é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }

        // Conversões
        EscopoEnum escopo = converterEscopo(escopoStr);
        Bioma bioma = converterBioma(biomaStr);
        
        // Log da conversão de valores simples para array
        log.debug("Linha {}: Convertendo categorias fitofisionomia '{}' para array", numeroLinha, categoriasFitofisionomiaStr);
        Set<CategoriaDesmatamento> categoriasFitofisionomia = converterCategoriasFitofisionomia(categoriasFitofisionomiaStr);
        log.debug("Linha {}: Categorias convertidas: {}", numeroLinha, categoriasFitofisionomia);

        // Verifica duplicatas
        if (fatorMutRepository.existsByTipoMudancaAndEscopoAndAtivoTrue(TipoMudanca.VEGETACAO, escopo)) {
            throw new ValidacaoException(
                String.format("Fator MUT Vegetação ativo já existe na linha %d da aba %s com Escopo: '%s'", 
                             numeroLinha, nomeAba, escopo));
        }

        // Cria ou reativa fator MUT
        FatorMut fatorMut = criarOuReativarFatorMut(TipoMudanca.VEGETACAO, escopo, numeroLinha, nomeAba);
        
        // Cria dados específicos da Vegetação
        DadosVegetacao dadosVegetacao = new DadosVegetacao();
        dadosVegetacao.setBioma(bioma);
        dadosVegetacao.setCategoriasFitofisionomia(categoriasFitofisionomia);
        dadosVegetacao.setParametro(parametro != null ? parametro.trim() : "Importado via Excel");
        dadosVegetacao.setFatorCO2(fatorCO2 != null ? fatorCO2 : BigDecimal.ZERO);
        dadosVegetacao.setFatorCH4(fatorCH4 != null ? fatorCH4 : BigDecimal.ZERO);
        dadosVegetacao.setFatorN2O(fatorN2O != null ? fatorN2O : BigDecimal.ZERO);
        dadosVegetacao.setFatorMut(fatorMut);
        
        fatorMut.setDadosVegetacao(List.of(dadosVegetacao));
        fatorMutRepository.save(fatorMut);
        
        log.debug("Linha {} da aba {} processada com sucesso: {}", numeroLinha, nomeAba, nome);
    }

    private void validarCamposObrigatorios(String nome, String escopoStr, String biomaStr, int numeroLinha, String nomeAba) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }
        
        if (escopoStr == null || escopoStr.trim().isEmpty()) {
            throw new ValidacaoException("Escopo é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }
        
        if (biomaStr == null || biomaStr.trim().isEmpty()) {
            throw new ValidacaoException("Bioma é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }
    }

    private FatorMut criarOuReativarFatorMut(TipoMudanca tipoMudanca, EscopoEnum escopo, int numeroLinha, String nomeAba) {
        List<FatorMut> fatoresInativos = fatorMutRepository.findByTipoMudancaAndEscopoAndAtivoFalse(tipoMudanca, escopo);
        
        FatorMut fatorMut;
        if (!fatoresInativos.isEmpty()) {
            fatorMut = fatoresInativos.get(0);
            fatorMut.setAtivo(true);
            fatorMut.setDataAtualizacao(LocalDateTime.now());
            log.debug("Reativando fator MUT existente na linha {} da aba {}", numeroLinha, nomeAba);
        } else {
            fatorMut = new FatorMut();
            fatorMut.setTipoMudanca(tipoMudanca);
            fatorMut.setEscopo(escopo);
            fatorMut.setAtivo(true);
            fatorMut.setDataAtualizacao(LocalDateTime.now()); // Adicionar esta linha
            log.debug("Criando novo fator MUT na linha {} da aba {}", numeroLinha, nomeAba);
        }
        
        return fatorMut;
    }

    private TipoMudanca converterTipoMudanca(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Tipo de mudança não pode ser vazio");
        }
        
        String valorLimpo = valor.trim().toUpperCase();
        return switch (valorLimpo) {
            case "DESMATAMENTO" -> TipoMudanca.DESMATAMENTO;
            case "VEGETACAO", "VEGETAÇÃO" -> TipoMudanca.VEGETACAO;
            case "SOLO" -> TipoMudanca.SOLO;
            default -> throw new ValidacaoException("Tipo de mudança inválido: " + valor + 
                ". Valores aceitos: DESMATAMENTO, VEGETACAO, SOLO");
        };
    }

    private EscopoEnum converterEscopo(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Escopo não pode ser vazio");
        }
        
        String valorLimpo = valor.trim().toUpperCase().replace(" ", "_");
        return switch (valorLimpo) {
            case "ESCOPO_1", "ESCOPO1", "1" -> EscopoEnum.ESCOPO1;
            case "ESCOPO_2", "ESCOPO2", "2" -> EscopoEnum.ESCOPO2;
            case "ESCOPO_3", "ESCOPO3", "3" -> EscopoEnum.ESCOPO3;
            default -> throw new ValidacaoException("Escopo inválido: " + valor + 
                ". Valores aceitos: ESCOPO_1, ESCOPO_2, ESCOPO_3");
        };
    }

    private TipoFatorSolo converterTipoFatorSolo(String tipoFatorSolo) {
        if (tipoFatorSolo == null || tipoFatorSolo.trim().isEmpty()) {
            return TipoFatorSolo.USO_ANTERIOR_ATUAL;
        }

        String tipo = tipoFatorSolo.trim().toUpperCase();
        
        // Mapeamento para os novos valores do enum
        switch (tipo) {
            case "SOLO_LAC":
            case "SOLO DE BAIXA ATIVIDADE ARGILOSA (LAC)":
            case "LAC":
            case "ALTO_CARBONO":
            case "BAIXO_CARBONO":
                return TipoFatorSolo.USO_ANTERIOR_ATUAL;
                
            case "SOLO_ARENOSO":
            case "SOLO ARENOSO":
            case "ARENOSO":
            case "MINERAL":
            case "ORGANICO":
                return TipoFatorSolo.SOLO_USO_ANTERIOR_ATUAL;
                
            case "USO_ANTERIOR_ATUAL":
                return TipoFatorSolo.USO_ANTERIOR_ATUAL;
                
            case "SOLO_USO_ANTERIOR_ATUAL":
                return TipoFatorSolo.SOLO_USO_ANTERIOR_ATUAL;
                
            default:
                throw new IllegalArgumentException("Tipo de fator solo inválido: " + tipoFatorSolo + 
                    ". Valores aceitos: USO_ANTERIOR_ATUAL, SOLO_USO_ANTERIOR_ATUAL");
        }
    }

    private SiglaFitofisionomia converterSiglaFitofisionomia(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return SiglaFitofisionomia.FOD; // Default value
        }
        
        String valorLimpo = valor.trim();
        try {
            return SiglaFitofisionomia.fromCodigo(valorLimpo);
        } catch (IllegalArgumentException e) {
            // Log the error but don't throw exception, return default value instead
            log.warn("Sigla Fitofisionomia inválida '{}', usando valor padrão FOD. Valores aceitos: {}", 
                valor, Arrays.toString(SiglaFitofisionomia.values()));
            return SiglaFitofisionomia.FOD;
        }
    }

    private CategoriaDesmatamento converterCategoriaDesmatamento(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Categoria Desmatamento não pode ser vazia");
        }
        
        // Remove possíveis caracteres de formatação JSON/array que podem vir do Excel
        String valorLimpo = valor.trim()
            .replaceAll("^\\[|\\]$", "")  // Remove colchetes do início/fim
            .replaceAll("\"|\'|", "")      // Remove aspas
            .trim();
        
        try {
            return CategoriaDesmatamento.fromString(valorLimpo);
        } catch (IllegalArgumentException e) {
            throw new ValidacaoException("Categoria Desmatamento inválida: " + valor + 
                ". Valores aceitos: O, F, OFL, G. Use valores simples sem colchetes ou aspas.");
        }
    }

    private Set<CategoriaDesmatamento> converterCategoriasFitofisionomia(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Categorias Fitofisionomia não pode ser vazia");
        }
        
        Set<CategoriaDesmatamento> categorias = new HashSet<>();
        
        // Remove possíveis caracteres de formatação JSON/array que podem vir do Excel
        String valorLimpo = valor.trim()
            .replaceAll("^\\[|\\]$", "")  // Remove colchetes do início/fim
            .replaceAll("\"|\'|", "")      // Remove aspas
            .trim();
        
        // Divide por vírgula, ponto e vírgula ou espaço
        String[] valores = valorLimpo.split("[,;\\s]+");
        
        for (String v : valores) {
            if (v.trim().isEmpty()) continue;
            categorias.add(converterCategoriaDesmatamento(v.trim()));
        }
        
        if (categorias.isEmpty()) {
            throw new ValidacaoException("Pelo menos uma Categoria Fitofisionomia deve ser informada: " + valor);
        }
        
        return categorias;
    }

    private Bioma converterBioma(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Bioma não pode ser vazio");
        }
        
        String valorLimpo = valor.trim().toUpperCase();
        return switch (valorLimpo) {
            case "AMAZONIA", "AMAZÔNIA" -> Bioma.AMAZONIA;
            case "CAATINGA" -> Bioma.CAATINGA;
            case "CERRADO" -> Bioma.CERRADO;
            case "MATA_ATLANTICA", "MATA ATLÂNTICA", "MATA ATLANTICA" -> Bioma.MATA_ATLANTICA;
            case "PAMPA" -> Bioma.PAMPA;
            case "PANTANAL" -> Bioma.PANTANAL;
            default -> throw new ValidacaoException("Bioma inválido: " + valor + 
                ". Valores aceitos: AMAZONIA, CAATINGA, CERRADO, MATA_ATLANTICA, PAMPA, PANTANAL");
        };
    }

    private Set<UF> converterUfs(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return new HashSet<>(); // UFs são opcionais
        }
        
        Set<UF> ufs = new HashSet<>();
        
        // Remove possíveis caracteres de formatação JSON/array que podem vir do Excel
        String valorLimpo = valor.trim()
            .replaceAll("^\\[|\\]$", "")  // Remove colchetes do início/fim
            .replaceAll("\"|'|", "")      // Remove aspas
            .trim();
        
        // Divide por vírgula, ponto e vírgula ou espaço
        String[] valores = valorLimpo.split("[,;\\s]+");
        
        for (String v : valores) {
            if (v.trim().isEmpty()) continue;
            
            String ufLimpa = v.trim().toUpperCase();
            try {
                UF uf = UF.valueOf(ufLimpa);
                ufs.add(uf);
            } catch (IllegalArgumentException e) {
                throw new ValidacaoException("UF inválida: " + v + 
                    ". Valores aceitos: " + Arrays.toString(UF.values()) + 
                    ". Use valores simples sem colchetes ou aspas.");
            }
        }
        
        return ufs;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    // Remove decimais desnecessários para números inteiros
                    double numValue = cell.getNumericCellValue();
                    if (numValue == Math.floor(numValue)) {
                        yield String.valueOf((long) numValue);
                    } else {
                        yield String.valueOf(numValue);
                    }
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    yield cell.getStringCellValue();
                }
            }
            default -> null;
        };
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        
        return switch (cell.getCellType()) {
            case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING -> {
                String value = cell.getStringCellValue().trim();
                if (value.isEmpty()) {
                    yield BigDecimal.ZERO;
                }
                try {
                    // Remove caracteres não numéricos exceto ponto e vírgula
                    value = value.replaceAll("[^0-9.,\\-]", "");
                    // Substitui vírgula por ponto para decimal
                    value = value.replace(",", ".");
                    yield new BigDecimal(value);
                } catch (NumberFormatException e) {
                    throw new ValidacaoException("Valor numérico inválido: '" + cell.getStringCellValue() + "'");
                }
            }
            case FORMULA -> {
                try {
                    yield BigDecimal.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    yield BigDecimal.ZERO;
                }
            }
            default -> BigDecimal.ZERO;
        };
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        
        // Verifica as 7 colunas obrigatórias
        for (int i = 0; i < 7; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void validarDadosMut(MutRequest request) {
        // Nome é opcional - removida validação obrigatória
        if (request.getTipoMudanca() == null)
            throw new ValidacaoException("Tipo de mudança é obrigatório");
        if (request.getEscopo() == null)
            throw new ValidacaoException("Escopo é obrigatório");

        switch (request.getTipoMudanca()) {
            case DESMATAMENTO -> validarDadosDesmatamento(request.getDadosDesmatamento());
            case VEGETACAO -> validarDadosVegetacao(request.getDadosVegetacao());
            case SOLO -> validarDadosSolo(request.getDadosSolo());
        }
    }

    private void validarDadosDesmatamento(List<MutRequest.DadosDesmatamentoRequest> dados) {
        if (dados == null || dados.isEmpty())
            throw new ValidacaoException("Dados de desmatamento são obrigatórios");
        for (MutRequest.DadosDesmatamentoRequest d : dados) {
            if (d.getBioma() == null) throw new ValidacaoException("Bioma é obrigatório");
            if (d.getValorUnico() == null) throw new ValidacaoException("Campo 'Valor único' é obrigatório");
            if (!d.getValorUnico() && (d.getUfs() == null || d.getUfs().isEmpty()))
                throw new ValidacaoException("UFs são obrigatórias quando 'Valor único' for 'Não'");
            if (d.getNomeFitofisionomia() == null || d.getNomeFitofisionomia().trim().isEmpty())
                throw new ValidacaoException("Nome da fitofisionomia é obrigatório");
            if (d.getSiglaFitofisionomia() == null)
                throw new ValidacaoException("Sigla da fitofisionomia é obrigatória");
            if (d.getCategoriaDesmatamento() == null)
                throw new ValidacaoException("Categoria de desmatamento é obrigatória");
        }
    }

    private void validarDadosVegetacao(List<MutRequest.DadosVegetacaoRequest> dados) {
        if (dados == null || dados.isEmpty())
            throw new ValidacaoException("Dados de vegetação são obrigatórios");
        for (MutRequest.DadosVegetacaoRequest d : dados) {
            if (d.getParametro() == null || d.getParametro().trim().isEmpty())
                throw new ValidacaoException("Parâmetro é obrigatório");
        }
    }

    private void validarDadosSolo(List<MutRequest.DadosSoloRequest> dados) {
        if (dados == null || dados.isEmpty())
            throw new ValidacaoException("Dados de solo são obrigatórios");
        for (MutRequest.DadosSoloRequest d : dados) {
            if (d.getTipoFatorSolo() == null)
                throw new ValidacaoException("Tipo de fator de solo é obrigatório");
            if (d.getValorFator() == null)
                throw new ValidacaoException("Valor do fator é obrigatório");
        }
    }

    private void processarDadosEspecificos(FatorMut fatorMut, MutRequest request) {
        log.debug("Processando dados específicos para tipo: {}", request.getTipoMudanca());
        
        try {
            switch (request.getTipoMudanca()) {
                case DESMATAMENTO -> {
                    int qtdDados = request.getDadosDesmatamento() != null ? request.getDadosDesmatamento().size() : 0;
                    log.debug("Processando {} registros de desmatamento", qtdDados);
                    processarDadosDesmatamento(fatorMut, request.getDadosDesmatamento());
                }
                case VEGETACAO -> {
                    int qtdDados = request.getDadosVegetacao() != null ? request.getDadosVegetacao().size() : 0;
                    log.debug("Processando {} registros de vegetação", qtdDados);
                    processarDadosVegetacao(fatorMut, request.getDadosVegetacao());
                }
                case SOLO -> {
                    int qtdDados = request.getDadosSolo() != null ? request.getDadosSolo().size() : 0;
                    log.debug("Processando {} registros de solo", qtdDados);
                    processarDadosSolo(fatorMut, request.getDadosSolo());
                }
            }
            log.debug("Processamento de dados específicos concluído com sucesso");
        } catch (Exception e) {
            log.error("Erro ao processar dados específicos para tipo {}: {}", request.getTipoMudanca(), e.getMessage(), e);
            throw new ValidacaoException("Erro ao processar dados específicos: " + e.getMessage());
        }
    }

    private void processarDadosDesmatamento(FatorMut fatorMut, List<MutRequest.DadosDesmatamentoRequest> dados) {
        // ✅ CORREÇÃO: Adicionar validação para campos obrigatórios
        for (MutRequest.DadosDesmatamentoRequest d : dados) {
            if (d.getNomeFitofisionomia() == null || d.getNomeFitofisionomia().trim().isEmpty()) {
                throw new ValidacaoException("Nome da fitofisionomia é obrigatório");
            }
            if (d.getSiglaFitofisionomia() == null) {
                throw new ValidacaoException("Sigla da fitofisionomia é obrigatória");
            }
        }
        
        List<DadosDesmatamento> lista = dados.stream().map(d -> {
            DadosDesmatamento dd = new DadosDesmatamento();
            dd.setFatorMut(fatorMut);
            dd.setBioma(d.getBioma());
            dd.setValorUnico(d.getValorUnico());
            dd.setUfs(d.getUfs());
            dd.setNomeFitofisionomia(d.getNomeFitofisionomia());
            dd.setSiglaFitofisionomia(d.getSiglaFitofisionomia());
            dd.setCategoriaDesmatamento(d.getCategoriaDesmatamento());
            dd.setEstoqueCarbono(d.getEstoqueCarbono());
            dd.setFatorCO2(d.getFatorCO2());
            dd.setFatorCH4(d.getFatorCH4());
            dd.setFatorN2O(d.getFatorN2O());
            
            return dd;
        }).collect(Collectors.toList());
        fatorMut.setDadosDesmatamento(lista);
    }

    private void processarDadosVegetacao(FatorMut fatorMut, List<MutRequest.DadosVegetacaoRequest> dados) {
        List<DadosVegetacao> lista = dados.stream().map(d -> {
            DadosVegetacao dv = new DadosVegetacao();
            dv.setFatorMut(fatorMut);
            // ✅ FIX: Se bioma for null, não definir (deixar null para campo opcional)
            // Como o campo tem NOT NULL constraint, vamos usar um valor padrão
            if (d.getBioma() != null) {
                dv.setBioma(d.getBioma());
            }
            // Note: O campo bioma tem constraint NOT NULL no banco, então precisamos de um valor
            // Mas como está sendo usado como opcional no código, vamos remover a constraint
            dv.setCategoriasFitofisionomia(d.getCategoriasFitofisionomia());
            dv.setParametro(d.getParametro());
            dv.setValorAmazonia(d.getValorAmazonia());
            dv.setValorCaatinga(d.getValorCaatinga());
            dv.setValorCerrado(d.getValorCerrado());
            dv.setValorMataAtlantica(d.getValorMataAtlantica());
            dv.setValorPampa(d.getValorPampa());
            dv.setValorPantanal(d.getValorPantanal());
            dv.setFatorCO2(d.getFatorCO2());
            dv.setFatorCH4(d.getFatorCH4());
            dv.setFatorN2O(d.getFatorN2O());
            return dv;
        }).collect(Collectors.toList());
        fatorMut.setDadosVegetacao(lista);
    }

    private void processarDadosSolo(FatorMut fatorMut, List<MutRequest.DadosSoloRequest> dados) {
        if (dados == null || dados.isEmpty()) {
            // Criar dados padrão quando não há dados fornecidos
            criarDadosSoloPadrao(fatorMut);
            return;
        }
        
        List<DadosSolo> lista = dados.stream().map(d -> {
            DadosSolo ds = new DadosSolo();
            ds.setFatorMut(fatorMut);
            ds.setTipoFatorSolo(d.getTipoFatorSolo());
            ds.setValorFator(d.getValorFator());
            ds.setDescricao(d.getDescricao());
            ds.setBioma(d.getBioma());
            ds.setFatorCO2(d.getFatorCO2());
            ds.setFatorCH4(d.getFatorCH4());
            ds.setFatorN2O(d.getFatorN2O());
            ds.setUsoAnterior(d.getUsoAnterior());
            ds.setUsoAtual(d.getUsoAtual());
            if ((ds.getUsoAnterior() == null || ds.getUsoAnterior().isBlank()
                 || ds.getUsoAtual() == null || ds.getUsoAtual().isBlank())
                && ds.getDescricao() != null && !ds.getDescricao().isBlank()) {
                String[] dePara = extractDePara(ds.getDescricao());
                if (dePara != null) {
                    ds.setUsoAnterior(dePara[0]);
                    ds.setUsoAtual(dePara[1]);
                }
            }
            return ds;
        }).collect(Collectors.toList());
        fatorMut.setDadosSolo(lista);
    }

    private String[] extractDePara(String desc) {
        String t = desc.replaceAll("\\s*[→➡➔]\\s*", "->").trim();
        java.util.regex.Matcher m = java.util.regex.Pattern
          .compile("de\\s+(.+?)\\s+para\\s+(.+)", java.util.regex.Pattern.CASE_INSENSITIVE)
          .matcher(t);
        if (m.find()) return new String[]{normalizeUso(m.group(1)), normalizeUso(m.group(2))};
        String[] parts = t.split("\\s*->\\s*");
        if (parts.length == 2) return new String[]{normalizeUso(parts[0]), normalizeUso(parts[1])};
        String[] dash = t.split("\\s*[—–-]\\s*");
        if (dash.length == 2) return new String[]{normalizeUso(dash[0]), normalizeUso(dash[1])};
        return null;
    }

    private String normalizeUso(String s) {
        return s.replaceAll("\\s*\\([^)]*\\)\\s*$", "")
                .replaceAll("\\s*-\\s*Solo\\s+\\w+\\b", "")
                .replaceAll("\\s+", " ").replaceAll("[.,;]\\s*$", "").trim();
    }

    private void limparDadosEspecificos(FatorMut fatorMut) {
        // First, manually delete ElementCollection records that have composite primary keys
        if (fatorMut.getDadosVegetacao() != null && !fatorMut.getDadosVegetacao().isEmpty()) {
            for (DadosVegetacao dv : fatorMut.getDadosVegetacao()) {
                if (dv.getId() != null) {
                    // Delete vegetacao_categorias records manually to avoid composite key constraint issues
                    entityManager.createNativeQuery(
                        "DELETE FROM vegetacao_categorias WHERE dados_vegetacao_id = :dadosVegetacaoId")
                        .setParameter("dadosVegetacaoId", dv.getId())
                        .executeUpdate();
                }
            }
        }
        
        // Clear the collections after manual cleanup
        if (fatorMut.getDadosDesmatamento() != null) {
            fatorMut.getDadosDesmatamento().clear();
        }
        
        if (fatorMut.getDadosVegetacao() != null) {
            fatorMut.getDadosVegetacao().clear();
        }
        
        if (fatorMut.getDadosSolo() != null) {
            fatorMut.getDadosSolo().clear();
        }
        
        // Force persistence of changes to ensure old data is removed
    fatorMutRepository.saveAndFlush(fatorMut);
    
    // Force a flush to ensure all deletions are committed before new insertions
    entityManager.flush();
    }

    // ✅ NOVO MÉTODO: Limpeza mais segura dos dados específicos
    private void limparDadosEspecificosSafe(FatorMut fatorMut) {
        try {
            // First, manually delete ElementCollection records that have composite primary keys
            if (fatorMut.getDadosVegetacao() != null && !fatorMut.getDadosVegetacao().isEmpty()) {
                for (DadosVegetacao dv : fatorMut.getDadosVegetacao()) {
                    if (dv.getId() != null) {
                        // Delete vegetacao_categorias records manually to avoid composite key constraint issues
                        entityManager.createNativeQuery(
                            "DELETE FROM vegetacao_categorias WHERE dados_vegetacao_id = :dadosVegetacaoId")
                            .setParameter("dadosVegetacaoId", dv.getId())
                            .executeUpdate();
                    }
                }
            }
            
            // Clear the collections after manual cleanup
            if (fatorMut.getDadosDesmatamento() != null) {
                fatorMut.getDadosDesmatamento().clear();
            }
            
            if (fatorMut.getDadosVegetacao() != null) {
                fatorMut.getDadosVegetacao().clear();
            }
            
            if (fatorMut.getDadosSolo() != null) {
                fatorMut.getDadosSolo().clear();
            }
            
            // ✅ CORREÇÃO: Usar saveAndFlush com clear para garantir persistência
            fatorMutRepository.saveAndFlush(fatorMut);
            entityManager.clear(); // Limpa o contexto de persistência
            
        } catch (Exception e) {
            log.error("Erro ao limpar dados específicos: {}", e.getMessage(), e);
            throw new ValidacaoException("Erro ao limpar dados específicos: " + e.getMessage());
        }
    }

    // RN008A/B — valida unicidade das combinações por tipo
    private void validarUnicidadeRN008(FatorMut fatorMut) {
        if (fatorMut == null || fatorMut.getTipoMudanca() == null || fatorMut.getEscopo() == null) return;

        switch (fatorMut.getTipoMudanca()) {
            case SOLO -> {
                // Solo: Tipo de fator + Uso anterior + Uso atual
                if (fatorMut.getDadosSolo() == null) return;
                java.util.Set<String> chaves = new java.util.HashSet<>();
                for (DadosSolo ds : fatorMut.getDadosSolo()) {
                    if (ds.getTipoFatorSolo() == null) continue;
                    String usoAnterior = safe(ds.getUsoAnterior());
                    String usoAtual = safe(ds.getUsoAtual());
                    if (usoAnterior.isEmpty() || usoAtual.isEmpty()) continue; // RN008B
                    String key = "SOLO|" + ds.getTipoFatorSolo() + "|" + usoAnterior + "|" + usoAtual;
                    if (!chaves.add(key)) {
                        throw new DuplicacaoRegistroException(
                            String.format("Já existe fator Solo para %s / %s → %s neste escopo.",
                                          ds.getTipoFatorSolo(), usoAnterior, usoAtual));
                    }
                    // ✅ Validação adicional por escopo para campos independentes (LAC/arenoso/referência/fator)
                    long countIndep = contarDuplicidadeSoloIndependente(
                        fatorMut.getEscopo(),
                        ds.getValorFator(),
                        ds.getFatorCO2(),
                        ds.getFatorCH4(),
                        safe(ds.getDescricao()),
                        fatorMut.getId()
                    );
                    if (countIndep > 0) {
                        throw new DuplicacaoRegistroException(
                            "Valores de Solo (LAC/arenoso/referência/fator de emissão) já existem neste escopo.");
                    }
                }
            }
            case DESMATAMENTO -> {
                if (fatorMut.getDadosDesmatamento() == null) return;
                java.util.Set<String> chaves = new java.util.HashSet<>();
                for (DadosDesmatamento dd : fatorMut.getDadosDesmatamento()) {
                    if (dd.getBioma() == null) continue;
                    // Branch: valor único selecionado
                    if (Boolean.TRUE.equals(dd.getValorUnico())) {
                        String key = "DESMATAMENTO|" + dd.getBioma() + "|VALOR_UNICO";
                        if (!chaves.add(key)) {
                            throw new DuplicacaoRegistroException(
                                String.format("Já existe fator de Desmatamento para %s / Valor único neste escopo.", dd.getBioma()));
                        }
                    } else {
                        // Branch: UF habilitada (verifica cada UF da coleção)
                        if (dd.getUfs() == null || dd.getUfs().isEmpty()) continue; // RN008B
                        for (UF uf : dd.getUfs()) {
                            String key = "DESMATAMENTO|" + dd.getBioma() + "|UF:" + uf;
                            if (!chaves.add(key)) {
                                throw new DuplicacaoRegistroException(
                                    String.format("Já existe fator de Desmatamento para %s / %s neste escopo.", dd.getBioma(), uf));
                            }
                        }
                    }
                    // ✅ Validação adicional por escopo para valores independentes (fitofisionomia/sigla/categoria/estoque)
                    long countIndep = contarDuplicidadeDesmatamentoIndependente(
                        fatorMut.getEscopo(),
                        safe(dd.getNomeFitofisionomia()),
                        dd.getSiglaFitofisionomia(),
                        dd.getCategoriaDesmatamento(),
                        dd.getEstoqueCarbono(),
                        fatorMut.getId()
                    );
                    if (countIndep > 0) {
                        throw new DuplicacaoRegistroException(
                            "Valores de Desmatamento (fitofisionomia/sigla/categoria/estoque) já existem neste escopo.");
                    }
                }
            }
            case VEGETACAO -> {
                if (fatorMut.getDadosVegetacao() == null) return;
                java.util.Set<String> chaves = new java.util.HashSet<>();
                for (DadosVegetacao dv : fatorMut.getDadosVegetacao()) {
                    String parametro = dv.getParametro();
                    if (parametro == null || parametro.trim().isEmpty()) continue; // RN008B
                    if (dv.getCategoriasFitofisionomia() == null || dv.getCategoriasFitofisionomia().isEmpty()) continue; // RN008B
                    for (CategoriaDesmatamento cat : dv.getCategoriasFitofisionomia()) {
                        String key = "VEGETACAO|" + cat + "|" + parametro.trim();
                        if (!chaves.add(key)) {
                            throw new DuplicacaoRegistroException(
                                String.format("Já existe fator de Vegetação para %s / %s neste escopo.", cat, parametro));
                        }
                        // ✅ Validação por escopo: categoria + parâmetro não pode duplicar
                        long countIndep = contarDuplicidadeVegetacaoReplicativa(
                            fatorMut.getEscopo(),
                            cat,
                            parametro.trim(),
                            fatorMut.getId()
                        );
                        if (countIndep > 0) {
                            throw new DuplicacaoRegistroException(
                                "Categoria da fitofisionomia + Parâmetro já existem neste escopo.");
                        }
                    }
                }
            }
        }
    }

    // Helpers JPQL de contagem por escopo
    private long contarDuplicidadeSoloIndependente(EscopoEnum escopo, BigDecimal valorFator,
                                                   BigDecimal fatorCO2, BigDecimal fatorCH4,
                                                   String descricao, Long excluirFatorId) {
        String jpql = "SELECT COUNT(ds) FROM DadosSolo ds " +
                      "WHERE ds.fatorMut.escopo = :escopo AND ds.fatorMut.ativo = true " +
                      (excluirFatorId != null ? "AND ds.fatorMut.id <> :excluirId " : "") +
                      "AND COALESCE(ds.valorFator, 0) = COALESCE(:valorFator, 0) " +
                      "AND COALESCE(ds.fatorCO2, 0) = COALESCE(:fatorCO2, 0) " +
                      "AND COALESCE(ds.fatorCH4, 0) = COALESCE(:fatorCH4, 0) " +
                      "AND COALESCE(ds.descricao, '') = COALESCE(:descricao, '')";
        var q = entityManager.createQuery(jpql, Long.class)
            .setParameter("escopo", escopo)
            .setParameter("valorFator", valorFator)
            .setParameter("fatorCO2", fatorCO2)
            .setParameter("fatorCH4", fatorCH4)
            .setParameter("descricao", descricao);
        if (excluirFatorId != null) q.setParameter("excluirId", excluirFatorId);
        return q.getSingleResult();
    }

    private long contarDuplicidadeDesmatamentoIndependente(EscopoEnum escopo, String nome,
                                                           SiglaFitofisionomia sigla,
                                                           CategoriaDesmatamento categoria,
                                                           BigDecimal estoque, Long excluirFatorId) {
        String jpql = "SELECT COUNT(dd) FROM DadosDesmatamento dd " +
                      "WHERE dd.fatorMut.escopo = :escopo AND dd.fatorMut.ativo = true " +
                      (excluirFatorId != null ? "AND dd.fatorMut.id <> :excluirId " : "") +
                      "AND COALESCE(dd.nomeFitofisionomia, '') = COALESCE(:nome, '') " +
                      "AND dd.siglaFitofisionomia = :sigla " +
                      "AND dd.categoriaDesmatamento = :categoria " +
                      "AND COALESCE(dd.estoqueCarbono, 0) = COALESCE(:estoque, 0)";
        var q = entityManager.createQuery(jpql, Long.class)
            .setParameter("escopo", escopo)
            .setParameter("nome", nome)
            .setParameter("sigla", sigla)
            .setParameter("categoria", categoria)
            .setParameter("estoque", estoque);
        if (excluirFatorId != null) q.setParameter("excluirId", excluirFatorId);
        return q.getSingleResult();
    }

    private long contarDuplicidadeVegetacaoReplicativa(EscopoEnum escopo, CategoriaDesmatamento categoria,
                                                       String parametro, Long excluirFatorId) {
        String jpql = "SELECT COUNT(dv) FROM DadosVegetacao dv JOIN dv.categoriasFitofisionomia cat " +
                      "WHERE dv.fatorMut.escopo = :escopo AND dv.fatorMut.ativo = true " +
                      (excluirFatorId != null ? "AND dv.fatorMut.id <> :excluirId " : "") +
                      "AND cat = :categoria AND dv.parametro = :parametro";
        var q = entityManager.createQuery(jpql, Long.class)
            .setParameter("escopo", escopo)
            .setParameter("categoria", categoria)
            .setParameter("parametro", parametro);
        if (excluirFatorId != null) q.setParameter("excluirId", excluirFatorId);
        return q.getSingleResult();
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }

    private MutResponse converterParaResponse(FatorMut fatorMut) {
        MutResponse r = new MutResponse();
        r.setId(fatorMut.getId());
        r.setTipoMudanca(fatorMut.getTipoMudanca());
        r.setEscopo(fatorMut.getEscopo());
        r.setAtivo(fatorMut.getAtivo());
        r.setDataCriacao(fatorMut.getDataCriacao());
        r.setDataAtualizacao(fatorMut.getDataAtualizacao());
        if (fatorMut.getUsuario() != null)
            r.setNomeUsuario(fatorMut.getUsuario().getNome());

        if (fatorMut.getDadosDesmatamento() != null)
            r.setDadosDesmatamento(fatorMut.getDadosDesmatamento().stream()
                    .map(this::converterDadosDesmatamento).collect(Collectors.toList()));
        if (fatorMut.getDadosVegetacao() != null)
            r.setDadosVegetacao(fatorMut.getDadosVegetacao().stream()
                    .map(this::converterDadosVegetacao).collect(Collectors.toList()));
        if (fatorMut.getDadosSolo() != null)
            r.setDadosSolo(fatorMut.getDadosSolo().stream()
                    .map(this::converterDadosSolo).collect(Collectors.toList()));
        return r;
    }

    private MutResponse.DadosDesmatamentoResponse converterDadosDesmatamento(DadosDesmatamento d) {
        MutResponse.DadosDesmatamentoResponse r = new MutResponse.DadosDesmatamentoResponse();
        r.setId(d.getId());
        r.setBioma(d.getBioma());
        r.setValorUnico(d.getValorUnico());
        // Força o carregamento da coleção lazy dentro do contexto transacional
        Set<UF> ufs = d.getUfs();
        if (ufs != null) {
            ufs.size(); // Força a inicialização da coleção lazy
        }
        r.setUfs(ufs);
        r.setNomeFitofisionomia(d.getNomeFitofisionomia());
        r.setSiglaFitofisionomia(d.getSiglaFitofisionomia());
        r.setCategoriaDesmatamento(d.getCategoriaDesmatamento());
        r.setEstoqueCarbono(d.getEstoqueCarbono());
        r.setFatorCO2(d.getFatorCO2());
        r.setFatorCH4(d.getFatorCH4());
        r.setFatorN2O(d.getFatorN2O());
        
        return r;
    }

    private MutResponse.DadosVegetacaoResponse converterDadosVegetacao(DadosVegetacao d) {
        MutResponse.DadosVegetacaoResponse r = new MutResponse.DadosVegetacaoResponse();
        r.setId(d.getId());
        r.setBioma(d.getBioma());
        
        // ✅ CORREÇÃO: Força o carregamento da coleção lazy dentro do contexto transacional
        Set<CategoriaDesmatamento> categorias = d.getCategoriasFitofisionomia();
        if (categorias != null) {
            categorias.size(); // Força a inicialização da coleção lazy
        }
        r.setCategoriasFitofisionomia(categorias);
        
        r.setParametro(d.getParametro());
        r.setValorAmazonia(d.getValorAmazonia());
        r.setValorCaatinga(d.getValorCaatinga());
        r.setValorCerrado(d.getValorCerrado());
        r.setValorMataAtlantica(d.getValorMataAtlantica());
        r.setValorPampa(d.getValorPampa());
        r.setValorPantanal(d.getValorPantanal());
        r.setFatorCO2(d.getFatorCO2());
        r.setFatorCH4(d.getFatorCH4());
        r.setFatorN2O(d.getFatorN2O());
        return r;
    }

    private MutResponse.DadosSoloResponse converterDadosSolo(DadosSolo d) {
        MutResponse.DadosSoloResponse r = new MutResponse.DadosSoloResponse();
        r.setId(d.getId());
        r.setBioma(d.getBioma());
        r.setTipoFatorSolo(d.getTipoFatorSolo());
        r.setValorFator(d.getValorFator());
        r.setFatorCO2(d.getFatorCO2());
        r.setFatorCH4(d.getFatorCH4());
        r.setFatorN2O(d.getFatorN2O());
        r.setDescricao(d.getDescricao());
        r.setUsoAnterior(d.getUsoAnterior());
        r.setUsoAtual(d.getUsoAtual());
        return r;
    }

    private void criarDadosSoloPadrao(FatorMut fatorMut) {
        DadosSolo ds = new DadosSolo();
        ds.setFatorMut(fatorMut);
        ds.setTipoFatorSolo(TipoFatorSolo.USO_ANTERIOR_ATUAL); // Atualizado para novo valor padrão
        ds.setValorFator(BigDecimal.ZERO);
        ds.setDescricao("Dados padrão");
        ds.setBioma(Bioma.AMAZONIA);
        ds.setFatorCO2(BigDecimal.ZERO);
        ds.setFatorCH4(BigDecimal.ZERO);
        ds.setFatorN2O(BigDecimal.ZERO);
        
        fatorMut.getDadosSolo().add(ds);
    }
}