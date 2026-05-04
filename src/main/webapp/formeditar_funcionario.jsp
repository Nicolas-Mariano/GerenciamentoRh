<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Editar Funcionário</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Editar Funcionário</h1>

        <c:if test="${not empty erro}">
            <p class="msg-erro">${erro}</p>
        </c:if>

        <form action="controller.do" method="POST">
            <input type="hidden" name="acao" value="AtualizarFuncionario"/>
            <input type="hidden" name="txtId" value="${funcionario.id}"/>
            <input type="hidden" name="txtIdEndereco" value="${funcionario.idEndereco}"/>

            <c:if test="${not empty funcionario.dataDemissao}">
                <div class="aviso-demitido" style="margin-bottom: 20px;">
                    <strong>Status: Demitido</strong><br/>
                    <label style="color: #cc0000; cursor: pointer;">
                        <input type="checkbox" name="chkReativar" value="true"> 
                        <strong>REATIVAR FUNCIONÁRIO (Anular demissao)</strong>
                    </label>
                </div>
            </c:if>

            <h2>Dados do Funcionário</h2>

            <label>Nome:</label>
            <input type="text" name="txtNome" value="${funcionario.nome}" required/><br/>

            <label>CPF:</label>
            <input type="text" name="txtCpf" id="cpf" value="${funcionario.cpf}" required/><br/>

            <label>Telefone:</label>
            <input type="text" name="txtTelefone" id="telefone" value="${funcionario.telefone}" required/><br/>

            <label>Setor:</label>
            <select name="txtIdSetor" required>
                <c:forEach var="s" items="${setores}">
                    <option value="${s.id}" <c:if test="${s.id == funcionario.idSetor}">selected</c:if>>${s.nome}</option>
                </c:forEach>
            </select><br/>

            <label>Nível:</label>
            <select name="txtNivel" required>
                <c:forEach var="n" items="Jovem Aprendiz,Estagiário,Junior,Pleno,Senior">
                    <option value="${n}" <c:if test="${funcionario.nivel == n}">selected</c:if>>${n}</option>
                </c:forEach>
            </select><br/>

            <label>Função:</label>
            <input type="text" name="txtFuncao" value="${funcionario.funcao}" required/><br/>

            <label>Salário Base:</label>
            <input type="text" name="txtSalario" id="salario" value="${funcionario.salarioBase}" required/><br/>

            <label>Data de Admissão:</label>
            <fmt:formatDate value="${funcionario.dataAdmissao}" pattern="yyyy-MM-dd" var="dataFmt"/>
            <input type="date" name="txtDataAdmissao" value="${dataFmt}" required/><br/>

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
            const d = document;
            const applyMask = (id) => {
                const el = d.getElementById(id);
                if (!el) return;

                const format = () => {
                    let v = el.value.replace(/\D/g, '');
                    if (id === 'salario') {
                        if (v === '') return;
                        v = (v / 100).toFixed(2).replace('.', ',');
                        v = v.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
                        el.value = 'R$ ' + v;
                    } else {
                        const masks = {
                            cpf: v => v.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4").substring(0, 14),
                            cep: v => v.replace(/(\d{5})(\d{3})/, "$1-$2").substring(0, 9),
                            telefone: v => {
                                if (v.length > 10) return v.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3").substring(0, 15);
                                return v.replace(/(\d{2})(\d{4})(\d{4})/, "($1) $2-$3").substring(0, 14);
                            }
                        };
                        el.value = masks[id] ? masks[id](v) : v;
                    }
                };

                el.addEventListener('input', format);

                if (el.value) {
                    if (id === 'salario') {
                        let val = parseFloat(el.value.replace(',', '.'));
                        el.value = Math.round(val * 100).toString();
                    }
                    format();
                }
            };
            ['cpf', 'telefone', 'cep', 'salario'].forEach(id => applyMask(id));
        </script>
    </body>
</html>