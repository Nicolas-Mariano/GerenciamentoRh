<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Setor, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cadastro de Funcionário</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 30px;
                background-color: #f5f5f5;
            }
            h1 {
                color: #333;
                border-bottom: 2px solid #555;
                padding-bottom: 8px;
            }
            h2 {
                color: #555;
                font-size: 15px;
                margin-top: 20px;
                margin-bottom: 8px;
            }
            form {
                background-color: #fff;
                border: 1px solid #ccc;
                padding: 20px;
                width: 480px;
            }
            label {
                display: block;
                margin-bottom: 4px;
                font-size: 14px;
                color: #333;
            }
            input[type="text"], input[type="date"], select {
                width: 100%;
                padding: 5px;
                margin-bottom: 12px;
                border: 1px solid #aaa;
                font-size: 14px;
                box-sizing: border-box;
            }
            input[type="submit"] {
                padding: 6px 16px;
                background-color: #4a4a4a;
                color: white;
                border: none;
                cursor: pointer;
                font-size: 14px;
            }
            input[type="submit"]:hover {
                background-color: #333;
            }
            hr {
                border: none;
                border-top: 1px solid #ddd;
                margin: 16px 0 8px 0;
            }
            .voltar {
                display: inline-block;
                margin-top: 12px;
                font-size: 13px;
                color: #0066cc;
                text-decoration: none;
            }
            .voltar:hover {
                text-decoration: underline;
            }
            .msg-erro {
                color: red;
                font-size: 13px;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>
        <h1>Cadastro de Funcionário</h1>

        <% if (request.getAttribute("erro") != null) { %>
            <p class="msg-erro"><%= request.getAttribute("erro") %></p>
        <% } %>

        <form action="controller.do" method="Post">
            <input type="hidden" name="acao" value="CadastrarFuncionario"/>

            <h2>Dados do Funcionário</h2>

            <label>Nome:</label>
            <input type="text" name="txtNome"/><br/>

            <label>CPF (somente números):</label>
            <input type="text" name="txtCpf" maxlength="11"/><br/>

            <label>Matrícula:</label>
            <input type="text" name="txtMatricula"/><br/>

            <label>Função:</label>
            <input type="text" name="txtFuncao"/><br/>

            <label>Nível:</label>
            <select name="txtNivel">
                <option value="">-- Selecione --</option>
                <option value="Jovem Aprendiz">Jovem Aprendiz</option>
                <option value="Estagiário">Estagiário</option>
                <option value="Junior">Junior</option>
                <option value="Pleno">Pleno</option>
                <option value="Senior">Senior</option>
            </select><br/>

            <label>Salário Base:</label>
            <input type="text" name="txtSalario" placeholder="Ex: 3500.00"/><br/>

            <label>Data de Admissão:</label>
            <input type="date" name="txtDataAdmissao"/><br/>

            <label>Telefone (somente números, 11 dígitos):</label>
            <input type="text" name="txtTelefone" maxlength="11"/><br/>

            <label>Setor:</label>
            <select name="txtIdSetor">
                <option value="">-- Selecione --</option>
                <%
                    List<Setor> setores = (List<Setor>) request.getAttribute("setores");
                    if (setores != null) {
                        for (Setor s : setores) {
                %>
                <option value="<%= s.getId() %>"><%= s.getNome() %></option>
                <%
                        }
                    }
                %>
            </select><br/>

            <hr/>
            <h2>Endereço</h2>

            <label>Logradouro:</label>
            <input type="text" name="txtLogradouro"/><br/>

            <label>Número:</label>
            <input type="text" name="txtNumEndereco"/><br/>

            <label>Complemento:</label>
            <input type="text" name="txtComplemento"/><br/>

            <label>Bairro:</label>
            <input type="text" name="txtBairro"/><br/>

            <label>Cidade:</label>
            <input type="text" name="txtCidade"/><br/>

            <label>Estado (sigla):</label>
            <select name="txtEstado">
                <option value="">-- UF --</option>
                <option>AC</option><option>AL</option><option>AP</option><option>AM</option>
                <option>BA</option><option>CE</option><option>DF</option><option>ES</option>
                <option>GO</option><option>MA</option><option>MT</option><option>MS</option>
                <option>MG</option><option>PA</option><option>PB</option><option>PR</option>
                <option>PE</option><option>PI</option><option>RJ</option><option>RN</option>
                <option>RS</option><option>RO</option><option>RR</option><option>SC</option>
                <option>SP</option><option>SE</option><option>TO</option>
            </select><br/>

            <label>CEP (somente números, 8 dígitos):</label>
            <input type="text" name="txtCep" maxlength="8"/><br/>

            <input type="submit" value="Cadastrar"/>
        </form>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>
