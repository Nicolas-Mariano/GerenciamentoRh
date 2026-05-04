<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Lista de Setores</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Lista de Setores</h1>

        <c:if test="${not empty mensagem}">
            <p class="msg" style="color: green;">${mensagem}</p>
        </c:if>
        <c:if test="${not empty erro}">
            <p class="msg-erro">${erro}</p>
        </c:if>

        <c:choose>
            <c:when test="${empty setores}">
                <p class="vazio">Nenhum setor cadastrado.</p>
            </c:when>
            
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Gerente Responsável</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${setores}">
                            <tr>
                                <td>${s.id}</td>
                                <td>${s.nome}</td>
                                
                                <td>
                                    <c:out value="${not empty s.nomeResponsavel ? s.nomeResponsavel : 'Sem Gerente'}" />
                                </td>
                                
                                <td>
                                    <c:url value="controller.do" var="urlEditar">
                                        <c:param name="acao" value="EditarSetor"/>
                                        <c:param name="id" value="${s.id}"/>
                                    </c:url>
                                    <a class="btn-acao" href="${urlEditar}">Editar</a>
                                    
                                    <c:url value="controller.do" var="urlDeletar">
                                        <c:param name="acao" value="AbrirConfirmacao"/>
                                        <c:param name="acaoDestino" value="DeletarSetor"/>
                                        <c:param name="acaoVoltar" value="ListarSetores"/>
                                        <c:param name="id" value="${s.id}"/>
                                        <c:param name="msg" value="ATENÇÃO: Deseja realmente excluir o setor ${s.nome}?"/>
                                    </c:url>
                                    <a class="btn-excluir" href="${urlDeletar}">Excluir</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

        <br>
        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>