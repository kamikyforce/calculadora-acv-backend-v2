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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.dao.DataIntegrityViolationException;

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
import java.text.Normalizer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
@Slf4j
public class MutService {

    private final FatorMutRepository fatorMutRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public MutResponse criar(MutRequest request, Usuario usuario) {
        validarDadosMut(request);

        // ===== Opção 1: bloquear duplicidade Solo pelo par usoAnterior/usoAtual e devolver idExistente no 409 =====
        try {
            verificarDuplicidadeSoloComIdExistente(request);
        } catch (DuplicacaoRegistroException e) {
            // Encaminha 409 imediatamente com detalhes (handler deve incluir idExistente no body)
            throw e;
        }

        // Primeiro tenta reativar
        List<FatorMut> fatoresInativos = fatorMutRepository.findByTipoMudancaAndEscopoAndAtivoFalse(
                request.getTipoMudanca(), request.getEscopo());

        FatorMut fatorMut;
        if (!fatoresInativos.isEmpty()) {
            fatorMut = fatoresInativos.get(0);
            fatorMut.setAtivo(true);
            limparDadosEspecificosSafe(fatorMut);
            processarDadosEspecificos(fatorMut, request);
            validarUnicidadeRN008(fatorMut); // RN de duplicidade (ver regras por tipo)
        } else {
            // Se já existir ativo com mesmo tipo+escopo, reaproveita
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

        fatorMut.setDataAtualizacao(LocalDateTime.now());
        try {
            fatorMut = fatorMutRepository.save(fatorMut);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            String msg = ex.getMessage();
            if (msg != null && (msg.contains("ux_desm_ufs_escopo") || msg.contains("ufs_hash") || msg.contains("ux_desm_valor_unico_escopo"))) {
                throw new DuplicacaoRegistroException(
                        "RN008_DUPLICIDADE",
                        "Já existe registro de Desmatamento com este Bioma e UFs/Valor Único neste escopo.",
                        null
                );
            }
            throw new DuplicacaoRegistroException(
                    "DADOS_DUPLICADOS",
                    "Dados duplicados detectados. Verifique se o registro já existe.",
                    null
            );
        } catch (Exception ex) {
            // Tradução para 409 sem consultar o repositório nesta transação
            if (isUniqueConstraintViolation(ex)) {
                throw new DuplicacaoRegistroException(
                    "DADOS_DUPLICADOS",
                    "Dados duplicados detectados. Verifique se o registro já existe.",
                    null // evitamos lookup aqui para não auto-flush; idExistente virá da pré-checagem
                );
            }
            throw ex;
        }
        // Replicação automática dos campos comuns para o outro escopo (ESCOPO1 <-> ESCOPO3)
        replicarCamposComunsParaOutroEscopo(fatorMut);
        return converterParaResponse(fatorMut);
    }

    @Transactional
    public MutResponse atualizar(Long id, MutRequest request, Usuario usuario) {
        log.info("=== INÍCIO ATUALIZAÇÃO MUT ID: {} ===", id);
        try {
            validarDadosMut(request);

            FatorMut fatorMut = fatorMutRepository.findByIdAndAtivoTrue(id)
                    .orElseThrow(() -> new ValidacaoException("Fator MUT não encontrado"));

            boolean dadosIdentificadoresAlterados =
                    !fatorMut.getTipoMudanca().equals(request.getTipoMudanca()) ||
                    !fatorMut.getEscopo().equals(request.getEscopo());

            if (dadosIdentificadoresAlterados &&
                    fatorMutRepository.existsByTipoMudancaAndEscopoAndAtivoTrueAndIdNot(
                            request.getTipoMudanca(), request.getEscopo(), id)) {
                throw new ValidacaoException("Já existe um fator MUT com este tipo e escopo");
            }

            // Atualiza chaves de identificação
            fatorMut.setTipoMudanca(request.getTipoMudanca());
            fatorMut.setEscopo(request.getEscopo());

            limparDadosEspecificosSafe(fatorMut);
            processarDadosEspecificos(fatorMut, request);

            // RN008 — duplicidade por escopo dos INDEPENDENTES
            validarUnicidadeRN008(fatorMut);

            fatorMut.setDataAtualizacao(LocalDateTime.now());
            try {
                fatorMut = fatorMutRepository.saveAndFlush(fatorMut);
            } catch (DataIntegrityViolationException ex) {
                String msg = ex.getMessage();
                if (msg != null && (msg.contains("ux_desm_ufs_escopo") || msg.contains("ufs_hash") || msg.contains("ux_desm_valor_unico_escopo"))) {
                    throw new DuplicacaoRegistroException(
                            "RN008_DUPLICIDADE",
                            "Já existe registro de Desmatamento com este Bioma e UFs/Valor Único neste escopo.",
                            id
                    );
                }
                throw ex;
            }
            log.info("Fator MUT ID {} atualizado", id);
            // Replicação automática dos campos comuns para o outro escopo (ESCOPO1 <-> ESCOPO3)
            replicarCamposComunsParaOutroEscopo(fatorMut);
            return converterParaResponse(fatorMut);

        } catch (ValidacaoException e) {
            log.error("Validação falhou ao atualizar MUT {}: {}", id, e.getMessage());
            throw e;
        } catch (DuplicacaoRegistroException e) {
            log.error("Duplicação detectada ao atualizar MUT {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar MUT {}: {}", id, e.getMessage(), e);
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

        Boolean ativo = filtros.getAtivo() != null ? filtros.getAtivo() : true;

        String termoBusca = filtros.getTermoBusca() != null && !filtros.getTermoBusca().trim().isEmpty()
                ? "%" + filtros.getTermoBusca().trim() + "%"
                : (filtros.getNome() != null && !filtros.getNome().trim().isEmpty()
                ? "%" + filtros.getNome().trim() + "%"
                : null);

        Page<FatorMut> fatores = fatorMutRepository.findWithFiltersOnly(
                filtros.getTipoMudanca(),
                filtros.getEscopo(),
                ativo,
                filtros.getBiomas() != null && !filtros.getBiomas().isEmpty()
                        ? filtros.getBiomas().iterator().next()
                        : null,
                termoBusca,
                pageable);

        if (fatores.hasContent()) {
            List<Long> ids = fatores.getContent().stream().map(FatorMut::getId).collect(Collectors.toList());

            List<FatorMut> fatoresComVegetacao = fatorMutRepository.findWithVegetacaoCategories(ids);
            Map<Long, FatorMut> vegetacaoMap = fatoresComVegetacao.stream()
                    .collect(Collectors.toMap(FatorMut::getId, Function.identity()));

            List<FatorMut> fatoresComDesmatamento = fatorMutRepository.findWithDesmatamentoAndUfs(ids);
            Map<Long, FatorMut> desmatamentoMap = fatoresComDesmatamento.stream()
                    .collect(Collectors.toMap(FatorMut::getId, Function.identity()));

            List<FatorMut> fatoresComSolo = fatorMutRepository.findWithDadosSolo(ids);
            Map<Long, FatorMut> soloMap = fatoresComSolo.stream()
                    .collect(Collectors.toMap(FatorMut::getId, Function.identity()));

            fatores.getContent().forEach(f -> {
                FatorMut v = vegetacaoMap.get(f.getId());
                if (v != null) f.setDadosVegetacao(v.getDadosVegetacao());
                FatorMut d = desmatamentoMap.get(f.getId());
                if (d != null) f.setDadosDesmatamento(d.getDadosDesmatamento());
                FatorMut s = soloMap.get(f.getId());
                if (s != null) f.setDadosSolo(s.getDadosSolo());
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

        if (arquivo.isEmpty()) throw new ValidacaoException("Arquivo não pode estar vazio");

        String nomeArquivo = arquivo.getOriginalFilename();
        if (nomeArquivo == null || (!nomeArquivo.toLowerCase().endsWith(".xlsx") && !nomeArquivo.toLowerCase().endsWith(".xls"))) {
            throw new ValidacaoException("Arquivo deve ser .xlsx ou .xls");
        }
        if (arquivo.getSize() > 10 * 1024 * 1024) {
            throw new ValidacaoException("Arquivo muito grande. Máximo: 10MB");
        }

        try {
            processarArquivoExcelMultiplasAbas(arquivo);
            log.info("=== IMPORTAÇÃO CONCLUÍDA ===");
        } catch (IOException e) {
            log.error("Erro ao processar arquivo Excel", e);
            throw new ValidacaoException("Erro ao processar arquivo Excel: " + e.getMessage());
        }
    }

    private void processarArquivoExcelMultiplasAbas(MultipartFile arquivo) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(arquivo.getInputStream())) {
            Map<String, TipoMudanca> abasEsperadas = Map.of(
                    "Base | Solo", TipoMudanca.SOLO,
                    "Base | Desmatamento", TipoMudanca.DESMATAMENTO,
                    "Base | Vegetação", TipoMudanca.VEGETACAO
            );

            Set<String> abasEncontradas = new HashSet<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String nomeAba = workbook.getSheetAt(i).getSheetName();
                if (abasEsperadas.containsKey(nomeAba)) abasEncontradas.add(nomeAba);
            }
            if (abasEncontradas.size() != abasEsperadas.size()) {
                Set<String> faltando = new HashSet<>(abasEsperadas.keySet());
                faltando.removeAll(abasEncontradas);
                throw new ValidacaoException("Abas obrigatórias não encontradas: " + String.join(", ", faltando));
            }

            int total = 0;
            for (Map.Entry<String, TipoMudanca> entry : abasEsperadas.entrySet()) {
                String aba = entry.getKey();
                TipoMudanca tipo = entry.getValue();
                Sheet sheet = workbook.getSheet(aba);
                if (sheet == null) throw new ValidacaoException("Aba não encontrada: " + aba);
                log.info("Processando aba: {}", aba);
                int linhas = processarAba(sheet, tipo, aba);
                total += linhas;
                log.info("Aba {}: {} linhas", aba, linhas);
            }
            log.info("Total processado: {}", total);
        }
    }

    private int processarAba(Sheet sheet, TipoMudanca tipoMudanca, String nomeAba) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) throw new ValidacaoException("Aba " + nomeAba + " não possui cabeçalho");

        validarCabecalhoEspecifico(headerRow, tipoMudanca, nomeAba);

        int linhas = 0;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                switch (tipoMudanca) {
                    case SOLO -> processarLinhaSolo(row, i + 1, nomeAba);
                    case DESMATAMENTO -> processarLinhaDesmatamento(row, i + 1, nomeAba);
                    case VEGETACAO -> processarLinhaVegetacao(row, i + 1, nomeAba);
                }
                linhas++;
            } catch (Exception e) {
                log.error("Erro na linha {} da aba {}: {}", i + 1, nomeAba, e.getMessage());
                throw new ValidacaoException("Erro na linha " + (i + 1) + " da aba " + nomeAba + ": " + e.getMessage());
            }
        }
        return linhas;
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
                    String.format("Aba %s: número de colunas incorreto. Esperado: %d, Recebido: %d. Esperado: %s",
                            nomeAba, cabecalhoEsperado.length, cabecalhoRecebido.size(), String.join(", ", cabecalhoEsperado)));
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
        log.info("Cabeçalho da aba {} validado", nomeAba);
    }

    private void processarLinhaSolo(Row row, int numeroLinha, String nomeAba) {
        String nome = getCellValueAsString(row.getCell(0));
        String escopoStr = getCellValueAsString(row.getCell(1));
        String biomaStr = getCellValueAsString(row.getCell(2));
        String tipoFatorSoloStr = getCellValueAsString(row.getCell(3));
        BigDecimal valorFator = getCellValueAsBigDecimal(row.getCell(4));
        BigDecimal fatorCO2 = getCellValueAsBigDecimal(row.getCell(5));
        BigDecimal fatorCH4 = getCellValueAsBigDecimal(row.getCell(6));
        BigDecimal fatorN2O = getCellValueAsBigDecimal(row.getCell(7));
        String descricao = getCellValueAsString(row.getCell(8));

        validarCamposObrigatorios(nome, escopoStr, biomaStr, numeroLinha, nomeAba);

        if (tipoFatorSoloStr == null || tipoFatorSoloStr.trim().isEmpty()) {
            throw new ValidacaoException("Tipo Fator Solo é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }

        EscopoEnum escopo = converterEscopo(escopoStr);
        Bioma bioma = converterBioma(biomaStr);
        TipoFatorSolo tipoFatorSolo = converterTipoFatorSolo(tipoFatorSoloStr);

        // Não bloqueia a existência do FatorMut — reusa/cria
        FatorMut fatorMut = criarOuReativarFatorMut(TipoMudanca.SOLO, escopo, numeroLinha, nomeAba);

        DadosSolo dadosSolo = new DadosSolo();
        dadosSolo.setBioma(bioma);
        dadosSolo.setTipoFatorSolo(tipoFatorSolo);
        dadosSolo.setValorFator(valorFator != null ? valorFator : BigDecimal.ZERO);
        dadosSolo.setFatorCO2(fatorCO2 != null ? fatorCO2 : BigDecimal.ZERO);
        dadosSolo.setFatorCH4(fatorCH4 != null ? fatorCH4 : BigDecimal.ZERO);
        dadosSolo.setFatorN2O(fatorN2O != null ? fatorN2O : BigDecimal.ZERO);
        // IMPORTANTE: descricao agora é só a referência (sem “A -> B”)
        dadosSolo.setDescricao(descricao != null ? descricao.trim() : "Importado via Excel");
        dadosSolo.setFatorMut(fatorMut);

        fatorMut.setDadosSolo(List.of(dadosSolo));
        fatorMutRepository.save(fatorMut);

        log.debug("Linha {} da aba {} processada (Solo): {}", numeroLinha, nomeAba, nome);
    }

    private void processarLinhaDesmatamento(Row row, int numeroLinha, String nomeAba) {
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

        validarCamposObrigatorios(nome, escopoStr, biomaStr, numeroLinha, nomeAba);
        if (nomeFitofisionomia == null || nomeFitofisionomia.trim().isEmpty()) {
            throw new ValidacaoException("Nome Fitofisionomia é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }

        EscopoEnum escopo = converterEscopo(escopoStr);
        Bioma bioma = converterBioma(biomaStr);
        SiglaFitofisionomia siglaFitofisionomia = converterSiglaFitofisionomia(siglaFitofisionomiaStr);
        CategoriaDesmatamento categoriaDesmatamento = converterCategoriaDesmatamento(categoriaDesmatamentoStr);
        Set<UF> ufs = converterUfs(ufsStr);

        FatorMut fatorMut = criarOuReativarFatorMut(TipoMudanca.DESMATAMENTO, escopo, numeroLinha, nomeAba);

        DadosDesmatamento dd = new DadosDesmatamento();
        dd.setBioma(bioma);
        dd.setNomeFitofisionomia(nomeFitofisionomia.trim());
        dd.setSiglaFitofisionomia(siglaFitofisionomia);
        dd.setCategoriaDesmatamento(categoriaDesmatamento);
        dd.setUfs(ufs);
        dd.setEstoqueCarbono(estoqueCarbono != null ? estoqueCarbono : BigDecimal.ZERO);
        dd.setFatorCO2(fatorCO2 != null ? fatorCO2 : BigDecimal.ZERO);
        dd.setFatorCH4(fatorCH4 != null ? fatorCH4 : BigDecimal.ZERO);
        dd.setFatorN2O(fatorN2O != null ? fatorN2O : BigDecimal.ZERO);
        dd.setValorUnico(ufs == null || ufs.isEmpty());
        dd.setFatorMut(fatorMut);

        fatorMut.setDadosDesmatamento(List.of(dd));
        fatorMutRepository.save(fatorMut);

        log.debug("Linha {} da aba {} processada (Desmatamento): {}", numeroLinha, nomeAba, nome);
    }

    private void processarLinhaVegetacao(Row row, int numeroLinha, String nomeAba) {
        String nome = getCellValueAsString(row.getCell(0));
        String escopoStr = getCellValueAsString(row.getCell(1));
        String biomaStr = getCellValueAsString(row.getCell(2));
        String categoriasFitofisionomiaStr = getCellValueAsString(row.getCell(3));
        String parametro = getCellValueAsString(row.getCell(4));
        BigDecimal fatorCO2 = getCellValueAsBigDecimal(row.getCell(5));
        BigDecimal fatorCH4 = getCellValueAsBigDecimal(row.getCell(6));
        BigDecimal fatorN2O = getCellValueAsBigDecimal(row.getCell(7));

        validarCamposObrigatorios(nome, escopoStr, biomaStr, numeroLinha, nomeAba);
        if (categoriasFitofisionomiaStr == null || categoriasFitofisionomiaStr.trim().isEmpty()) {
            throw new ValidacaoException("Categorias Fitofisionomia é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        }

        EscopoEnum escopo = converterEscopo(escopoStr);
        Bioma bioma = converterBioma(biomaStr);

        Set<CategoriaDesmatamento> categorias = converterCategoriasFitofisionomia(categoriasFitofisionomiaStr);

        FatorMut fatorMut = criarOuReativarFatorMut(TipoMudanca.VEGETACAO, escopo, numeroLinha, nomeAba);

        DadosVegetacao dv = new DadosVegetacao();
        dv.setFatorMut(fatorMut);
        dv.setBioma(bioma); // manter padrão/compat
        dv.setCategoriasFitofisionomia(categorias);
        dv.setParametro(parametro != null ? parametro.trim() : "Importado via Excel");
        dv.setFatorCO2(fatorCO2 != null ? fatorCO2 : BigDecimal.ZERO);
        dv.setFatorCH4(fatorCH4 != null ? fatorCH4 : BigDecimal.ZERO);
        dv.setFatorN2O(fatorN2O != null ? fatorN2O : BigDecimal.ZERO);

        fatorMut.setDadosVegetacao(List.of(dv));
        fatorMutRepository.save(fatorMut);

        log.debug("Linha {} da aba {} processada (Vegetação): {}", numeroLinha, nomeAba, nome);
    }

    private void validarCamposObrigatorios(String nome, String escopoStr, String biomaStr, int numeroLinha, String nomeAba) {
        if (nome == null || nome.trim().isEmpty())
            throw new ValidacaoException("Nome é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        if (escopoStr == null || escopoStr.trim().isEmpty())
            throw new ValidacaoException("Escopo é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
        if (biomaStr == null || biomaStr.trim().isEmpty())
            throw new ValidacaoException("Bioma é obrigatório na linha " + numeroLinha + " da aba " + nomeAba);
    }

    private FatorMut criarOuReativarFatorMut(TipoMudanca tipoMudanca, EscopoEnum escopo, int linha, String aba) {
        List<FatorMut> inativos = fatorMutRepository.findByTipoMudancaAndEscopoAndAtivoFalse(tipoMudanca, escopo);
        FatorMut f;
        if (!inativos.isEmpty()) {
            f = inativos.get(0);
            f.setAtivo(true);
            f.setDataAtualizacao(LocalDateTime.now());
            log.debug("Reativando Fator MUT (linha {} / aba {})", linha, aba);
        } else {
            f = new FatorMut();
            f.setTipoMudanca(tipoMudanca);
            f.setEscopo(escopo);
            f.setAtivo(true);
            f.setDataAtualizacao(LocalDateTime.now());
            log.debug("Criando Fator MUT (linha {} / aba {})", linha, aba);
        }
        return f;
    }

    private TipoMudanca converterTipoMudanca(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Tipo de mudança não pode ser vazio");
        }
        String v = valor.trim().toUpperCase();
        return switch (v) {
            case "DESMATAMENTO" -> TipoMudanca.DESMATAMENTO;
            case "VEGETACAO", "VEGETAÇÃO" -> TipoMudanca.VEGETACAO;
            case "SOLO" -> TipoMudanca.SOLO;
            default -> throw new ValidacaoException("Tipo de mudança inválido: " + valor +
                    ". Valores: DESMATAMENTO, VEGETACAO, SOLO");
        };
    }

    private EscopoEnum converterEscopo(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Escopo não pode ser vazio");
        }
        String v = valor.trim().toUpperCase().replace(" ", "_");
        return switch (v) {
            case "ESCOPO_1", "ESCOPO1", "1" -> EscopoEnum.ESCOPO1;
            case "ESCOPO_2", "ESCOPO2", "2" -> EscopoEnum.ESCOPO2;
            case "ESCOPO_3", "ESCOPO3", "3" -> EscopoEnum.ESCOPO3;
            default -> throw new ValidacaoException("Escopo inválido: " + valor +
                    ". Valores: ESCOPO_1, ESCOPO_2, ESCOPO_3");
        };
    }

    private TipoFatorSolo converterTipoFatorSolo(String tipoFatorSolo) {
        if (tipoFatorSolo == null || tipoFatorSolo.trim().isEmpty()) {
            return TipoFatorSolo.USO_ANTERIOR_ATUAL;
        }
        String tipo = tipoFatorSolo.trim().toUpperCase();
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
                        ". Aceitos: USO_ANTERIOR_ATUAL, SOLO_USO_ANTERIOR_ATUAL");
        }
    }

    private SiglaFitofisionomia converterSiglaFitofisionomia(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return SiglaFitofisionomia.FOD;
        }
        String v = valor.trim();
        try {
            return SiglaFitofisionomia.fromCodigo(v);
        } catch (IllegalArgumentException e) {
            log.warn("Sigla Fitofisionomia inválida '{}', usando FOD. Valores: {}", valor, Arrays.toString(SiglaFitofisionomia.values()));
            return SiglaFitofisionomia.FOD;
        }
    }

    private CategoriaDesmatamento converterCategoriaDesmatamento(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Categoria Desmatamento não pode ser vazia");
        }
        String limpo = valor.trim()
                .replaceAll("^\\[|\\]$", "")
                .replaceAll("[\"']", "") // << CORREÇÃO DE REGEX
                .trim();
        try {
            return CategoriaDesmatamento.fromString(limpo);
        } catch (IllegalArgumentException e) {
            throw new ValidacaoException("Categoria Desmatamento inválida: " + valor +
                    ". Aceitos: O, F, OFL, G. Informe sem colchetes/aspas.");
        }
    }

    private Set<CategoriaDesmatamento> converterCategoriasFitofisionomia(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacaoException("Categorias Fitofisionomia não pode ser vazia");
        }
        Set<CategoriaDesmatamento> categorias = new HashSet<>();
        String limpo = valor.trim()
                .replaceAll("^\\[|\\]$", "")
                .replaceAll("[\"']", "") // << CORREÇÃO DE REGEX
                .trim();
        String[] valores = limpo.split("[,;\\s]+");
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
        String v = valor.trim().toUpperCase();
        return switch (v) {
            case "AMAZONIA", "AMAZÔNIA" -> Bioma.AMAZONIA;
            case "CAATINGA" -> Bioma.CAATINGA;
            case "CERRADO" -> Bioma.CERRADO;
            case "MATA_ATLANTICA", "MATA ATLÂNTICA", "MATA ATLANTICA" -> Bioma.MATA_ATLANTICA;
            case "PAMPA" -> Bioma.PAMPA;
            case "PANTANAL" -> Bioma.PANTANAL;
            default -> throw new ValidacaoException("Bioma inválido: " + valor +
                    ". Aceitos: AMAZONIA, CAATINGA, CERRADO, MATA_ATLANTICA, PAMPA, PANTANAL");
        };
    }

    private Set<UF> converterUfs(String valor) {
        if (valor == null || valor.trim().isEmpty()) return new HashSet<>();
        Set<UF> ufs = new HashSet<>();
        String limpo = valor.trim()
                .replaceAll("^\\[|\\]$", "")
                .replaceAll("[\"']", "") // << CORREÇÃO DE REGEX
                .trim();
        String[] valores = limpo.split("[,;\\s]+");
        for (String v : valores) {
            if (v.trim().isEmpty()) continue;
            String ufLimpa = v.trim().toUpperCase();
            try {
                UF uf = UF.valueOf(ufLimpa);
                ufs.add(uf);
            } catch (IllegalArgumentException e) {
                throw new ValidacaoException("UF inválida: " + v +
                        ". Valores aceitos: " + Arrays.toString(UF.values()) + ". Sem colchetes/aspas.");
            }
        }
        return ufs;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) yield cell.getDateCellValue().toString();
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) yield String.valueOf((long) numValue);
                yield String.valueOf(numValue);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try { yield String.valueOf(cell.getNumericCellValue()); }
                catch (Exception e) { yield cell.getStringCellValue(); }
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
                if (value.isEmpty()) yield BigDecimal.ZERO;
                try {
                    value = value.replaceAll("[^0-9.,\\-]", "");
                    value = value.replace(",", ".");
                    yield new BigDecimal(value);
                } catch (NumberFormatException e) {
                    throw new ValidacaoException("Valor numérico inválido: '" + cell.getStringCellValue() + "'");
                }
            }
            case FORMULA -> {
                try { yield BigDecimal.valueOf(cell.getNumericCellValue()); }
                catch (Exception e) { yield BigDecimal.ZERO; }
            }
            default -> BigDecimal.ZERO;
        };
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int i = 0; i < 7; i++) { // checagem leve
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) return false;
            }
        }
        return true;
    }

    private void validarDadosMut(MutRequest request) {
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
        log.debug("Processando dados específicos: {}", request.getTipoMudanca());
        try {
            switch (request.getTipoMudanca()) {
                case DESMATAMENTO -> processarDadosDesmatamento(fatorMut, request.getDadosDesmatamento());
                case VEGETACAO -> processarDadosVegetacao(fatorMut, request.getDadosVegetacao());
                case SOLO -> processarDadosSolo(fatorMut, request.getDadosSolo());
            }
        } catch (Exception e) {
            log.error("Erro ao processar dados específicos ({}): {}", request.getTipoMudanca(), e.getMessage(), e);
            throw new ValidacaoException("Erro ao processar dados específicos: " + e.getMessage());
        }
    }

    private void processarDadosDesmatamento(FatorMut fatorMut, List<MutRequest.DadosDesmatamentoRequest> dados) {
        for (MutRequest.DadosDesmatamentoRequest d : dados) {
            if (d.getNomeFitofisionomia() == null || d.getNomeFitofisionomia().trim().isEmpty())
                throw new ValidacaoException("Nome da fitofisionomia é obrigatório");
            if (d.getSiglaFitofisionomia() == null)
                throw new ValidacaoException("Sigla da fitofisionomia é obrigatória");
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
            if (d.getBioma() != null) dv.setBioma(d.getBioma());
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
            criarDadosSoloPadrao(fatorMut);
            return;
        }
        List<DadosSolo> lista = dados.stream().map(d -> {
            DadosSolo ds = new DadosSolo();
            ds.setFatorMut(fatorMut);
            ds.setTipoFatorSolo(d.getTipoFatorSolo());
            ds.setValorFator(d.getValorFator());
            // descricao agora é somente a REFERÊNCIA (independente por escopo)
            ds.setDescricao(d.getDescricao());
            ds.setBioma(d.getBioma());
            ds.setFatorCO2(d.getFatorCO2());   // LAC (quando usado pra guardar LAC)
            ds.setFatorCH4(d.getFatorCH4());   // Arenoso (quando usado pra guardar Arenoso)
            ds.setFatorN2O(d.getFatorN2O());
            ds.setUsoAnterior(d.getUsoAnterior());
            ds.setUsoAtual(d.getUsoAtual());
            // Define principal com base em CO2/CH4 nulos
            boolean isPrincipal = (ds.getFatorCO2() == null && ds.getFatorCH4() == null);
            ds.setPrincipal(isPrincipal);
            ds.setReplicadoAutomatico(Boolean.FALSE);

            // fallback: se vier descricao no formato "de A para B" ainda extrai
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
        if (fatorMut.getDadosVegetacao() != null && !fatorMut.getDadosVegetacao().isEmpty()) {
            for (DadosVegetacao dv : fatorMut.getDadosVegetacao()) {
                if (dv.getId() != null) {
                    entityManager.createNativeQuery(
                                    "DELETE FROM vegetacao_categorias WHERE dados_vegetacao_id = :id")
                            .setParameter("id", dv.getId())
                            .executeUpdate();
                }
            }
        }
        if (fatorMut.getDadosDesmatamento() != null) fatorMut.getDadosDesmatamento().clear();
        if (fatorMut.getDadosVegetacao() != null) fatorMut.getDadosVegetacao().clear();
        if (fatorMut.getDadosSolo() != null) fatorMut.getDadosSolo().clear();

        // Apaga explicitamente filhos de SOLO para evitar duplicidade ao reinserir
        if (fatorMut.getId() != null) {
            entityManager.createNativeQuery(
                    "DELETE FROM dados_solo WHERE fator_mut_id = :id")
                .setParameter("id", fatorMut.getId())
                .executeUpdate();
        }

        fatorMutRepository.saveAndFlush(fatorMut);
        entityManager.flush();
    }

    private void limparDadosEspecificosSafe(FatorMut fatorMut) {
        try {
            if (fatorMut.getDadosVegetacao() != null && !fatorMut.getDadosVegetacao().isEmpty()) {
                for (DadosVegetacao dv : fatorMut.getDadosVegetacao()) {
                    if (dv.getId() != null) {
                        entityManager.createNativeQuery(
                                        "DELETE FROM vegetacao_categorias WHERE dados_vegetacao_id = :id")
                                .setParameter("id", dv.getId())
                                .executeUpdate();
                    }
                }
            }
            if (fatorMut.getDadosDesmatamento() != null) fatorMut.getDadosDesmatamento().clear();
            if (fatorMut.getDadosVegetacao() != null) fatorMut.getDadosVegetacao().clear();
            if (fatorMut.getDadosSolo() != null) fatorMut.getDadosSolo().clear();

            // Apaga explicitamente filhos de SOLO para evitar duplicidade ao reinserir
            if (fatorMut.getId() != null) {
                entityManager.createNativeQuery(
                        "DELETE FROM dados_solo WHERE fator_mut_id = :id")
                    .setParameter("id", fatorMut.getId())
                    .executeUpdate();
            }

            fatorMutRepository.saveAndFlush(fatorMut);
            entityManager.clear();
        } catch (Exception e) {
            log.error("Erro ao limpar dados específicos: {}", e.getMessage(), e);
            throw new ValidacaoException("Erro ao limpar dados específicos: " + e.getMessage());
        }
    }

    // ===== RN008 — Unicidade com as novas regras =====
    private void validarUnicidadeRN008(FatorMut fatorMut) {
        if (fatorMut == null || fatorMut.getTipoMudanca() == null || fatorMut.getEscopo() == null) return;

        switch (fatorMut.getTipoMudanca()) {
            case SOLO -> {
                if (fatorMut.getDadosSolo() == null) return;

                // Replicados: tipoFatorSolo + usoAnterior + usoAtual (registro principal: CO2/CH4 nulos)
                Set<String> chaves = new HashSet<>();
                for (DadosSolo ds : fatorMut.getDadosSolo()) {
                    String tipo = normalizeTipoFator(ds.getTipoFatorSolo());
                    String usoAnterior = normalizeNoAccentsLower(ds.getUsoAnterior());
                    String usoAtual = normalizeNoAccentsLower(ds.getUsoAtual());

                    // considera só o "principal" para a chave replicativa
                    if (tipo.isBlank() || usoAnterior.isBlank() || usoAtual.isBlank() || ds.getFatorCO2() != null || ds.getFatorCH4() != null) {
                        continue;
                    }
                    String key = "SOLO|" + tipo + "|" + usoAnterior + "|" + usoAtual;
                    if (!chaves.add(key)) {
                        throw new DuplicacaoRegistroException(
                                String.format("Já existe fator Solo para %s / %s → %s neste escopo.",
                                        ds.getTipoFatorSolo(), usoAnterior, usoAtual));
                    }

                    // Checagem replicativa ESTRITA contra o banco, mesmo escopo (evita duplicar entre ESCOPO1/ESCOPO3)
                    List<FatorMut> candidatos = fatorMutRepository
                            .findAllByTipoMudancaAndEscopoAndAtivoTrueOrderByDataAtualizacaoDesc(TipoMudanca.SOLO, fatorMut.getEscopo());

                    for (FatorMut fm : candidatos) {
                        if (fatorMut.getId() != null && Objects.equals(fm.getId(), fatorMut.getId())) {
                            continue; // ignora o próprio registro durante atualização
                        }
                        List<DadosSolo> dsList = fm.getDadosSolo() != null ? fm.getDadosSolo() : List.of();
                        boolean existeMainEquivalente = dsList.stream().anyMatch(dsBanco -> {
                            String tipoDs = normalizeTipoFator(dsBanco.getTipoFatorSolo());
                            String usoAntDs = normalizeNoAccentsLower(dsBanco.getUsoAnterior());
                            String usoAtDs  = normalizeNoAccentsLower(dsBanco.getUsoAtual());
                            return tipoDs.equals(tipo)
                                    && usoAntDs.equals(usoAnterior)
                                    && usoAtDs.equals(usoAtual)
                                    && dsBanco.getFatorCO2() == null
                                    && dsBanco.getFatorCH4() == null;
                        });
                        if (existeMainEquivalente) {
                            throw new DuplicacaoRegistroException(
                                    String.format("Já existe fator Solo para %s / %s → %s neste escopo.",
                                            ds.getTipoFatorSolo(), usoAnterior, usoAtual));
                        }
                    }

                    // Campos INDEPENDENTES por escopo: antidupe por escopo (vale para principal e auxiliares)
                    long countIndep = contarDuplicidadeSoloIndependente(
                            fatorMut.getEscopo(),
                            ds.getValorFator(),
                            ds.getFatorCO2(),
                            ds.getFatorCH4(),
                            safe(ds.getDescricao()), // Referência
                            fatorMut.getId()
                    );
                    if (countIndep > 0) {
                        throw new DuplicacaoRegistroException(
                                "Valores de Solo (fator emissão/LAC/arenoso/referência) já existem neste escopo.");
                    }
                }
            }
            case DESMATAMENTO -> {
                if (fatorMut.getDadosDesmatamento() == null) return;

                Set<String> chaves = new HashSet<>();
                for (DadosDesmatamento dd : fatorMut.getDadosDesmatamento()) {
                    if (dd.getBioma() == null) continue;

                    // Replicados: Bioma + (UFs quando habilitado) + Valor único
                    String baseKey = "DESMATAMENTO|" + dd.getBioma();
                    if (Boolean.TRUE.equals(dd.getValorUnico())) {
                        String key = baseKey + "|VALOR_UNICO";
                        if (!chaves.add(key)) {
                            throw new DuplicacaoRegistroException(
                              String.format("Já existe fator de Desmatamento para %s / Valor único neste escopo.", dd.getBioma()));
                        }
                    } else {
                        if (dd.getUfs() == null || dd.getUfs().isEmpty()) continue;
                        for (UF uf : dd.getUfs()) {
                            String key = baseKey + "|UF:" + uf;
                            if (!chaves.add(key)) {
                                throw new DuplicacaoRegistroException(
                                  String.format("Já existe fator de Desmatamento para %s / %s neste escopo.", dd.getBioma(), uf));
                            }
                        }
                    }

                    // Independentes por escopo
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

                // Replicados: Categoria da fitofisionomia + Parâmetro (por escopo)
                Set<String> chaves = new HashSet<>();
                for (DadosVegetacao dv : fatorMut.getDadosVegetacao()) {
                    String parametro = dv.getParametro();
                    if (parametro == null || parametro.trim().isEmpty()) continue;
                    if (dv.getCategoriasFitofisionomia() == null || dv.getCategoriasFitofisionomia().isEmpty()) continue;

                    for (var cat : dv.getCategoriasFitofisionomia()) {
                        String key = "VEGETACAO|" + cat + "|" + parametro.trim();
                        if (!chaves.add(key)) {
                            throw new DuplicacaoRegistroException(
                                String.format("Já existe fator de Vegetação para %s / %s neste escopo.", cat, parametro));
                        }
                        long count = contarDuplicidadeVegetacaoReplicativa(
                                fatorMut.getEscopo(), cat, parametro.trim(), fatorMut.getId());
                        if (count > 0) {
                            throw new DuplicacaoRegistroException(
                                "Categoria da fitofisionomia + Parâmetro já existem neste escopo.");
                        }
                    }
                }
            }
        }
    }

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

    private long contarDuplicidadeVegetacaoIndependente(EscopoEnum escopo,
                                                        BigDecimal amazonia,
                                                        BigDecimal caatinga,
                                                        BigDecimal cerrado,
                                                        BigDecimal mataAtlantica,
                                                        BigDecimal pampa,
                                                        BigDecimal pantanal,
                                                        Long excluirFatorId) {
        String jpql = "SELECT COUNT(dv) FROM DadosVegetacao dv " +
                "WHERE dv.fatorMut.escopo = :escopo AND dv.fatorMut.ativo = true " +
                (excluirFatorId != null ? "AND dv.fatorMut.id <> :excluirId " : "") +
                "AND COALESCE(dv.valorAmazonia, 0) = COALESCE(:amazonia, 0) " +
                "AND COALESCE(dv.valorCaatinga, 0) = COALESCE(:caatinga, 0) " +
                "AND COALESCE(dv.valorCerrado, 0) = COALESCE(:cerrado, 0) " +
                "AND COALESCE(dv.valorMataAtlantica, 0) = COALESCE(:mataAtlantica, 0) " +
                "AND COALESCE(dv.valorPampa, 0) = COALESCE(:pampa, 0) " +
                "AND COALESCE(dv.valorPantanal, 0) = COALESCE(:pantanal, 0)";

        var q = entityManager.createQuery(jpql, Long.class)
                .setParameter("escopo", escopo)
                .setParameter("amazonia", amazonia)
                .setParameter("caatinga", caatinga)
                .setParameter("cerrado", cerrado)
                .setParameter("mataAtlantica", mataAtlantica)
                .setParameter("pampa", pampa)
                .setParameter("pantanal", pantanal);

        if (excluirFatorId != null) q.setParameter("excluirId", excluirFatorId);
        return q.getSingleResult();
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }

    // ===== Helper: normaliza string removendo acentos, trim e lower-case =====
    private String normalizeNoAccentsLower(String s) {
        if (s == null) return "";
        String nfd = Normalizer.normalize(s.trim(), Normalizer.Form.NFD);
        // remove diacríticos
        String withoutAccents = nfd.replaceAll("\\p{M}", "");
        return withoutAccents.toLowerCase();
    }

    // ===== Helper: localizar ID existente de Solo main (escopo + tipoFator + usoAnterior/usoAtual) =====
    private Long buscarIdExistenteSoloMain(MutRequest request) {
        if (request == null || request.getTipoMudanca() != TipoMudanca.SOLO) return null;
        if (request.getEscopo() == null || request.getDadosSolo() == null || request.getDadosSolo().isEmpty()) return null;

        MutRequest.DadosSoloRequest mainReq = request.getDadosSolo().stream()
                .filter(ds -> ds.getFatorCO2() == null && ds.getFatorCH4() == null)
                .findFirst()
                .orElse(null);
        if (mainReq == null) return null;

        String tipoNorm = normalizeTipoFator(mainReq.getTipoFatorSolo());
        String usoAnt = normalizeNoAccentsLower(mainReq.getUsoAnterior());
        String usoAt  = normalizeNoAccentsLower(mainReq.getUsoAtual());

        List<FatorMut> candidatos = fatorMutRepository
                .findAllByTipoMudancaAndEscopoAndAtivoTrueOrderByDataAtualizacaoDesc(TipoMudanca.SOLO, request.getEscopo());

        for (FatorMut fm : candidatos) {
            List<DadosSolo> dsList = fm.getDadosSolo() != null ? fm.getDadosSolo() : Collections.emptyList();
            boolean existeMainEquivalente = dsList.stream().anyMatch(ds -> {
                String tipoDs = normalizeTipoFator(ds.getTipoFatorSolo());
                String usoAntDs = normalizeNoAccentsLower(ds.getUsoAnterior());
                String usoAtDs  = normalizeNoAccentsLower(ds.getUsoAtual());
                return tipoDs.equals(tipoNorm)
                        && usoAntDs.equals(usoAnt)
                        && usoAtDs.equals(usoAt)
                        && ds.getFatorCO2() == null
                        && ds.getFatorCH4() == null;
            });
            if (existeMainEquivalente) {
                return fm.getId();
            }
        }
        return null;
    }

    // ===== Helper: verifica duplicidade Solo (escopo + tipoFator + usoAnterior/usoAtual) e lança 409 com idExistente =====
    private void verificarDuplicidadeSoloComIdExistente(MutRequest request) {
        if (request == null || request.getTipoMudanca() != TipoMudanca.SOLO) return;
        if (request.getEscopo() == null || request.getDadosSolo() == null || request.getDadosSolo().isEmpty()) return;

        // Main = registro sem CO2/CH4
        MutRequest.DadosSoloRequest mainReq = request.getDadosSolo().stream()
                .filter(ds -> ds.getFatorCO2() == null && ds.getFatorCH4() == null)
                .findFirst()
                .orElse(null);
        if (mainReq == null) return;

        String tipoNorm = normalizeTipoFator(mainReq.getTipoFatorSolo());
        String usoAnt = normalizeNoAccentsLower(mainReq.getUsoAnterior());
        String usoAt  = normalizeNoAccentsLower(mainReq.getUsoAtual());

        // Procura FatorMut ativo no escopo que contenha um main equivalente
        List<FatorMut> candidatos = fatorMutRepository
                .findAllByTipoMudancaAndEscopoAndAtivoTrueOrderByDataAtualizacaoDesc(TipoMudanca.SOLO, request.getEscopo());

        for (FatorMut fm : candidatos) {
            List<DadosSolo> dsList = fm.getDadosSolo() != null ? fm.getDadosSolo() : Collections.emptyList();
            boolean existeMainEquivalente = dsList.stream().anyMatch(ds -> {
                String tipoDs = normalizeTipoFator(ds.getTipoFatorSolo());
                String usoAntDs = normalizeNoAccentsLower(ds.getUsoAnterior());
                String usoAtDs  = normalizeNoAccentsLower(ds.getUsoAtual());
                return tipoDs.equals(tipoNorm)
                        && usoAntDs.equals(usoAnt)
                        && usoAtDs.equals(usoAt)
                        && ds.getFatorCO2() == null
                        && ds.getFatorCH4() == null;
            });
            if (existeMainEquivalente) {
                // Lança DuplicacaoRegistroException contendo idExistente; handler deve serializar {codigo, mensagem, idExistente, ...}
                throw new DuplicacaoRegistroException(
                        "DADOS_DUPLICADOS",
                        "Dados duplicados detectados. Verifique se o registro já existe.",
                        fm.getId()
                );
            }
        }
    }

    // Normaliza USO_ANTERIOR_ATUAL e SOLO_USO_ANTERIOR_ATUAL como equivalentes
    private String normalizeTipoFator(TipoFatorSolo tipo) {
        String raw = tipo == null ? "" : tipo.name();
        return raw.trim().toUpperCase().replaceFirst("^SOLO_", "");
    }

    private String safeLower(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

    private MutResponse converterParaResponse(FatorMut f) {
        MutResponse r = new MutResponse();
        r.setId(f.getId());
        r.setTipoMudanca(f.getTipoMudanca());
        r.setEscopo(f.getEscopo());
        r.setAtivo(f.getAtivo());
        r.setDataCriacao(f.getDataCriacao());
        r.setDataAtualizacao(f.getDataAtualizacao());
        if (f.getUsuario() != null) r.setNomeUsuario(f.getUsuario().getNome());

        if (f.getDadosDesmatamento() != null)
            r.setDadosDesmatamento(f.getDadosDesmatamento().stream().map(this::converterDadosDesmatamento).toList());
        if (f.getDadosVegetacao() != null)
            r.setDadosVegetacao(f.getDadosVegetacao().stream().map(this::converterDadosVegetacao).toList());
        if (f.getDadosSolo() != null)
            r.setDadosSolo(f.getDadosSolo().stream().map(this::converterDadosSolo).toList());
        return r;
    }

    private MutResponse.DadosDesmatamentoResponse converterDadosDesmatamento(DadosDesmatamento d) {
        MutResponse.DadosDesmatamentoResponse r = new MutResponse.DadosDesmatamentoResponse();
        r.setId(d.getId());
        r.setBioma(d.getBioma());
        r.setValorUnico(d.getValorUnico());
        Set<UF> ufs = d.getUfs();
        if (ufs != null) ufs.size(); // força inicialização
        r.setUfs(ufs);
        r.setNomeFitofisionomia(d.getNomeFitofisionomia());
        r.setSiglaFitofisionomia(d.getSiglaFitofisionomia());
        r.setCategoriaDesmatamento(d.getCategoriaDesmatamento());
        r.setEstoqueCarbono(d.getEstoqueCarbono());
        r.setFatorCO2(d.getFatorCO2());
        r.setFatorCH4(d.getFatorCH4());
        r.setFatorN2O(d.getFatorN2O());
        r.setReplicadoAutomatico(Boolean.TRUE.equals(d.getReplicadoAutomatico()));
        return r;
    }

    private MutResponse.DadosVegetacaoResponse converterDadosVegetacao(DadosVegetacao d) {
        MutResponse.DadosVegetacaoResponse r = new MutResponse.DadosVegetacaoResponse();
        r.setId(d.getId());
        r.setBioma(d.getBioma());
        Set<CategoriaDesmatamento> categorias = d.getCategoriasFitofisionomia();
        if (categorias != null) categorias.size();
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
        r.setReplicadoAutomatico(Boolean.TRUE.equals(d.getReplicadoAutomatico()));
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
        r.setDescricao(d.getDescricao()); // agora só a Referência
        r.setUsoAnterior(d.getUsoAnterior());
        r.setUsoAtual(d.getUsoAtual());
        r.setPrincipal(Boolean.TRUE.equals(d.getPrincipal()));
        r.setReplicadoAutomatico(Boolean.TRUE.equals(d.getReplicadoAutomatico()));
        return r;
    }

    private void criarDadosSoloPadrao(FatorMut fatorMut) {
        DadosSolo ds = new DadosSolo();
        ds.setFatorMut(fatorMut);
        ds.setTipoFatorSolo(TipoFatorSolo.USO_ANTERIOR_ATUAL);
        ds.setValorFator(BigDecimal.ZERO);
        ds.setDescricao("Dados padrão");
        ds.setBioma(Bioma.AMAZONIA);
        ds.setFatorCO2(BigDecimal.ZERO);
        ds.setFatorCH4(BigDecimal.ZERO);
        ds.setFatorN2O(BigDecimal.ZERO);
        fatorMut.getDadosSolo().add(ds);
    }

    private boolean isUniqueConstraintViolation(Throwable ex) {
        if (ex == null) return false;
        String message = ex.getMessage();
        if (message != null) {
            String lowerMessage = message.toLowerCase();
            return lowerMessage.contains("unique") || 
                   lowerMessage.contains("duplicate") || 
                   lowerMessage.contains("constraint") ||
                   lowerMessage.contains("violates");
        }
        return isUniqueConstraintViolation(ex.getCause());
    }

    private void replicarCamposComunsParaOutroEscopo(FatorMut fonte) {
        try {
            if (fonte == null || fonte.getEscopo() == null || fonte.getTipoMudanca() == null) return;
            // Replicar apenas entre ESCOPO1 e ESCOPO3
            if (fonte.getEscopo() != EscopoEnum.ESCOPO1 && fonte.getEscopo() != EscopoEnum.ESCOPO3) return;

            // Cancelar replicação automática para VEGETAÇÃO
            if (fonte.getTipoMudanca() == TipoMudanca.VEGETACAO) {
                log.info("Replicação automática desativada para VEGETACAO.");
                return;
            }

            EscopoEnum destino = fonte.getEscopo() == EscopoEnum.ESCOPO1 ? EscopoEnum.ESCOPO3 : EscopoEnum.ESCOPO1;

            switch (fonte.getTipoMudanca()) {
                case SOLO -> {
                    List<DadosSolo> dsOrigList = fonte.getDadosSolo() != null ? fonte.getDadosSolo() : List.of();
                    // Considera só o "registro principal": CO2/CH4 nulos
                    DadosSolo principal = dsOrigList.stream()
                            .filter(ds -> ds.getFatorCO2() == null && ds.getFatorCH4() == null)
                            .findFirst().orElse(null);
                    if (principal == null) return;
                    String tipoNorm = normalizeTipoFator(principal.getTipoFatorSolo());
                    String usoAnt = normalizeNoAccentsLower(principal.getUsoAnterior());
                    String usoAt  = normalizeNoAccentsLower(principal.getUsoAtual());
                    if (tipoNorm.isBlank() || usoAnt.isBlank() || usoAt.isBlank()) return;

                    // Procura FatorMut destino
                    List<FatorMut> candidatos = fatorMutRepository
                            .findAllByTipoMudancaAndEscopoAndAtivoTrueOrderByDataAtualizacaoDesc(TipoMudanca.SOLO, destino);

                    FatorMut alvo = null;
                    for (FatorMut fm : candidatos) {
                        List<DadosSolo> dsList = fm.getDadosSolo() != null ? fm.getDadosSolo() : List.of();
                        boolean existsMain = dsList.stream().anyMatch(ds -> {
                            String tipoDs = normalizeTipoFator(ds.getTipoFatorSolo());
                            String usoAntDs = normalizeNoAccentsLower(ds.getUsoAnterior());
                            String usoAtDs  = normalizeNoAccentsLower(ds.getUsoAtual());
                            return tipoDs.equals(tipoNorm)
                                    && usoAntDs.equals(usoAnt)
                                    && usoAtDs.equals(usoAt)
                                    && ds.getFatorCO2() == null
                                    && ds.getFatorCH4() == null;
                        });
                        if (existsMain) {
                            // Já replicado — nada a fazer
                            return;
                        }
                        if (alvo == null) alvo = fm;
                    }
                    // Se não havia ativo, cria novo FatorMut no destino
                    if (alvo == null) {
                        alvo = new FatorMut();
                        alvo.setTipoMudanca(TipoMudanca.SOLO);
                        alvo.setEscopo(destino);
                        alvo.setAtivo(true);
                        alvo.setDadosSolo(new ArrayList<>());
                    } else if (alvo.getDadosSolo() == null) {
                        alvo.setDadosSolo(new ArrayList<>());
                    }

                    // Replica apenas os campos comuns, sem exclusivos
                    DadosSolo dsRep = new DadosSolo();
                    dsRep.setFatorMut(alvo);
                    dsRep.setTipoFatorSolo(principal.getTipoFatorSolo());
                    dsRep.setUsoAnterior(principal.getUsoAnterior());
                    dsRep.setUsoAtual(principal.getUsoAtual());
                    // Exclusivos NÃO replicados, mas garantir not-null e evitar RN008 indevida
                    dsRep.setValorFator(BigDecimal.ZERO);
                    dsRep.setDescricao("0");
                    dsRep.setBioma(null);
                    dsRep.setFatorCO2(null);
                    dsRep.setFatorCH4(null);
                    dsRep.setFatorN2O(BigDecimal.ZERO);
                    dsRep.setPrincipal(Boolean.TRUE);
                    dsRep.setReplicadoAutomatico(Boolean.TRUE);

                    alvo.getDadosSolo().add(dsRep);
                    alvo.setDataAtualizacao(LocalDateTime.now());
                    fatorMutRepository.saveAndFlush(alvo);
                }

                case DESMATAMENTO -> {
                    List<DadosDesmatamento> ddOrigList = fonte.getDadosDesmatamento() != null ? fonte.getDadosDesmatamento() : List.of();
                    if (ddOrigList.isEmpty()) return;

                    DadosDesmatamento comum = ddOrigList.get(0); // chave replicativa por escopo
                    if (comum.getBioma() == null) return;
                    Boolean valorUnico = comum.getValorUnico();
                    Set<UF> ufs = comum.getUfs();

                    List<FatorMut> candidatos = fatorMutRepository
                            .findAllByTipoMudancaAndEscopoAndAtivoTrueOrderByDataAtualizacaoDesc(TipoMudanca.DESMATAMENTO, destino);

                    FatorMut alvo = null;
                    for (FatorMut fm : candidatos) {
                        List<DadosDesmatamento> ddList = fm.getDadosDesmatamento() != null ? fm.getDadosDesmatamento() : List.of();
                        boolean existsKey = ddList.stream().anyMatch(dd -> {
                            if (dd.getBioma() != comum.getBioma()) return false;
                            if (Boolean.TRUE.equals(valorUnico)) {
                                return Boolean.TRUE.equals(dd.getValorUnico());
                            } else {
                                Set<UF> u = dd.getUfs() != null ? dd.getUfs() : Set.of();
                                Set<UF> v = ufs != null ? ufs : Set.of();
                                return u.equals(v) && !Boolean.TRUE.equals(dd.getValorUnico());
                            }
                        });
                        if (existsKey) {
                            // Já replicado — nada a fazer
                            return;
                        }
                        if (alvo == null) alvo = fm;
                    }
                    if (alvo == null) {
                        alvo = new FatorMut();
                        alvo.setTipoMudanca(TipoMudanca.DESMATAMENTO);
                        alvo.setEscopo(destino);
                        alvo.setAtivo(true);
                        alvo.setDadosDesmatamento(new ArrayList<>());
                    } else if (alvo.getDadosDesmatamento() == null) {
                        alvo.setDadosDesmatamento(new ArrayList<>());
                    }

                    DadosDesmatamento ddRep = new DadosDesmatamento();
                    ddRep.setFatorMut(alvo);
                    ddRep.setBioma(comum.getBioma());
                    ddRep.setValorUnico(valorUnico);
                    ddRep.setUfs(ufs);
                    // Exclusivos não replicados
                    ddRep.setNomeFitofisionomia(null);
                    ddRep.setSiglaFitofisionomia(null);
                    ddRep.setCategoriaDesmatamento(null);
                    ddRep.setEstoqueCarbono(null);
                    ddRep.setFatorCO2(null);
                    ddRep.setFatorCH4(null);
                    ddRep.setFatorN2O(null);

                    alvo.getDadosDesmatamento().add(ddRep);
                    alvo.setDataAtualizacao(LocalDateTime.now());
                    fatorMutRepository.saveAndFlush(alvo);
                }
            }
        } catch (Exception e) {
            // Silencioso para não bloquear a operação principal; log para rastreamento
            log.warn("Falha ao replicar campos comuns para o outro escopo: {}", e.getMessage(), e);
        }
    }


}
