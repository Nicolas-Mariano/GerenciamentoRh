<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Cadastro de Setor</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Cadastro de Setor</h1>

        <c:if test="${not empty erro}">
            <p class="msg-erro">${erro}</p>
        </c:if>

        <form action="controller.do" method="POST">
            <input type="hidden" name="acao" value="CadastrarSetor"/>

            <label>Nome do Setor:</label>
            <input type="text" name="txtNome" value="${nomeSetor}" placeholder="Ex: Recursos Humanos" required/><br/>

            <input type="submit" value="Cadastrar"/>
        </form>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>