<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Vincular Gerente</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Vincular Gerente ao Setor</h1>
        <c:if test="${not empty erro}"><p class="msg-erro">${erro}</p></c:if>

        <form action="controller.do" method="GET">
            <input type="hidden" name="acao" value="AbrirVincularGerente"/>
            <label>1. Escolha o Setor e busque:</label>
            <div style="display: flex; gap: 10px;">
                <select name="idSetor" required>
                    <option value="">-- Selecione o Setor --</option>
                    <c:forEach var="s" items="${setores}">
                        <option value="${s.id}" <c:if test="${s.id == setorSelecionado}">selected</c:if>>${s.nome}</option>
                    </c:forEach>
                </select>
                <input type="submit" value="Buscar" style="margin-bottom: 16px;"/>
            </div>
        </form>

        <c:if test="${not empty setorSelecionado}">
            <form action="controller.do" method="POST" style="margin-top: 20px;">
                <input type="hidden" name="acao" value="AtualizarGerenteSetor"/>
                <input type="hidden" name="txtIdSetor" value="${setorSelecionado}"/>

                <label>2. Escolha o Contrato Responsável (Pleno/Senior):</label>
                <select name="txtIdContrato" required>
                    <c:forEach var="c" items="${contratos}">
                        <option value="${c.id}">${c.funcionario.nome} — ${c.nivelSenioridade} (Matrícula: ${c.matricula})</option>
                    </c:forEach>
                    <c:if test="${empty contratos}">
                        <option value="" disabled selected>Nenhum contrato Pleno/Senior ativo neste setor</option>
                    </c:if>
                </select>

                <c:if test="${not empty contratos}">
                    <input type="submit" value="Vincular Gerente"/>
                </c:if>
            </form>
        </c:if>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>
