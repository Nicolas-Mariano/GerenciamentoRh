<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Detalhes do Funcionário</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Detalhes do Funcionário</h1>

        <div class="card">
            <c:if test="${empty contratoAtivo}">
                <div class="aviso-demitido">Funcionário sem contrato ativo (demitido ou não contratado)</div>
            </c:if>

            <h2>Dados Pessoais</h2>
            <div class="linha"><strong>ID:</strong> ${funcionario.id}</div>
            <div class="linha"><strong>Nome:</strong> ${funcionario.nome}</div>
            <div class="linha"><strong>CPF:</strong> ${funcionario.cpfFormatado}</div>
            <div class="linha"><strong>Telefone:</strong> ${funcionario.telefoneFormatado}</div>
            <div class="linha"><strong>E-mail:</strong> ${funcionario.email}</div>

            <h2 style="margin-top: 20px;">Endereço</h2>
            <c:choose>
                <c:when test="${not empty endereco}">
                    <div class="campo-copiar">
                        <span class="texto-endereco" id="textoParaCopiar">${endereco.enderecoPadronizado}</span>
                        <button class="btn-copiar" onclick="copiarEndereco()">Copiar</button>
                    </div>
                </c:when>
                <c:otherwise><p>Endereço não cadastrado.</p></c:otherwise>
            </c:choose>

            <c:if test="${not empty contratoAtivo}">
                <h2 style="margin-top: 20px;">Contrato Ativo</h2>
                <div class="linha"><strong>Matrícula:</strong> ${contratoAtivo.matricula}</div>
                <div class="linha"><strong>Setor:</strong> ${contratoAtivo.setor.nome}</div>
                <div class="linha"><strong>Nível:</strong> ${contratoAtivo.nivelSenioridade}</div>
                <div class="linha"><strong>Salário Base:</strong> R$ <fmt:formatNumber value="${contratoAtivo.salarioBase}" minFractionDigits="2"/></div>
                <div class="linha"><strong>Data de Admissão:</strong> <fmt:formatDate value="${contratoAtivo.dataAdmissao}" pattern="dd/MM/yyyy"/></div>

                <div class="botoes" style="margin-top: 10px;">
                    <c:url value="controller.do" var="urlDemitir">
                        <c:param name="acao" value="AbrirConfirmacao"/>
                        <c:param name="acaoDestino" value="DemitirFuncionario"/>
                        <c:param name="acaoVoltar" value="DetalharFuncionario"/>
                        <c:param name="id" value="${funcionario.id}"/>
                        <c:param name="idContrato" value="${contratoAtivo.id}"/>
                        <c:param name="msg" value="Registrar demissão de ${funcionario.nome} na data de hoje?"/>
                    </c:url>
                    <a href="${urlDemitir}" class="btn btn-demitir">Demitir</a>
                </div>
            </c:if>

            <c:if test="${not empty historico}">
                <h2 style="margin-top: 20px;">Histórico de Contratos</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Matrícula</th><th>Setor</th><th>Nível</th>
                            <th>Admissão</th><th>Demissão</th><th>Motivo</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${historico}">
                            <tr>
                                <td>${c.matricula}</td>
                                <td>${c.setor.nome}</td>
                                <td>${c.nivelSenioridade}</td>
                                <td><fmt:formatDate value="${c.dataAdmissao}" pattern="dd/MM/yyyy"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty c.dataDemissao}"><fmt:formatDate value="${c.dataDemissao}" pattern="dd/MM/yyyy"/></c:when>
                                        <c:otherwise><em>Ativo</em></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${c.motivoDesligamento}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <div class="botoes" style="margin-top: 20px;">
                <c:url value="controller.do" var="urlEditar">
                    <c:param name="acao" value="EditarFuncionario"/>
                    <c:param name="id" value="${funcionario.id}"/>
                </c:url>
                <a href="${urlEditar}" class="btn btn-editar">Editar Dados Pessoais</a>

                <c:url value="controller.do" var="urlDeletar">
                    <c:param name="acao" value="AbrirConfirmacao"/>
                    <c:param name="acaoDestino" value="DeletarFuncionario"/>
                    <c:param name="acaoVoltar" value="DetalharFuncionario"/>
                    <c:param name="id" value="${funcionario.id}"/>
                    <c:param name="msg" value="ATENÇÃO: Excluir permanentemente o funcionário do banco de dados?"/>
                </c:url>
                <a href="${urlDeletar}" class="btn btn-deletar">Deletar</a>
            </div>
        </div>
        <a class="voltar" href="controller.do?acao=ListarFuncionarios">&larr; Voltar</a>

        <script>
            function copiarEndereco() {
                var texto = document.getElementById("textoParaCopiar").innerText;
                navigator.clipboard.writeText(texto).then(function() {
                    alert("Endereço copiado para a área de transferência!");
                }).catch(function() {
                    alert("Erro ao copiar endereço.");
                });
            }
        </script>
    </body>
</html>
