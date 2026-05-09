package service;

import dao.DAOFactory;
import dao.ISetorDAO;
import model.Funcionario;
import model.Setor;

public class SetorServiceImpl implements ISetorService {

    @Override
    public void vincularGerente(int idSetor, int idFuncionario) throws Exception {
        Funcionario f = DAOFactory.getFuncionarioDAO().consultarById(idFuncionario);
        if (f.getSetor() == null || f.getSetor().getId() != idSetor) {
            throw new Exception("Operação negada: O funcionário não pertence ao setor selecionado.");
        }
        
        if (!"Pleno".equalsIgnoreCase(f.getNivel()) && !"Senior".equalsIgnoreCase(f.getNivel())) {
            throw new Exception("Regra Violada: Apenas funcionários de nível Pleno ou Senior podem assumir a gerência.");
        }

        ISetorDAO dao = DAOFactory.getSetorDAO();
        Setor setor = dao.consultarById(idSetor);
        setor.setFuncResponsavel(f);
        
        dao.atualizar(setor);
    }
}
