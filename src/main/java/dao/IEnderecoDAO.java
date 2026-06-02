package dao;

import java.sql.Connection;
import java.util.List;
import model.Endereco;

public interface IEnderecoDAO {
    int cadastrarRetornandoId(Connection con, Endereco e) throws Exception;
    int cadastrarRetornandoId(Endereco e) throws Exception;
    void deletar(Connection con, int idEndereco) throws Exception;
    void atualizar(Endereco e) throws Exception;
    Endereco consultarById(int id) throws Exception;
    List<Endereco> consultarTodos() throws Exception;
    List<Endereco> consultarSomenteAtivos() throws Exception;
}
