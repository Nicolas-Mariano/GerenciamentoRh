<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Lista de Endereços</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Lista de Endereços</h1>

        <form method="get" action="controller.do" style="margin-bottom: 1rem;">
            <input type="hidden" name="acao" value="ListarEnderecos">
            <label>
                <input type="checkbox" name="incluirDemitidos" value="true"
                       onchange="this.form.submit()"
                       ${incluirDemitidos ? 'checked' : ''}>
                Incluir endereços de demitidos
            </label>
        </form>

        <c:choose>
            <c:when test="${empty enderecos}">
                <p class="vazio">Nenhum endereço cadastrado.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Funcionário</th>
                            <th>Logradouro</th>
                            <th>Número</th>
                            <th>Bairro</th>
                            <th>Cidade</th>
                            <th>UF</th>
                            <th>CEP</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${enderecos}">
                            <tr>
                                <td>${e.id}</td>
                                <td>
                                    <c:out value="${not empty e.nomeFuncionario ? e.nomeFuncionario : '-'}" />
                                </td>
                                <td>${e.logradouro}</td>
                                <td>${e.numEndereco}</td>
                                <td>${e.bairro}</td>
                                <td>${e.cidade}</td>
                                <td>${e.estado}</td>
                                <td>${e.cepFormatado}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty e.funcionario}">
                                            <a href="controller.do?acao=DetalharFuncionario&id=${e.funcionario.id}" class="btn-detalhes">
                                                Ver Funcionário &rarr;
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="sem-vinculo">Sem vínculo</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>