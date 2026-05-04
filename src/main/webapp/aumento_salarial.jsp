<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Aplicar Aumento Salarial</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Aplicar Aumento Salarial</h1>

        <c:if test="${not empty erro}">
            <p class="msg-erro">${erro}</p>
        </c:if>

        <form action="controller.do" method="GET">
            <input type="hidden" name="acao" value="AbrirAplicarAumento"/>

            <label>1. Escolha o Setor e busque:</label>
            <div style="display: flex; gap: 10px;">
                <select name="idSetor" required>
                    <option value="">-- Selecione o Setor --</option>
                    <c:forEach var="s" items="${setores}">
                        <option value="${s.id}" <c:if test="${s.id == setorSelecionado}">selected</c:if>>
                            ${s.nome}
                        </option>
                    </c:forEach>
                </select>
                <input type="submit" value="Buscar" style="margin-bottom: 16px;"/>
            </div>
        </form>

        <c:if test="${not empty setorSelecionado}">
            <form action="controller.do" method="POST" style="margin-top: 20px;">
                <input type="hidden" name="acao" value="AplicarAumento"/>

                <label>2. Selecione o Funcionário (Ativos):</label>
                <select name="idFuncionario" required>
                    <c:forEach var="f" items="${funcionarios}">
                        <option value="${f.id}">${f.nome} (Função: ${f.funcao})</option>
                    </c:forEach>
                    <c:if test="${empty funcionarios}">
                        <option value="" disabled selected>Nenhum funcionário ativo neste setor</option>
                    </c:if>
                </select>

                <c:if test="${not empty funcionarios}">
                    <label>3. Percentual de Aumento (%):</label>
                    <input type="text" name="percentualAumento" pattern="[0-9]+([,\.][0-9]+)?" title="Digite um número válido, ex: 10 ou 10.5" placeholder="Ex: 10.5" required/><br/>

                    <input type="submit" value="Aplicar Aumento"/>
                </c:if>
            </form>
        </c:if>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>