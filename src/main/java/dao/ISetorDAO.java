package dao;

import java.util.List;
import model.Setor;

public interface ISetorDAO {
    void cadastrar(Setor setor) throws Exception;
    void deletar(Setor setor) throws Exception;
    void atualizar(Setor setor) throws Exception;
    Setor consultarById(int id) throws Exception;
    List<Setor> consultarTodos() throws Exception;
}
