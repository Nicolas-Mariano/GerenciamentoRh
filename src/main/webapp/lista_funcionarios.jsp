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

        <c:choose>
            <c:when test="${empty funcionarios}"><p class="vazio">Nenhum funcionário.</p></c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <th>ID</th><th>Nome</th><th>CPF</th><th>Matrícula</th><th>Setor</th><th>Função</th><th>Ações</th>
                    </tr>
                    <c:forEach var="f" items="${funcionarios}">
                        <tr>
                            <td>${f.id}</td>
                            <td>${f.nome} <c:if test="${not empty f.dataDemissao}"><span class="demitido">(Demitido)</span></c:if></td>
                            <td>${f.cpfFormatado}</td>
                            <td>${f.matricula}</td>
                            
                            <td>
                                <c:set var="nomeSetor" value="Não vinculado" />
                                <c:forEach var="s" items="${setores}">
                                    <c:if test="${s.id == f.idSetor}"><c:set var="nomeSetor" value="${s.nome}" /></c:if>
                                </c:forEach>
                                ${nomeSetor}
                            </td>
                            
                            <td>${f.funcao}</td>
                            <td><a href="controller.do?acao=DetalharFuncionario&id=${f.id}" class="btn-detalhes">Ver &rarr;</a></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
        <a class="voltar" href="index.jsp">&larr; Menu</a>
    </body>
</html>