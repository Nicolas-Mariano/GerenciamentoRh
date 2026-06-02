package service;

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
        this.contratoDAO.cadastrar(dadosContrato);
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
        dadosContrato.setMatricula(gerarMatricula());
        Funcionario func = new Funcionario();
        func.setId(idFuncionario);
        dadosContrato.setFuncionario(func);
        this.contratoDAO.cadastrar(dadosContrato);
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
}
