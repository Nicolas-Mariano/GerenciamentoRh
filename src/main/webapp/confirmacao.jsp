
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Confirmação</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Atenção</h1>
        <div class="card" style="border-color: #cc0000; text-align: center;">
            <h2 style="color: #cc0000; font-size: 20px;">${mensagem}</h2>
            <br/>
            <form action="controller.do" method="POST" style="box-shadow: none; border: none; width: 100%; display: flex; flex-wrap: wrap; justify-content: center; gap: 15px;">
                <input type="hidden" name="acao" value="${acaoDestino}"/>
                <input type="hidden" name="id" value="${idItem}"/>
                <c:if test="${not empty idContrato}">
                    <input type="hidden" name="idContrato" value="${idContrato}"/>
                </c:if>

                <c:if test="${acaoDestino == 'DemitirFuncionario'}">
                    <div style="width: 100%; text-align: left; margin-bottom: 15px;">
                        <label for="motivoDesligamento" style="font-weight: bold; font-size: 14px; color: #333; margin-bottom: 4px; display: block;">
                            Motivo da Demissão: <span style="color: #cc0000;">*</span>
                        </label>
                        <textarea id="motivoDesligamento" name="motivoDesligamento"
                                  rows="3" required
                                  placeholder="Informe o motivo da demissão..."
                                  style="width: 100%; padding: 7px; border: 1px solid #aaa; font-size: 14px; box-sizing: border-box; resize: vertical; font-family: Arial, sans-serif;"></textarea>
                    </div>
                </c:if>

                <a href="controller.do?acao=${acaoVoltar}&id=${idItem}" class="btn btn-editar">Não, Voltar</a>
                <input type="submit" class="btn-deletar" style="padding: 8px 16px; border: none; color: white; border-radius: 4px; cursor: pointer;" value="Sim, Confirmar"/>
            </form>
        </div>
    </body>
</html>