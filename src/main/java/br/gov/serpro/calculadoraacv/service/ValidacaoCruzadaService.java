package br.gov.serpro.calculadoraacv.service;

import br.gov.serpro.calculadoraacv.model.Certificadora;
import br.gov.serpro.calculadoraacv.model.Industria;
import br.gov.serpro.calculadoraacv.model.UsuarioCertificadora;
import br.gov.serpro.calculadoraacv.model.UsuarioIndustria;
import br.gov.serpro.calculadoraacv.repository.CertificadoraRepository;
import br.gov.serpro.calculadoraacv.repository.IndustriaRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioCertificadoraRepository;
import br.gov.serpro.calculadoraacv.repository.UsuarioIndustriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidacaoCruzadaService {

    private final CertificadoraRepository certificadoraRepository;
    private final IndustriaRepository industriaRepository;
    private final UsuarioCertificadoraRepository usuarioCertificadoraRepository;
    private final UsuarioIndustriaRepository usuarioIndustriaRepository;

    public boolean verificarCnpjExisteEmCertificadora(String cnpj) {
        return certificadoraRepository.existsByCnpj(cnpj);
    }

    public boolean verificarCnpjExisteEmIndustria(String cnpj) {
        return industriaRepository.existsByCnpj(cnpj);
    }

    public boolean verificarCpfExisteEmCertificadora(String cpf) {
        List<UsuarioCertificadora> usuariosCertificadora = usuarioCertificadoraRepository.findByUsuarioCpfAndAtivo(cpf, true);
        return !usuariosCertificadora.isEmpty();
    }

    public boolean verificarCpfExisteEmIndustria(String cpf) {
        List<UsuarioIndustria> usuariosIndustria = usuarioIndustriaRepository.findByUsuarioCpfAndAtivo(cpf, true);
        return !usuariosIndustria.isEmpty();
    }

    public String buscarNomeCertificadoraPorCpf(String cpf) {
        List<UsuarioCertificadora> usuariosCertificadora = usuarioCertificadoraRepository.findByUsuarioCpfAndAtivo(cpf, true);
        return usuariosCertificadora.stream().findFirst()
                .map(uc -> uc.getCertificadora().getNome())
                .orElse(null);
    }

    public String buscarNomeIndustriaPorCpf(String cpf) {
        List<UsuarioIndustria> usuariosIndustria = usuarioIndustriaRepository.findByUsuarioCpfAndAtivo(cpf, true);
        return usuariosIndustria.stream().findFirst()
                .map(ui -> ui.getIndustria().getNome())
                .orElse(null);
    }

    public Long buscarIdCertificadoraPorCpf(String cpf) {
        List<UsuarioCertificadora> usuariosCertificadora = usuarioCertificadoraRepository.findByUsuarioCpfAndAtivo(cpf, true);
        return usuariosCertificadora.stream().findFirst()
                .map(uc -> uc.getCertificadora().getId())
                .orElse(null);
    }

    public Long buscarIdIndustriaPorCpf(String cpf) {
        List<UsuarioIndustria> usuariosIndustria = usuarioIndustriaRepository.findByUsuarioCpfAndAtivo(cpf, true);
        return usuariosIndustria.stream().findFirst()
                .map(ui -> ui.getIndustria().getId())
                .orElse(null);
    }
}