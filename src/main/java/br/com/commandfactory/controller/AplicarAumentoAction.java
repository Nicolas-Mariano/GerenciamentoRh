package br.com.commandfactory.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @deprecated Substituído por {@link AplicarPromocaoAction}.
 * Mantido apenas para compatibilidade reversa. Redireciona para a nova ação.
 */
public class AplicarAumentoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new AplicarPromocaoAction().executar(request, response);
    }
}
