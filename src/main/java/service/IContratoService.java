package service;

import java.util.Date;
import java.util.List;
import model.Contrato;
import model.Funcionario;
import model.NivelSenioridade;
import salary.TipoAumento;

public interface IContratoService {
    void contratar(Funcionario funcionario, Contrato dadosContrato) throws Exception;
    void demitir(Contrato contrato, String motivo, Date dataDemissao) throws Exception;
    void recontratar(Funcionario funcionario, Contrato dadosContrato) throws Exception;
    void aplicarPromocao(Contrato contrato, NivelSenioridade novoNivel, TipoAumento tipoAumento, double valor) throws Exception;
    Contrato buscarAtivo(Funcionario funcionario) throws Exception;
    List<Contrato> buscarHistorico(Funcionario funcionario) throws Exception;
}
