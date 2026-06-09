package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import dao.DAOFactory;
import dao.IContratoDAO;
import model.Contrato;
import model.Funcionario;
import model.NivelSenioridade;
import salary.AumentoPorBonus;
import salary.AumentoPercentual;
import salary.CalculadoraSalario;
import salary.SalarioBaseContrato;
import salary.TipoAumento;

public class ContratoServiceImpl implements IContratoService {

    private final IContratoDAO contratoDAO;

    public ContratoServiceImpl() {
        this.contratoDAO = DAOFactory.getContratoDAO();
    }

    public ContratoServiceImpl(IContratoDAO contratoDAO) {
        this.contratoDAO = contratoDAO;
    }

    @Override
    public void contratar(Funcionario funcionario, Contrato dadosContrato) throws Exception {
        validarDataAdmissao(dadosContrato.getDataAdmissao());
        garantirSemContratoAtivo(funcionario);
        dadosContrato.setMatricula(gerarMatricula());
        dadosContrato.setFuncionario(funcionario);
        try {
            this.contratoDAO.cadastrar(dadosContrato);
        } catch (SQLException ex) {
            throw new Exception(traduzirErroSQL(ex));
        }
    }

    @Override
    public void demitir(Contrato contrato, String motivo, Date dataDemissao) throws Exception {
        if (dataDemissao != null && dataDemissao.after(new Date())) {
            throw new Exception("Regra Violada: A data de demissão não pode ser futura.");
        }
        Contrato contratoCompleto = this.contratoDAO.consultarById(contrato.getId());
        if (contratoCompleto == null) {
            throw new Exception("Contrato não encontrado.");
        }
        if (contratoCompleto.getDataDemissao() != null) {
            throw new Exception("Operação negada: O contrato já está encerrado.");
        }
        contratoCompleto.setDataDemissao(dataDemissao != null ? dataDemissao : new Date());
        contratoCompleto.setMotivoDesligamento(motivo);
        this.contratoDAO.atualizar(contratoCompleto);
    }

    @Override
    public void recontratar(Funcionario funcionario, Contrato dadosContrato) throws Exception {
        validarDataAdmissao(dadosContrato.getDataAdmissao());
        Contrato ativo = this.contratoDAO.buscarAtivo(funcionario);
        if (ativo != null) {
            throw new Exception("Operação negada: O funcionário ainda possui contrato ativo. Registre a demissão primeiro.");
        }

        Contrato anterior = this.contratoDAO.buscarUltimoDemitido(funcionario);
        if (anterior != null) {
            validarRecontratacao(dadosContrato, anterior);
        }

        dadosContrato.setMatricula(gerarMatricula());
        dadosContrato.setFuncionario(funcionario);
        try {
            this.contratoDAO.cadastrar(dadosContrato);
        } catch (SQLException ex) {
            throw new Exception(traduzirErroSQL(ex));
        }
    }

    @Override
    public void aplicarPromocao(Contrato contrato, NivelSenioridade novoNivel, TipoAumento tipoAumento, double valor) throws Exception {
        Contrato contratoCompleto = this.contratoDAO.consultarById(contrato.getId());
        if (contratoCompleto == null) {
            throw new Exception("Contrato não encontrado.");
        }
        if (contratoCompleto.getDataDemissao() != null) {
            throw new Exception("Operação negada: Não é possível aplicar promoção em contrato encerrado.");
        }

        NivelSenioridade nivelAtual = contratoCompleto.getNivelSenioridade();
        if (!nivelAtual.ehInferiorA(novoNivel)) {
            throw new Exception("Promoção negada: O novo nível (" + novoNivel.getRotulo()
                + ") deve ser estritamente superior ao atual (" + nivelAtual.getRotulo() + ").");
        }

        CalculadoraSalario base = new SalarioBaseContrato();
        CalculadoraSalario calculadora = switch (tipoAumento) {
            case PERCENTUAL -> new AumentoPercentual(base, valor);
            case BONUS      -> new AumentoPorBonus(base, valor);
        };

        double novoSalario = calculadora.calcular(contratoCompleto);
        contratoCompleto.setNivelSenioridade(novoNivel);
        contratoCompleto.setSalarioBase(novoSalario);
        this.contratoDAO.atualizarPromocao(contratoCompleto);
    }

    @Override
    public Contrato buscarAtivo(Funcionario funcionario) throws Exception {
        return this.contratoDAO.buscarAtivo(funcionario);
    }

    @Override
    public List<Contrato> buscarHistorico(Funcionario funcionario) throws Exception {
        return this.contratoDAO.buscarHistorico(funcionario);
    }

    private void validarRecontratacao(Contrato novo, Contrato anterior) throws Exception {
        if (novo.getNivelSenioridade().ehInferiorA(anterior.getNivelSenioridade())) {
            throw new Exception("Recontratação negada: O nível/cargo ("
                + novo.getNivelSenioridade() + ") não pode ser inferior ao anterior ("
                + anterior.getNivelSenioridade() + ").");
        }

        double salarioMinimoExigido = anterior.getSalarioBase() * 1.10;
        if (novo.getSalarioBase() < salarioMinimoExigido) {
            throw new Exception("Recontratação negada: O salário (R$ "
                + String.format("%.2f", novo.getSalarioBase())
                + ") deve ser ao menos 10% superior ao anterior (R$ "
                + String.format("%.2f", anterior.getSalarioBase())
                + "). Mínimo exigido: R$ "
                + String.format("%.2f", salarioMinimoExigido) + ".");
        }
    }

    private void validarDataAdmissao(Date data) throws Exception {
        if (data == null || data.after(new Date())) {
            throw new Exception("Regra violada: A data de admissão não pode ser futura.");
        }
    }

    private void garantirSemContratoAtivo(Funcionario funcionario) throws Exception {
        Contrato ativo = this.contratoDAO.buscarAtivo(funcionario);
        if (ativo != null) {
            throw new Exception("Operação negada: O funcionário já possui um contrato ativo.");
        }
    }

    private String gerarMatricula() {
        return LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    private String traduzirErroSQL(SQLException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        if (msg.contains("contrato_nivel_senioridade_check")) {
            return "O nível de senioridade informado não é válido. Valores aceitos: Jovem Aprendiz, Estagiário, Junior, Pleno, Senior.";
        }
        if (msg.contains("contrato_matricula_key") || msg.contains("unique")) {
            return "Erro interno: conflito de matrícula gerada. Tente novamente.";
        }
        if (msg.contains("chk_contrato_admissao")) {
            return "A data de admissão não pode ser posterior à data atual.";
        }
        if (msg.contains("id_setor") && msg.contains("foreign key")) {
            return "O setor selecionado não existe no sistema.";
        }
        if (msg.contains("id_funcionario") && msg.contains("foreign key")) {
            return "O funcionário informado não existe no sistema.";
        }
        return "Erro ao salvar contrato no banco de dados. Verifique os dados informados e tente novamente.";
    }
}
