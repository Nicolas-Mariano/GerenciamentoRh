<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Cadastro de Funcionário</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Cadastro de Funcionário</h1>

        <c:if test="${not empty erro}">
            <p class="msg-erro">${erro}</p>
        </c:if>

        <form action="controller.do" method="POST">
            <input type="hidden" name="acao" value="CadastrarFuncionario"/>

            <h2>Dados Pessoais</h2>

            <label>Nome:</label>
            <input type="text" name="txtNome" placeholder="Nome completo" required/><br/>

            <label>CPF:</label>
            <input type="text" name="txtCpf" id="cpf" placeholder="000.000.000-00" required/><br/>

            <label>Telefone:</label>
            <input type="text" name="txtTelefone" id="telefone" placeholder="(00) 00000-0000" required/><br/>

            <label>E-mail:</label>
            <input type="email" name="txtEmail" placeholder="email@exemplo.com"/><br/>

            <hr/>
            <h2>Contrato</h2>

            <label>Setor:</label>
            <select name="txtIdSetor" required>
                <option value="">-- Selecione --</option>
                <c:forEach var="s" items="${setores}">
                    <option value="${s.id}">${s.nome}</option>
                </c:forEach>
            </select><br/>

            <label>Nível de Senioridade:</label>
            <select name="txtNivel" required>
                <option value="">-- Selecione --</option>
                <option value="JUNIOR">Junior</option>
                <option value="PLENO">Pleno</option>
                <option value="SENIOR">Senior</option>
            </select><br/>

            <label>Salário Base:</label>
            <input type="text" name="txtSalario" id="salario" placeholder="R$ 0,00" required/><br/>

            <label>Data de Admissão:</label>
            <input type="date" name="txtDataAdmissao" required/><br/>

            <hr/>
            <h2>Endereço</h2>

            <label>CEP:</label>
            <input type="text" name="txtCep" id="cep" placeholder="00000-000" required/><br/>

            <label>Estado (sigla):</label>
            <select name="txtEstado" required>
                <option value="">-- UF --</option>
                <option>AC</option><option>AL</option><option>AP</option><option>AM</option>
                <option>BA</option><option>CE</option><option>DF</option><option>ES</option>
                <option>GO</option><option>MA</option><option>MT</option><option>MS</option>
                <option>MG</option><option>PA</option><option>PB</option><option>PR</option>
                <option>PE</option><option>PI</option><option>RJ</option><option>RN</option>
                <option>RS</option><option>RO</option><option>RR</option><option>SC</option>
                <option>SP</option><option>SE</option><option>TO</option>
            </select><br/>

            <label>Cidade:</label>
            <input type="text" name="txtCidade" required/><br/>

            <label>Bairro:</label>
            <input type="text" name="txtBairro" required/><br/>

            <label>Logradouro:</label>
            <input type="text" name="txtLogradouro" required/><br/>

            <label>Número:</label>
            <input type="text" name="txtNumEndereco" required/><br/>

            <label>Complemento:</label>
            <input type="text" name="txtComplemento"/><br/>

            <input type="submit" value="Cadastrar"/>
        </form>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
        <script>
            const d = document;
            const applyMask = (id) => {
                const el = d.getElementById(id);
                if (!el) return;
                el.addEventListener('input', e => {
                    let v = e.target.value.replace(/\D/g, '');
                    if (id === 'salario') {
                        v = (v / 100).toFixed(2).replace('.', ',');
                        v = v.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
                        e.target.value = v ? 'R$ ' + v : '';
                        return;
                    }
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
            ['cpf', 'telefone', 'cep', 'salario'].forEach(id => applyMask(id));
        </script>
    </body>
</html>
