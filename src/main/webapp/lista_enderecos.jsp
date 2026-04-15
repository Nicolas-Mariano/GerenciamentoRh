<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Endereco, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de Endereços</title>
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
            table {
                border-collapse: collapse;
                background-color: #fff;
                width: 800px;
                font-size: 14px;
            }
            table th {
                background-color: #4a4a4a;
                color: white;
                padding: 8px 12px;
                text-align: left;
            }
            table td {
                padding: 7px 12px;
                border-bottom: 1px solid #ddd;
            }
            table tr:hover {
                background-color: #f0f0f0;
            }
            .voltar {
                display: inline-block;
                margin-top: 14px;
                font-size: 13px;
                color: #0066cc;
                text-decoration: none;
            }
            .voltar:hover {
                text-decoration: underline;
            }
            .vazio {
                font-size: 14px;
                color: #777;
            }
        </style>
    </head>
    <body>
        <h1>Lista de Endereços</h1>

        <%
            List<Endereco> lista = (List<Endereco>) request.getAttribute("enderecos");
            if (lista == null || lista.isEmpty()) {
        %>
            <p class="vazio">Nenhum endereço cadastrado.</p>
        <%
            } else {
        %>
        <table>
            <tr>
                <th>ID</th>
                <th>Funcionário</th>
                <th>Logradouro</th>
                <th>Número</th>
                <th>Bairro</th>
                <th>Cidade</th>
                <th>UF</th>
                <th>CEP</th>
            </tr>
            <% for (Endereco e : lista) {
                String nomeFuncionario = (e.getNomeFuncionario() != null) ? e.getNomeFuncionario() : "-";
            %>
            <tr>
                <td><%= e.getId() %></td>
                <td><%= nomeFuncionario %></td>
                <td><%= e.getLogradouro() %></td>
                <td><%= e.getNumEndereco() %></td>
                <td><%= e.getBairro() %></td>
                <td><%= e.getCidade() %></td>
                <td><%= e.getEstado() %></td>
                <td><%= e.getCep() %></td>
            </tr>
            <% } %>
        </table>
        <% } %>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>
