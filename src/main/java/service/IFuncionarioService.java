package service;

import model.Endereco;
import model.Funcionario;

public interface IFuncionarioService {
    void cadastrar(Funcionario f, Endereco e) throws Exception;
    void atualizar(Funcionario f, Endereco e) throws Exception;
}
