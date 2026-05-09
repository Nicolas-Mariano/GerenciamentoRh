package dao;

import java.sql.Connection;
import java.util.List;
import model.Funcionario;

public interface IFuncionarioDAO {
    void cadastrar(Connection con, Funcionario f) throws Exception;
    void cadastrar(Funcionario f) throws Exception;
    void deletar(Connection con, int idFuncionario) throws Exception;
    void deletar(int idFuncionario) throws Exception;
    void deletar(Funcionario f) throws Exception;
    void registrarDemissao(int idFuncionario, java.util.Date dataDemissao) throws Exception;
    void atualizar(Funcionario f) throws Exception;
    Funcionario consultarById(int id) throws Exception;
    List<Funcionario> consultarTodos() throws Exception;
}
