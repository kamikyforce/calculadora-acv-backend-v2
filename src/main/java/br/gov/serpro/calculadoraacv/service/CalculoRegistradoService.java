package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.dto.CalculoRegistradoRequest;
import br.gov.serpro.calculadoraacv.dto.CalculoRegistradoResponse;
import br.gov.serpro.calculadoraacv.enums.StatusCalculoRegistrado;
import br.gov.serpro.calculadoraacv.enums.TipoCertificacao;
import br.gov.serpro.calculadoraacv.exception.EntidadeNaoEncontradaException;
import br.gov.serpro.calculadoraacv.exception.ValidacaoException;
import br.gov.serpro.calculadoraacv.model.CalculoRegistrado;
import br.gov.serpro.calculadoraacv.repository.CalculoRegistradoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CalculoRegistradoService {

    private final CalculoRegistradoRepository calculoRegistradoRepository;

    public List<CalculoRegistradoResponse> listarTodos() {
        return calculoRegistradoRepository.findByAtivoTrue()
                .stream()
                .map(CalculoRegistradoResponse::new)
                .collect(Collectors.toList());
    }

    public Page<CalculoRegistradoResponse> listarComPaginacao(Pageable pageable) {
        return calculoRegistradoRepository.findByAtivoTrue(pageable)
                .map(CalculoRegistradoResponse::new);
    }

    public Page<CalculoRegistradoResponse> listarComFiltros(
            String car, String fazenda, StatusCalculoRegistrado status,
            TipoCertificacao certificacao, String estado, Integer ano,
            Pageable pageable) {
        
        return calculoRegistradoRepository.findByFiltros(
                car, fazenda, status, certificacao, estado, ano, pageable)
                .map(CalculoRegistradoResponse::new);
    }

    public CalculoRegistradoResponse buscarPorId(Long id) {
        CalculoRegistrado calculo = calculoRegistradoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cálculo Registrado", id));
        return new CalculoRegistradoResponse(calculo);
    }

    public List<CalculoRegistradoResponse> buscarPorCar(String car) {
        return calculoRegistradoRepository.findByCarAndAtivoTrue(car)
                .stream()
                .map(CalculoRegistradoResponse::new)
                .collect(Collectors.toList());
    }

    public List<CalculoRegistradoResponse> buscarPorFazenda(String fazenda) {
        return calculoRegistradoRepository.findByFazendaContainingIgnoreCaseAndAtivoTrue(fazenda)
                .stream()
                .map(CalculoRegistradoResponse::new)
                .collect(Collectors.toList());
    }

    public List<CalculoRegistradoResponse> buscarPorStatus(StatusCalculoRegistrado status) {
        return calculoRegistradoRepository.findByStatusAndAtivoTrue(status)
                .stream()
                .map(CalculoRegistradoResponse::new)
                .collect(Collectors.toList());
    }

    public List<CalculoRegistradoResponse> buscarPorCertificacao(TipoCertificacao certificacao) {
        return calculoRegistradoRepository.findByCertificacaoAndAtivoTrue(certificacao)
                .stream()
                .map(CalculoRegistradoResponse::new)
                .collect(Collectors.toList());
    }

    public CalculoRegistradoResponse criar(CalculoRegistradoRequest request, Long usuarioId) {
        validarRequest(request);
        
        CalculoRegistrado calculo = new CalculoRegistrado();
        mapearRequestParaEntidade(request, calculo);
        calculo.setUsuarioId(usuarioId);
        calculo.setAtivo(true);
        
        CalculoRegistrado calculoSalvo = calculoRegistradoRepository.save(calculo);
        return new CalculoRegistradoResponse(calculoSalvo);
    }

    public CalculoRegistradoResponse atualizar(Long id, CalculoRegistradoRequest request, Long usuarioId) {
        validarRequest(request);
        
        CalculoRegistrado calculo = calculoRegistradoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cálculo Registrado", id));
        
        // Verificar se o CAR não está sendo usado por outro cálculo
        if (!calculo.getCar().equals(request.getCar())) {
            Optional<CalculoRegistrado> calculoExistente = calculoRegistradoRepository
                    .findByCarAndIdNotAndAtivoTrue(request.getCar(), id);
            if (calculoExistente.isPresent()) {
                throw new ValidacaoException("CAR já está sendo usado por outro cálculo");
            }
        }
        
        mapearRequestParaEntidade(request, calculo);
        calculo.setDataAtualizacao(LocalDateTime.now());
        
        CalculoRegistrado calculoAtualizado = calculoRegistradoRepository.save(calculo);
        return new CalculoRegistradoResponse(calculoAtualizado);
    }

    public void excluir(Long id) {
        CalculoRegistrado calculo = calculoRegistradoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cálculo Registrado", id));
        
        calculo.setAtivo(false);
        calculo.setDataAtualizacao(LocalDateTime.now());
        calculoRegistradoRepository.save(calculo);
    }

    public Long contarPorStatus(StatusCalculoRegistrado status) {
        return calculoRegistradoRepository.countByStatusAndAtivoTrue(status);
    }

    public Long contarPorCertificacao(TipoCertificacao certificacao) {
        return calculoRegistradoRepository.countByCertificacaoAndAtivoTrue(certificacao);
    }

    private void validarRequest(CalculoRegistradoRequest request) {
        if (request.getCar() == null || request.getCar().trim().isEmpty()) {
            throw new ValidacaoException("CAR é obrigatório");
        }
        
        if (request.getFazenda() == null || request.getFazenda().trim().isEmpty()) {
            throw new ValidacaoException("Nome da fazenda é obrigatório");
        }
        
        if (request.getAno() == null || request.getAno() < 2000) {
            throw new ValidacaoException("Ano deve ser informado e maior que 2000");
        }
        
        if (request.getStatus() == null) {
            throw new ValidacaoException("Status é obrigatório");
        }
        
        if (request.getCertificacao() == null) {
            throw new ValidacaoException("Tipo de certificação é obrigatório");
        }
    }

    private void mapearRequestParaEntidade(CalculoRegistradoRequest request, CalculoRegistrado calculo) {
        calculo.setCar(request.getCar().trim());
        calculo.setFazenda(request.getFazenda().trim());
        calculo.setTipo(request.getTipo() != null ? request.getTipo().trim() : null);
        calculo.setEstado(request.getEstado() != null ? request.getEstado().toUpperCase() : null);
        calculo.setMunicipio(request.getMunicipio() != null ? request.getMunicipio().trim() : null);
        calculo.setTamanho(request.getTamanho());
        calculo.setAno(request.getAno());
        calculo.setVersao(request.getVersao() != null ? request.getVersao().trim() : null);
        calculo.setStatus(request.getStatus());
        calculo.setEmissaoTotal(request.getEmissaoTotal());
        calculo.setCertificacao(request.getCertificacao());
    }
    
    /**
     * Verifica se já existe uma fazenda com o mesmo nome e ano para o usuário
     */
    public boolean verificarFazendaExistente(String fazenda, Integer ano, Long usuarioId, Long idExcluir) {
        if (fazenda == null || fazenda.trim().isEmpty()) {
            return false;
        }
        
        if (idExcluir != null) {
            return calculoRegistradoRepository.existsByFazendaAndAnoIgnoreCaseAndAccentsAndUsuarioIdExcludingId(fazenda.trim(), ano, usuarioId, idExcluir);
        } else {
            return calculoRegistradoRepository.existsByFazendaAndAnoIgnoreCaseAndAccentsAndUsuarioId(fazenda.trim(), ano, usuarioId);
        }
    }
}