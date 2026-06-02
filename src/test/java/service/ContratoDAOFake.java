package service;

import dao.IContratoDAO;
import model.Contrato;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAOFake implements IContratoDAO {

    private Contrato contratoAtivo;
    private Contrato contratoPorId;
    private Contrato ultimoDemitido;
    public Contrato ultimoAtualizado;
    public Contrato ultimoCadastrado;

    public void setContratoAtivo(Contrato contrato) { this.contratoAtivo = contrato; }
    public void setContratoPorId(Contrato contrato) { this.contratoPorId = contrato; }
    public void setUltimoDemitido(Contrato contrato) { this.ultimoDemitido = contrato; }

    @Override
    public void cadastrar(Connection con, Contrato c) throws Exception { this.ultimoCadastrado = c; }

    @Override
    public void cadastrar(Contrato c) throws Exception { this.ultimoCadastrado = c; }

    @Override
    public void atualizar(Contrato c) throws Exception { this.ultimoAtualizado = c; }

    @Override
    public Contrato consultarById(int id) throws Exception { return contratoPorId; }

    @Override
    public Contrato buscarAtivo(int idFuncionario) throws Exception { return contratoAtivo; }

    @Override
    public List<Contrato> buscarHistorico(int idFuncionario) throws Exception { return new ArrayList<>(); }

    @Override
    public List<Contrato> buscarAtivosPorSetor(int idSetor) throws Exception { return new ArrayList<>(); }

    @Override
    public Contrato buscarUltimoDemitido(int idFuncionario) throws Exception { return ultimoDemitido; }
}
