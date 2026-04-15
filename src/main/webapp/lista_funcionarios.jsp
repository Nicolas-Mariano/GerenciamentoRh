<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Funcionario, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de Funcionários</title>
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
                width: 750px;
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
        <h1>Lista de Funcionários</h1>

        <%
            List<Funcionario> lista = (List<Funcionario>) request.getAttribute("funcionarios");
            if (lista == null || lista.isEmpty()) {
        %>
            <p class="vazio">Nenhum funcionário cadastrado.</p>
        <%
            } else {
        %>
        <table>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Matrícula</th>
                <th>Função</th>
                <th>Nível</th>
                <th>Setor ID</th>
            </tr>
            <% for (Funcionario f : lista) { %>
            <tr>
                <td><%= f.getId() %></td>
                <td><%= f.getNome() %></td>
                <td><%= f.getMatricula() %></td>
                <td><%= f.getFuncao() %></td>
                <td><%= f.getNivel() != null ? f.getNivel() : "-" %></td>
                <td><%= f.getIdSetor() %></td>
            </tr>
            <% } %>
        </table>
        <% } %>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>
