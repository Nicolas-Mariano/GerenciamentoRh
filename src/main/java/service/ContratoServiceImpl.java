package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class ContratoServiceImpl implements IContratoService {

    private final IContratoDAO contratoDAO;

    public ContratoServiceImpl() {
        this.contratoDAO = DAOFactory.getContratoDAO();
    }

    public ContratoServiceImpl(IContratoDAO contratoDAO) {
        this.contratoDAO = contratoDAO;
    }

    @Override
    public void contratar(int idFuncionario, Contrato dadosContrato) throws Exception {
        validarDataAdmissao(dadosContrato.getDataAdmissao());
        garantirSemContratoAtivo(idFuncionario);
        dadosContrato.setMatricula(gerarMatricula());
        Funcionario func = new Funcionario();
        func.setId(idFuncionario);
        dadosContrato.setFuncionario(func);
        try {
            this.contratoDAO.cadastrar(dadosContrato);
        } catch (SQLException ex) {
            throw new Exception(traduzirErroSQL(ex));
        }
    }

    @Override
    public void demitir(int idContrato, String motivo, Date dataDemissao) throws Exception {
        if (dataDemissao != null && dataDemissao.after(new Date())) {
            throw new Exception("Regra Violada: A data de demissão não pode ser futura.");
        }
        Contrato contrato = this.contratoDAO.consultarById(idContrato);
        if (contrato == null) {
            throw new Exception("Contrato não encontrado.");
        }
        if (contrato.getDataDemissao() != null) {
            throw new Exception("Operação negada: O contrato já está encerrado.");
        }
        contrato.setDataDemissao(dataDemissao != null ? dataDemissao : new Date());
        contrato.setMotivoDesligamento(motivo);
        this.contratoDAO.atualizar(contrato);
    }

    @Override
    public void recontratar(int idFuncionario, Contrato dadosContrato) throws Exception {
        validarDataAdmissao(dadosContrato.getDataAdmissao());
        Contrato ativo = this.contratoDAO.buscarAtivo(idFuncionario);
        if (ativo != null) {
            throw new Exception("Operação negada: O funcionário ainda possui contrato ativo. Registre a demissão primeiro.");
        }

        Contrato anterior = this.contratoDAO.buscarUltimoDemitido(idFuncionario);
        if (anterior != null) {
            validarRecontratacao(dadosContrato, anterior);
        }

        dadosContrato.setMatricula(gerarMatricula());
        Funcionario func = new Funcionario();
        func.setId(idFuncionario);
        dadosContrato.setFuncionario(func);
        try {
            this.contratoDAO.cadastrar(dadosContrato);
        } catch (SQLException ex) {
            throw new Exception(traduzirErroSQL(ex));
        }
    }

    @Override
    public void aplicarAumento(int idContrato, String tipo, double valor) throws Exception {
        Contrato contrato = this.contratoDAO.consultarById(idContrato);
        if (contrato == null) {
            throw new Exception("Contrato não encontrado.");
        }
        if (contrato.getDataDemissao() != null) {
            throw new Exception("Operação negada: Não é possível aplicar aumento em contrato encerrado.");
        }
        CalculadoraSalario base = new SalarioBaseContrato();
        Map<String, CalculadoraSalario> tiposDeAumento = new HashMap<>();
        tiposDeAumento.put("PERCENTUAL", new AumentoPercentual(base, valor));
        tiposDeAumento.put("BONUS", new AumentoPorBonus(base, valor));
        CalculadoraSalario calculadora = tiposDeAumento.get(tipo.toUpperCase());
        if (calculadora == null) {
            throw new Exception("Tipo de aumento inválido. Use PERCENTUAL ou BONUS.");
        }
        double novoSalario = calculadora.calcular(contrato);
        contrato.setSalarioBase(novoSalario);
        this.contratoDAO.atualizar(contrato);
    }

    @Override
    public Contrato buscarAtivo(int idFuncionario) throws Exception {
        return this.contratoDAO.buscarAtivo(idFuncionario);
    }

    @Override
    public List<Contrato> buscarHistorico(int idFuncionario) throws Exception {
        return this.contratoDAO.buscarHistorico(idFuncionario);
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

    private void garantirSemContratoAtivo(int idFuncionario) throws Exception {
        Contrato ativo = this.contratoDAO.buscarAtivo(idFuncionario);
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
