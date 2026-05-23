<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Editar Funcionário</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Editar Dados Pessoais</h1>

        <c:if test="${not empty erro}">
            <p class="msg-erro">${erro}</p>
        </c:if>

        <c:if test="${not empty contratoAtivo}">
            <p class="msg" style="background:#e8f5e9; border:1px solid #4caf50; padding:8px; border-radius:4px;">
                Contrato ativo: <strong>${contratoAtivo.matricula}</strong> — Setor: ${contratoAtivo.setor.nome}
                (Salário, nível e setor são gerenciados pelo Contrato)
            </p>
        </c:if>

        <form action="controller.do" method="POST">
            <input type="hidden" name="acao" value="AtualizarFuncionario"/>
            <input type="hidden" name="txtId" value="${funcionario.id}"/>
            <input type="hidden" name="txtIdEndereco" value="${endereco.id}"/>

            <h2>Dados Pessoais</h2>

            <label>Nome:</label>
            <input type="text" name="txtNome" value="${funcionario.nome}" required/><br/>

            <label>CPF:</label>
            <input type="text" name="txtCpf" id="cpf" value="${funcionario.cpf}" required/><br/>

            <label>Telefone:</label>
            <input type="text" name="txtTelefone" id="telefone" value="${funcionario.telefone}" required/><br/>

            <label>E-mail:</label>
            <input type="email" name="txtEmail" value="${funcionario.email}"/><br/>

            <hr/>
            <h2>Endereço</h2>
            <label>CEP:</label>
            <input type="text" name="txtCep" id="cep" value="${endereco.cep}" required/><br/>

            <label>Estado (sigla):</label>
            <select name="txtEstado" required>
                <c:forEach var="uf" items="AC,AL,AP,AM,BA,CE,DF,ES,GO,MA,MT,MS,MG,PA,PB,PR,PE,PI,RJ,RN,RS,RO,RR,SC,SP,SE,TO">
                    <option value="${uf}" <c:if test="${uf == endereco.estado}">selected</c:if>>${uf}</option>
                </c:forEach>
            </select><br/>

            <label>Cidade:</label>
            <input type="text" name="txtCidade" value="${endereco.cidade}" required/><br/>

            <label>Bairro:</label>
            <input type="text" name="txtBairro" value="${endereco.bairro}" required/><br/>

            <label>Logradouro:</label>
            <input type="text" name="txtLogradouro" value="${endereco.logradouro}" required/><br/>

            <label>Número:</label>
            <input type="text" name="txtNumEndereco" value="${endereco.numEndereco}" required/><br/>

            <label>Complemento:</label>
            <input type="text" name="txtComplemento" value="${endereco.complemento}"/><br/>

            <input type="submit" value="Salvar Alterações"/>
        </form>

        <c:url value="controller.do" var="urlVoltar">
            <c:param name="acao" value="DetalharFuncionario"/>
            <c:param name="id" value="${funcionario.id}"/>
        </c:url>
        <a class="voltar" href="${urlVoltar}">&larr; Voltar aos Detalhes</a>

        <script>
            const applyMask = (id) => {
                const el = document.getElementById(id);
                if (!el) return;
                el.addEventListener('input', e => {
                    let v = e.target.value.replace(/\D/g, '');
                    const masks = {
                        cpf: v => v.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4").substring(0, 14),
                        cep: v => v.replace(/(\d{5})(\d{3})/, "$1-$2").substring(0, 9),
                        telefone: v => {
                            if (v.length > 10) return v.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3").substring(0, 15);
                            return v.replace(/(\d{2})(\d{4})(\d{4})/, "($1) $2-$3").substring(0, 14);
                        }
                    };
                    e.target.value = masks[id] ? masks[id](v) : v;
                });
            };
            ['cpf', 'telefone', 'cep'].forEach(id => applyMask(id));
        </script>
    </body>
</html>
