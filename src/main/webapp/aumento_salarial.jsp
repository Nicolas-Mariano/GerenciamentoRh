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

                <label>2. Selecione o Contrato (Ativos):</label>
                <select name="idContrato" required>
                    <c:forEach var="c" items="${contratos}">
                        <option value="${c.id}">${c.funcionario.nome} — Matrícula: ${c.matricula} (${c.nivelSenioridade})</option>
                    </c:forEach>
                    <c:if test="${empty contratos}">
                        <option value="" disabled selected>Nenhum contrato ativo neste setor</option>
                    </c:if>
                </select>

                <c:if test="${not empty contratos}">
                    <label>3. Tipo de Aumento:</label>
                    <select name="tipoAumento" required>
                        <option value="PERCENTUAL" <c:if test="${tipoAumentoSelecionado == 'PERCENTUAL'}">selected</c:if>>Percentual (%)</option>
                        <option value="BONUS" <c:if test="${tipoAumentoSelecionado == 'BONUS'}">selected</c:if>>Bônus fixo (R$)</option>
                    </select><br/>

                    <label>4. Valor:</label>
                    <input type="text" name="valorAumento" value="${valorAumentoDigitado}" placeholder="Ex: 10.5 ou 500" required/><br/>

                    <input type="submit" value="Aplicar Aumento"/>
                </c:if>
            </form>
        </c:if>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>
