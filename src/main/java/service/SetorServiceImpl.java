package service;

import dao.DAOFactory;
import dao.ISetorDAO;
import model.Contrato;
import model.Setor;

public class SetorServiceImpl implements ISetorService {

    @Override
    public void vincularGerente(Setor setor, Contrato contrato) throws Exception {
        Contrato contratoCompleto = DAOFactory.getContratoDAO().consultarById(contrato.getId());
        if (contratoCompleto == null) {
            throw new Exception("Contrato não encontrado.");
        }
        if (contratoCompleto.getDataDemissao() != null) {
            throw new Exception("Operação negada: O contrato está encerrado.");
        }
        if (contratoCompleto.getSetor() == null || contratoCompleto.getSetor().getId() != setor.getId()) {
            throw new Exception("Operação negada: O contrato não pertence ao setor selecionado.");
        }
        if (!contratoCompleto.getNivelSenioridade().podeSerGerente()) {
            throw new Exception("Regra Violada: Apenas contratos de nível Pleno ou Senior podem assumir a gerência.");
        }
        ISetorDAO dao = DAOFactory.getSetorDAO();
        Setor setorCompleto = dao.consultarById(setor.getId());
        setorCompleto.setContratoResponsavel(contratoCompleto);
        dao.atualizar(setorCompleto);
    }
}
