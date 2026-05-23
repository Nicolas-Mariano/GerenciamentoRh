package service;

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

public class ContratoServiceImpl implements IContratoService {

    @Override
    public void contratar(int idFuncionario, Contrato dadosContrato) throws Exception {
        if (dadosContrato.getDataAdmissao() == null || dadosContrato.getDataAdmissao().after(new Date())) {
            throw new Exception("Regra violada: A data de admissão não pode ser futura.");
        }
        Contrato ativo = DAOFactory.getContratoDAO().buscarAtivo(idFuncionario);
        if (ativo != null) {
            throw new Exception("Operação negada: O funcionário já possui um contrato ativo.");
        }
        dadosContrato.setMatricula(gerarMatricula());
        Funcionario func = new Funcionario();
        func.setId(idFuncionario);
        dadosContrato.setFuncionario(func);
        DAOFactory.getContratoDAO().cadastrar(dadosContrato);
    }

    @Override
    public void demitir(int idContrato, String motivo, Date dataDemissao) throws Exception {
        if (dataDemissao != null && dataDemissao.after(new Date())) {
            throw new Exception("Regra Violada: A data de demissão não pode ser futura.");
        }
        IContratoDAO dao = DAOFactory.getContratoDAO();
        Contrato contrato = dao.consultarById(idContrato);
        if (contrato == null) {
            throw new Exception("Contrato não encontrado.");
        }
        if (contrato.getDataDemissao() != null) {
            throw new Exception("Operação negada: O contrato já está encerrado.");
        }
        contrato.setDataDemissao(dataDemissao != null ? dataDemissao : new Date());
        contrato.setMotivoDesligamento(motivo);
        dao.atualizar(contrato);
    }

    @Override
    public void recontratar(int idFuncionario, Contrato dadosContrato) throws Exception {
        if (dadosContrato.getDataAdmissao() == null || dadosContrato.getDataAdmissao().after(new Date())) {
            throw new Exception("Regra violada: A data de admissão não pode ser futura.");
        }
        Contrato ativo = DAOFactory.getContratoDAO().buscarAtivo(idFuncionario);
        if (ativo != null) {
            throw new Exception("Operação negada: O funcionário ainda possui contrato ativo. Registre a demissão primeiro.");
        }
        dadosContrato.setMatricula(gerarMatricula());
        Funcionario func = new Funcionario();
        func.setId(idFuncionario);
        dadosContrato.setFuncionario(func);
        DAOFactory.getContratoDAO().cadastrar(dadosContrato);
    }

    @Override
    public void aplicarAumento(int idContrato, String tipo, double valor) throws Exception {
        IContratoDAO dao = DAOFactory.getContratoDAO();
        Contrato contrato = dao.consultarById(idContrato);
        if (contrato == null) {
            throw new Exception("Contrato não encontrado.");
        }
        if (contrato.getDataDemissao() != null) {
            throw new Exception("Operação negada: Não é possível aplicar aumento em contrato encerrado.");
        }
        CalculadoraSalario calculadora = new SalarioBaseContrato();
        if ("PERCENTUAL".equalsIgnoreCase(tipo)) {
            calculadora = new AumentoPercentual(calculadora, valor);
        } else if ("BONUS".equalsIgnoreCase(tipo)) {
            calculadora = new AumentoPorBonus(calculadora, valor);
        } else {
            throw new Exception("Tipo de aumento inválido. Use PERCENTUAL ou BONUS.");
        }
        double novoSalario = calculadora.calcular(contrato);
        contrato.setSalarioBase(novoSalario);
        dao.atualizar(contrato);
    }

    @Override
    public Contrato buscarAtivo(int idFuncionario) throws Exception {
        return DAOFactory.getContratoDAO().buscarAtivo(idFuncionario);
    }

    @Override
    public List<Contrato> buscarHistorico(int idFuncionario) throws Exception {
        return DAOFactory.getContratoDAO().buscarHistorico(idFuncionario);
    }

    private String gerarMatricula() {
        return LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}
