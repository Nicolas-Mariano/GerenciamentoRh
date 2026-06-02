<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Lista de Funcionários</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Lista de Funcionários</h1>
        <c:if test="${not empty mensagem}"><p class="msg">${mensagem}</p></c:if>
        <c:if test="${not empty erro}"><p class="msg-erro">${erro}</p></c:if>
        <form method="get" action="controller.do" style="margin-bottom: 1rem;">
            <input type="hidden" name="acao" value="ListarFuncionarios">
            <label>
                <input type="checkbox" name="incluirDemitidos" value="true"
                       onchange="this.form.submit()" ${incluirDemitidos ? 'checked' : ''}>
                Incluir demitidos
            </label>
        </form>
        <c:choose>
            <c:when test="${empty funcionarios}"><p class="vazio">Nenhum funcionário encontrado.</p></c:when>
            <c:otherwise>
                <table>
                    <tr><th>ID</th><th>Nome</th><th>CPF</th><th>E-mail</th><th>Ações</th></tr>
                    <c:forEach var="f" items="${funcionarios}">
                        <tr>
                            <td>${f.id}</td>
                            <td>${f.nome}</td>
                            <td>${f.cpfFormatado}</td>
                            <td>${f.email}</td>
                            <td><a href="controller.do?acao=DetalharFuncionario&id=${f.id}" class="btn-detalhes">Ver →</a></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
        <a class="voltar" href="index.jsp">← Menu</a>
    </body>
</html>
