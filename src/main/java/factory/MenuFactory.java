package factory;

import command.*;

public class MenuFactory {

    public static ComandoTerminal obterComando(int opcaoEscolhida) {
        switch (opcaoEscolhida) {
            case 1:
                return new CadastrarSetorCommand();
            case 2:
                return new ListarSetoresCommand();
            case 3:
                return new AtualizarGerenteSetorCommand();
            case 4:
                return new ExcluirSetorCommand();
            case 5:
                return new FluxoContratacaoCommand();
            case 6:
                return new ListarFuncionariosCommand();
            case 7:
                return new ExcluirFuncionarioCommand();
            case 8:
                return new ListarEnderecosCommand();
            case 9:
                return new AplicarAumentoCommand();
            default:
                return null;
        }
    }
}