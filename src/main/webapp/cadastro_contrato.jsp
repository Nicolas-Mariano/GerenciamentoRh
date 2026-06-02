<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Novo Contrato</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Novo Contrato para: ${funcionario.nome}</h1>

        <c:if test="${not empty erro}">
            <p class="msg-erro">${erro}</p>
        </c:if>

        <form action="controller.do" method="POST">
            <input type="hidden" name="acao" value="RecontratarFuncionario"/>
            <input type="hidden" name="id" value="${funcionario.id}"/>

            <h2>Dados do Contrato</h2>

            <label>Setor:</label>
            <select name="txtIdSetor" required>
                <option value="">-- Selecione --</option>
                <c:forEach var="s" items="${setores}">
                    <option value="${s.id}" <c:if test="${s.id == idSetorSelecionado}">selected</c:if>>${s.nome}</option>
                </c:forEach>
            </select><br/>

            <label>Nível de Senioridade:</label>
            <select name="txtNivel" required>
                <option value="">-- Selecione --</option>
                <option value="JOVEM_APRENDIZ" <c:if test="${nivelSelecionado == 'JOVEM_APRENDIZ'}">selected</c:if>>Jovem Aprendiz</option>
                <option value="ESTAGIARIO" <c:if test="${nivelSelecionado == 'ESTAGIARIO'}">selected</c:if>>Estagiário</option>
                <option value="JUNIOR" <c:if test="${nivelSelecionado == 'JUNIOR'}">selected</c:if>>Junior</option>
                <option value="PLENO" <c:if test="${nivelSelecionado == 'PLENO'}">selected</c:if>>Pleno</option>
                <option value="SENIOR" <c:if test="${nivelSelecionado == 'SENIOR'}">selected</c:if>>Senior</option>
            </select><br/>

            <label>Salário Base:</label>
            <input type="text" name="txtSalario" id="salario" value="${salarioDigitado}" placeholder="R$ 0,00" required/><br/>

            <label>Data de Admissão:</label>
            <input type="date" name="txtDataAdmissao" value="${dataAdmissaoDigitada}" required/><br/>

            <input type="submit" value="Registrar Contrato"/>
        </form>

        <a class="voltar" href="controller.do?acao=DetalharFuncionario&id=${funcionario.id}">&larr; Voltar aos Detalhes</a>
        <script>
            const salarioEl = document.getElementById('salario');
            if (salarioEl) {
                salarioEl.addEventListener('input', function(e) {
                    let v = e.target.value.replace(/\D/g, '');
                    v = (v / 100).toFixed(2).replace('.', ',');
                    v = v.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
                    e.target.value = v ? 'R$ ' + v : '';
                });
            }
        </script>
    </body>
</html>
