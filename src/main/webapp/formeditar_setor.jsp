<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Editar Setor</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Editar Setor</h1>

        <c:if test="${not empty erro}">
            <p class="msg-erro">${erro}</p>
        </c:if>

        <form action="controller.do" method="POST">
            <input type="hidden" name="acao" value="AtualizarSetor"/>
            <input type="hidden" name="txtId" value="${setor.id}"/>

            <label>Nome do Setor:</label>
            <input type="text" name="txtNome" value="${setor.nome}" required/><br/>

            <label>Gerente/Responsável (Apenas Pleno/Senior do Setor):</label>
            <select name="txtIdGerente">
                <option value="">-- Sem Gerente --</option>
                <c:forEach var="f" items="${funcionarios}">
                    <option value="${f.id}" <c:if test="${setor.idFuncResponsavel == f.id}">selected</c:if>>
                        ${f.nome} (${f.nivel})
                    </option>
                </c:forEach>
                
                <c:if test="${empty funcionarios}">
                    <option value="" disabled>Nenhum funcionário Pleno ou Senior neste setor</option>
                </c:if>
            </select><br/>

            <input type="submit" value="Salvar Alterações"/>
        </form>

        <a class="voltar" href="controller.do?acao=ListarSetores">&larr; Voltar à Lista</a>
    </body>
</html>