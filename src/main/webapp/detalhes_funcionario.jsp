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
            <c:if test="${not empty funcionario.dataDemissao}">
                <div class="aviso-demitido">Demitido em: <fmt:formatDate value="${funcionario.dataDemissao}" pattern="dd/MM/yyyy"/></div>
            </c:if>

            <h2>Informações Pessoais</h2>
            <div class="linha"><strong>ID:</strong> ${funcionario.id}</div>
            <div class="linha"><strong>Matrícula:</strong> ${funcionario.matricula}</div>
            <div class="linha"><strong>Nome:</strong> ${funcionario.nome}</div>
            <div class="linha"><strong>CPF:</strong> ${funcionario.cpfFormatado}</div>
            <div class="linha"><strong>Telefone:</strong> ${funcionario.telefoneFormatado}</div>
            <div class="linha"><strong>Data de Admissão:</strong> <fmt:formatDate value="${funcionario.dataAdmissao}" pattern="dd/MM/yyyy"/></div>
            <div class="linha"><strong>Nível:</strong> ${funcionario.nivel}</div>
            <div class="linha"><strong>Função:</strong> ${funcionario.funcao}</div>
            <div class="linha"><strong>Salário:</strong> R$ <fmt:formatNumber value="${funcionario.salarioBase}" minFractionDigits="2"/></div>
            <div class="linha"><strong>Setor:</strong> 
                <c:choose>
                    <c:when test="${not empty setor}">${setor.nome}</c:when>
                    <c:otherwise>Não vinculado</c:otherwise>
                </c:choose>
            </div>

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

            <div class="botoes">
                <c:url value="controller.do" var="urlEditar">
                    <c:param name="acao" value="EditarFuncionario"/>
                    <c:param name="id" value="${funcionario.id}"/>
                </c:url>
                <a href="${urlEditar}" class="btn btn-editar">Editar</a>
                
                <c:if test="${empty funcionario.dataDemissao}">
                    <c:url value="controller.do" var="urlDemitir">
                        <c:param name="acao" value="AbrirConfirmacao"/>
                        <c:param name="acaoDestino" value="DemitirFuncionario"/>
                        <c:param name="acaoVoltar" value="DetalharFuncionario"/>
                        <c:param name="id" value="${funcionario.id}"/>
                        <c:param name="msg" value="Deseja registrar a demissão de ${funcionario.nome} hoje?"/>
                    </c:url>
                    <a href="${urlDemitir}" class="btn btn-demitir">Demitir</a>
                </c:if>

                <c:url value="controller.do" var="urlDeletar">
                    <c:param name="acao" value="AbrirConfirmacao"/>
                    <c:param name="acaoDestino" value="DeletarFuncionario"/>
                    <c:param name="acaoVoltar" value="DetalharFuncionario"/>
                    <c:param name="id" value="${funcionario.id}"/>
                    <c:param name="msg" value="ATENÇÃO: Excluir permanentemente o funcionário e seu endereço do banco de dados?"/>
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
                }).catch(function(err) {
                    alert("Erro ao copiar endereço.");
                });
            }
        </script>
    </body>
</html>