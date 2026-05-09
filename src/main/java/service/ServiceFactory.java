package service;

public class ServiceFactory {
    public static IFuncionarioService getFuncionarioService() {
        return new FuncionarioServiceImpl();
    }

    public static ISetorService getSetorService() {
        return new SetorServiceImpl();
    }
}
