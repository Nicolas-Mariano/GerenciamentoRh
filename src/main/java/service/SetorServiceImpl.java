package service;

import dao.DAOFactory;
import dao.ISetorDAO;
import model.Contrato;
import model.NivelSenioridade;
import model.Setor;

public class SetorServiceImpl implements ISetorService {

    @Override
    public void vincularGerente(int idSetor, int idContrato) throws Exception {
        Contrato contrato = DAOFactory.getContratoDAO().consultarById(idContrato);
        if (contrato == null) {
            throw new Exception("Contrato não encontrado.");
        }
        if (contrato.getDataDemissao() != null) {
            throw new Exception("Operação negada: O contrato está encerrado.");
        }
        if (contrato.getSetor() == null || contrato.getSetor().getId() != idSetor) {
            throw new Exception("Operação negada: O contrato não pertence ao setor selecionado.");
        }
        NivelSenioridade nivel = contrato.getNivelSenioridade();
        if (nivel != NivelSenioridade.PLENO && nivel != NivelSenioridade.SENIOR) {
            throw new Exception("Regra Violada: Apenas contratos de nível Pleno ou Senior podem assumir a gerência.");
        }
        ISetorDAO dao = DAOFactory.getSetorDAO();
        Setor setor = dao.consultarById(idSetor);
        setor.setContratoResponsavel(contrato);
        dao.atualizar(setor);
    }
}
