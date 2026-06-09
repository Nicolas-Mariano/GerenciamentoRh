package dao;

import java.sql.Connection;
import java.util.List;
import model.Contrato;
import model.Funcionario;
import model.Setor;

public interface IContratoDAO {
    void cadastrar(Connection con, Contrato c) throws Exception;
    void cadastrar(Contrato c) throws Exception;
    void atualizar(Contrato c) throws Exception;
    void atualizarPromocao(Contrato c) throws Exception;
    Contrato consultarById(int id) throws Exception;
    Contrato buscarAtivo(Funcionario funcionario) throws Exception;
    List<Contrato> buscarHistorico(Funcionario funcionario) throws Exception;
    List<Contrato> buscarAtivosPorSetor(Setor setor) throws Exception;
    Contrato buscarUltimoDemitido(Funcionario funcionario) throws Exception;
}
