<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Aplicar Aumento Salarial</title>
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
            form {
                background-color: #fff;
                border: 1px solid #ccc;
                padding: 20px;
                width: 380px;
            }
            label {
                display: block;
                margin-bottom: 4px;
                font-size: 14px;
                color: #333;
            }
            input[type="text"] {
                width: 100%;
                padding: 5px;
                margin-bottom: 14px;
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
        <h1>Aplicar Aumento Salarial</h1>

        <% if (request.getAttribute("erro") != null) { %>
            <p class="msg-erro"><%= request.getAttribute("erro") %></p>
        <% } %>

        <form action="controller.do" method="Post">
            <input type="hidden" name="acao" value="AplicarAumento"/>

            <label>ID do Funcionário:</label>
            <input type="text" name="txtIdFuncionario"/><br/>

            <label>Percentual de Aumento (%):</label>
            <input type="text" name="txtPercentual" placeholder="Ex: 10.5"/><br/>

            <input type="submit" value="Aplicar"/>
        </form>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>
