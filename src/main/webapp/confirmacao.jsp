
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
            <form action="controller.do" method="POST" style="box-shadow: none; border: none; width: 100%; display: flex; justify-content: center; gap: 15px;">
                <input type="hidden" name="acao" value="${acaoDestino}"/>
                <input type="hidden" name="id" value="${idItem}"/>
                
                <a href="controller.do?acao=${acaoVoltar}&id=${idItem}" class="btn btn-editar">Não, Voltar</a>
                <input type="submit" class="btn-deletar" style="padding: 8px 16px; border: none; color: white; border-radius: 4px; cursor: pointer;" value="Sim, Confirmar"/>
            </form>
        </div>
    </body>
</html>