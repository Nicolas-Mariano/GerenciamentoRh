package service;

import java.util.Date;
import java.util.List;
import model.Contrato;

public interface IContratoService {
    void contratar(int idFuncionario, Contrato dadosContrato) throws Exception;
    void demitir(int idContrato, String motivo, Date dataDemissao) throws Exception;
    void recontratar(int idFuncionario, Contrato dadosContrato) throws Exception;
    void aplicarAumento(int idContrato, String tipo, double valor) throws Exception;
    Contrato buscarAtivo(int idFuncionario) throws Exception;
    List<Contrato> buscarHistorico(int idFuncionario) throws Exception;
}
