<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Operação Realizada</title>
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
            .msg {
                background-color: #fff;
                border: 1px solid #ccc;
                padding: 16px 20px;
                width: 380px;
                font-size: 14px;
                color: #2a7a2a;
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
        </style>
    </head>
    <body>
        <h1>Operação Realizada</h1>

        <div class="msg">
            <% 
                String msg = (String) request.getAttribute("mensagem");
                if (msg != null) { 
            %>
                <%= msg %>
            <%
                } else {
            %>
                Operação realizada com sucesso.
            <%
                }
            %>
        </div>

        <a class="voltar" href="index.jsp">&larr; Voltar ao Menu</a>
    </body>
</html>
