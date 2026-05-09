package service;

import java.util.Date;
import model.Endereco;
import model.Funcionario;

public interface IFuncionarioService {
    void cadastrar(Funcionario f, Endereco e, int idSetor) throws Exception;
    void aplicarAumento(int idFuncionario, double percentual) throws Exception;
    void demitirFuncionario(int idFuncionario, Date dataDemissao) throws Exception;
}
