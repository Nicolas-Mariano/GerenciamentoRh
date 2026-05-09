package dao;

public class DAOFactory {
    public static IFuncionarioDAO getFuncionarioDAO() {
        return new FuncionarioDAO();
    }
    
    public static ISetorDAO getSetorDAO() {
        return new SetorDAO();
    }
    
    public static IEnderecoDAO getEnderecoDAO() {
        return new EnderecoDAO();
    }
}
