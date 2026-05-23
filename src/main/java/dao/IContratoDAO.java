package dao;

import java.sql.Connection;
import java.util.List;
import model.Contrato;

public interface IContratoDAO {
    void cadastrar(Connection con, Contrato c) throws Exception;
    void cadastrar(Contrato c) throws Exception;
    void atualizar(Contrato c) throws Exception;
    Contrato consultarById(int id) throws Exception;
    Contrato buscarAtivo(int idFuncionario) throws Exception;
    List<Contrato> buscarHistorico(int idFuncionario) throws Exception;
    List<Contrato> buscarAtivosPorSetor(int idSetor) throws Exception;
}
